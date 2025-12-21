package com.github.calhanwynters.refproductmngr.businessinfra.infrastructure.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.FeatureAbstractClass;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.FeatureFixedPriceEntity;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.FeatureScalingPriceEntity;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.BusinessIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregate;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantEntity;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantIdVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the Product Command Repository following Pure DDD and Outbox Patterns.
 * Uses JdbcTemplate for performance and PostgreSQL JSONB for CQRS snapshots.
 */
@Repository
public class ProductCommandRepositoryImpl implements ProductCommandRepository {
    private static final Logger logger = LoggerFactory.getLogger(ProductCommandRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductCommandRepositoryImpl(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * PURE DDD SAVE: Handles State Transitions (Updates and Soft Deletes).
     * This is used by VariantUpdateStatusService and ProductDeleteService.softDelete().
     * Saves the product aggregate using batch updates for performance.
     * NOTE: For significant performance gains in PostgreSQL (2x to 3x speedup),
     * the JDBC connection string MUST include: reWriteBatchedInserts=true
     */
    /**
     * PURE DDD SAVE: Handles State Transitions and Schema Evolution.
     * The schema_version column tracks the structure of the data for backward compatibility.
     */
    @Override
    @Transactional
    public void save(ProductAggregate product) {
        // Identity mapping
        UUID productId = UUID.fromString(product.id().value());
        UUID businessId = UUID.fromString(product.businessIdVO().value());

        // Use your VersionVO 'num' for business context/schema versioning
        int currentBusinessVersion = product.version().num();

        // 1. UPSERT Main Product (Now including Business Version)
        final String productSql = """
        INSERT INTO products (id, business_id_vo, category, description, is_deleted, schema_version)
        VALUES (?, ?, ?, ?, ?, ?)
        ON CONFLICT (id) DO UPDATE SET
            category = EXCLUDED.category,
            description = EXCLUDED.description,
            is_deleted = EXCLUDED.is_deleted,
            schema_version = EXCLUDED.schema_version;
    """;
        jdbcTemplate.update(productSql,
                productId,
                businessId,
                product.category().value(),
                product.description().text(),
                product.isDeleted(),
                currentBusinessVersion);

        // 2. UPSERT Variants
        final String variantSql = """
        INSERT INTO product_variants (id, product_id, sku, status, base_price, current_price, weight, care_instructions)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT (id) DO UPDATE SET
            status = EXCLUDED.status,
            current_price = EXCLUDED.current_price,
            weight = EXCLUDED.weight,
            care_instructions = EXCLUDED.care_instructions;
    """;

        List<VariantEntity> variants = List.copyOf(product.variants());
        jdbcTemplate.batchUpdate(variantSql, variants, 500, (ps, variant) -> {
            ps.setObject(1, UUID.fromString(variant.id().value()));
            ps.setObject(2, productId);
            ps.setString(3, variant.sku().sku());
            ps.setString(4, variant.status().name());
            ps.setBigDecimal(5, variant.basePrice().value());
            ps.setBigDecimal(6, variant.currentPrice().value());
            ps.setObject(7, variant.weight().amount());
            ps.setString(8, variant.careInstructions().instructions());
        });

        // 3. UPSERT Feature Definitions (Master Features Table)
        // Extract unique features across all variants to minimize DB hits
        Map<String, FeatureAbstractClass> distinctFeatures = variants.stream()
                .flatMap(v -> v.getFeatures().stream())
                .collect(Collectors.toMap(f -> f.getId().value(), f -> f, (f1, f2) -> f1));

        final String featureDefinitionSql = """
        INSERT INTO features (id, name, label, description, is_unique, feature_type, attributes)
        VALUES (?, ?, ?, ?, ?, ?, ?::jsonb)
        ON CONFLICT (id) DO UPDATE SET
            name = EXCLUDED.name,
            label = EXCLUDED.label,
            description = EXCLUDED.description,
            is_unique = EXCLUDED.is_unique,
            attributes = EXCLUDED.attributes;
    """;

        jdbcTemplate.batchUpdate(featureDefinitionSql, distinctFeatures.values(), 500, (ps, feature) -> {
            ps.setObject(1, UUID.fromString(feature.getId().value()));
            ps.setString(2, feature.getNameVO().value());
            ps.setString(3, feature.getLabelVO().value());
            ps.setString(4, feature.getDescription() != null ? feature.getDescription().text() : null);
            ps.setBoolean(5, feature.isUnique());

            // Polymorphic Mapping to JSONB
            Map<String, Object> attrMap = new HashMap<>();
            String type = "BASIC";

            if (feature instanceof FeatureFixedPriceEntity f) {
                type = "FIXED_PRICE";
                attrMap.put("fixedPrice", f.getFixedPrice());
            } else if (feature instanceof FeatureScalingPriceEntity s) {
                type = "SCALING_PRICE";
                attrMap.put("unit", s.getMeasurementUnit().unit());
                attrMap.put("base", s.getBaseAmount());
                attrMap.put("increment", s.getIncrementAmount());
                attrMap.put("max", s.getMaxQuantity());
            }

            ps.setString(6, type);
            ps.setString(7, serializeToJson(attrMap));
        });

        // 4. SYNC Variant-to-Feature Links (Join Table)
        List<UUID> variantUuids = variants.stream()
                .map(v -> UUID.fromString(v.id().value()))
                .toList();

        // Clear old links for the variants in this aggregate to ensure a clean sync
        namedParameterJdbcTemplate.update(
                "DELETE FROM variant_features WHERE variant_id IN (:ids)",
                Map.of("ids", variantUuids)
        );

        final String linkSql = "INSERT INTO variant_features (variant_id, feature_id) VALUES (?, ?)";
        List<FeatureJoin> featureJoins = variants.stream()
                .flatMap(v -> v.getFeatures().stream().map(f -> new FeatureJoin(
                        UUID.fromString(v.id().value()),
                        UUID.fromString(f.getId().value())
                ))).toList();

        jdbcTemplate.batchUpdate(linkSql, featureJoins, 500, (ps, join) -> {
            ps.setObject(1, join.variantId());
            ps.setObject(2, join.featureId());
        });

        // 5. TRANSACTIONAL OUTBOX
        // Captures the full snapshot + business version for event-driven synchronization
        final String outboxSql = """
        INSERT INTO outbox_messages (id, aggregate_type, aggregate_id, event_type, payload, schema_version, created_at)
        VALUES (?, 'PRODUCT', ?, 'PRODUCT_UPDATED', ?::jsonb, ?, NOW())
    """;
        jdbcTemplate.update(outboxSql,
                UUID.randomUUID(),
                product.id().value(),
                serializeToJson(product),
                currentBusinessVersion);
    }

    // Internal record for batch link management
    private record FeatureJoin(UUID variantId, UUID featureId) {}

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
     * Internal Helper: Converts Java objects (Domain Aggregates or Maps) to a JSON string.
     * PostgreSQL's ::jsonb cast in the SQL ensures this is stored optimally.
     */
    private String serializeToJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            // Determine a label for logging to identify what failed
            String identifier = (payload instanceof ProductAggregate p)
                    ? "Product " + p.id().value()
                    : "Generic Object (" + payload.getClass().getSimpleName() + ")";

            logger.error("Failed to serialize {} for database persistence: {}", identifier, e.getMessage());
            throw new RuntimeException("Domain Serialization Error", e);
        }
    }

}
