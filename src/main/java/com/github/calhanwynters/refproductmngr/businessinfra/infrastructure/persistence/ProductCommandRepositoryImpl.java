package com.github.calhanwynters.refproductmngr.businessinfra.infrastructure.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.BusinessIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregate;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantEntity;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantIdVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the Product Command Repository following Pure DDD and Outbox Patterns.
 * Uses JdbcTemplate for performance and PostgreSQL JSONB for CQRS snapshots.
 */
@Repository
public class ProductCommandRepositoryImpl implements ProductCommandRepository {
    private static final Logger logger = LoggerFactory.getLogger(ProductCommandRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public ProductCommandRepositoryImpl(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * PURE DDD SAVE: Handles State Transitions (Updates and Soft Deletes).
     * This is used by VariantUpdateStatusService and ProductDeleteService.softDelete().
     */
    @Override
    @Transactional
    public void save(ProductAggregate product) {
        // 1. UPSERT Main Product
        final String productSql = """
    INSERT INTO products (id, business_id_vo, category, description, is_deleted, version)
    VALUES (?, ?, ?, ?, ?, ?)
    ON CONFLICT (id) DO UPDATE SET
        category = EXCLUDED.category,
        description = EXCLUDED.description,
        is_deleted = EXCLUDED.is_deleted,
        version = EXCLUDED.version;
    """;

        jdbcTemplate.update(productSql,
                product.id().value(), product.businessIdVO().value(),
                product.category().value(), product.description().text(),
                product.isDeleted(), product.version().num());

        // 2. BATCH UPSERT Variants
        final String variantSql = """
    INSERT INTO product_variants (id, product_id, sku, status, base_price, current_price)
    VALUES (?, ?, ?, ?, ?, ?)
    ON CONFLICT (id) DO UPDATE SET
        status = EXCLUDED.status,
        current_price = EXCLUDED.current_price;
    """;

        List<VariantEntity> variants = List.copyOf(product.variants());
        jdbcTemplate.batchUpdate(variantSql, variants, 500, (ps, variant) -> {
            ps.setObject(1, UUID.fromString(variant.id().value())); // Convert String to UUID
            ps.setObject(2, UUID.fromString(product.id().value()));
            ps.setString(3, variant.sku().sku());
            ps.setString(4, variant.status().name());
            ps.setBigDecimal(5, variant.basePrice().value());
            ps.setBigDecimal(6, variant.currentPrice().value());
        });

        // 3. SYNC Variant Features (Delete-and-Batch pattern)
        // FIX: Map VariantIdVO (String) to java.util.UUID to resolve incompatible types
        UUID[] variantUuidArray = variants.stream()
                .map(v -> UUID.fromString(v.id().value()))
                .toArray(UUID[]::new);

        // Postgres requires the second argument to be a JDBC-compliant array type for ANY(?)
        jdbcTemplate.update("DELETE FROM variant_features WHERE variant_id = ANY(?)",
                (Object) variantUuidArray);

        final String featureSql = "INSERT INTO variant_features (variant_id, feature_id) VALUES (?, ?)";
        List<Object[]> featureBatch = variants.stream()
                .flatMap(v -> v.getFeatures().stream()
                        .map(f -> new Object[]{
                                UUID.fromString(v.id().value()), // Convert String to UUID
                                UUID.fromString(f.getId().value()) // Convert String to UUID
                        }))
                .toList();

        if (!featureBatch.isEmpty()) {
            jdbcTemplate.batchUpdate(featureSql, featureBatch);
        }

        // 4. TRANSACTIONAL OUTBOX
        final String outboxSql = """
    INSERT INTO outbox_messages (id, aggregate_type, aggregate_id, event_type, payload, created_at)
    VALUES (?, 'PRODUCT', ?, 'PRODUCT_UPDATED', ?::jsonb, NOW())
    """;

        String payload = serializeToJson(product);
        jdbcTemplate.update(outboxSql, UUID.randomUUID(), product.id().value(), payload);
    }



    /**
     * HARD DELETE: Physical removal of the Aggregate Root.
     * Dashboard consumes 'PRODUCT_HARD_DELETED' to remove the item from display entirely.
     */
    @Override
    @Transactional
    public boolean deleteProductByProductIdAndBusinessId(ProductIdVO id, BusinessIdVO businessId) {
        String deleteSql = "DELETE FROM products WHERE id = ? AND business_id_vo = ?";
        String outboxSql = """
            INSERT INTO outbox_messages (id, aggregate_type, aggregate_id, event_type, payload, created_at)
            VALUES (?, 'PRODUCT', ?, 'PRODUCT_HARD_DELETED', ?::jsonb, NOW())
            """;

        int rows = jdbcTemplate.update(deleteSql, id.value(), businessId.value());
        if (rows > 0) {
            String payload = String.format("{\"productId\": \"%s\", \"businessId\": \"%s\"}", id.value(), businessId.value());
            jdbcTemplate.update(outboxSql, UUID.randomUUID(), id.value(), payload);
            return true;
        }
        return false;
    }

    /**
     * HARD DELETE: Physical removal of all variants.
     */
    @Override
    @Transactional
    public void deleteAllVariantsByProductId(ProductIdVO id) {
        String deleteSql = "DELETE FROM product_variants WHERE product_id = ?";
        String outboxSql = """
            INSERT INTO outbox_messages (id, aggregate_type, aggregate_id, event_type, payload, created_at)
            VALUES (?, 'PRODUCT', ?, 'ALL_VARIANTS_HARD_DELETED', ?::jsonb, NOW())
            """;

        jdbcTemplate.update(deleteSql, id.value());
        String payload = String.format("{\"productId\": \"%s\"}", id.value());
        jdbcTemplate.update(outboxSql, UUID.randomUUID(), id.value(), payload);
    }

    /**
     * HARD DELETE: Physical removal of a single Variant.
     */
    @Override
    @Transactional
    public boolean deleteVariantById(String variantId) {
        String deleteSql = "DELETE FROM product_variants WHERE id = ?";
        String outboxSql = """
            INSERT INTO outbox_messages (id, aggregate_type, aggregate_id, event_type, payload, created_at)
            VALUES (?, 'VARIANT', ?, 'VARIANT_HARD_DELETED', ?::jsonb, NOW())
            """;

        int rows = jdbcTemplate.update(deleteSql, variantId);
        if (rows > 0) {
            String payload = String.format("{\"variantId\": \"%s\"}", variantId);
            jdbcTemplate.update(outboxSql, UUID.randomUUID(), variantId, payload);
            return true;
        }
        return false;
    }

    @Override
    public Optional<ProductAggregate> findById(ProductIdVO id) {
        // To be implemented: Reconstruct aggregate from products table
        return Optional.empty();
    }

    @Override
    public Optional<ProductAggregate> findByVariantId(VariantIdVO variantId) {
        // To be implemented: Join products/variants and reconstruct ProductAggregate
        return Optional.empty();
    }

    /**
     * Internal Helper: Converts the Domain Aggregate (Java Record) to a JSON string.
     * PostgreSQL's ::jsonb cast in the SQL ensures this is stored optimally.
     */
    private String serializeToJson(ProductAggregate product) {
        try {
            return objectMapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize Product {} for outbox: {}", product.id().value(), e.getMessage());
            throw new RuntimeException("Domain Event Serialization Error", e);
        }
    }
}
