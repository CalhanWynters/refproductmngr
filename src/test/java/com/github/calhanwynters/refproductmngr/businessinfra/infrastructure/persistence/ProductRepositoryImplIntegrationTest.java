package com.github.calhanwynters.refproductmngr.businessinfra.infrastructure.persistence;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import java.math.BigDecimal;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper; // Add this import
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
public class ProductRepositoryImplIntegrationTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.0");

    @Autowired
    private MongoTemplate mongoTemplate;

    private ProductRepositoryImpl productRepository;
    private ProductIdVO productId;
    private BusinessIdVO businessId;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepositoryImpl(mongoTemplate);
        productId = ProductIdVO.generate();
        businessId = new BusinessIdVO("BIZ-TEST-2025");
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(ProductAggregate.class);
    }

    @Test
    @DisplayName("Should persist and retrieve a product with 2025 polymorphic features")
    void testPersistAndRetrieveProductWithFeatures() {
        // Arrange
        ProductAggregate product = createValidProductAggregateWithFeatures();

        // Act: Save via template
        mongoTemplate.save(product);

        // Act: Retrieve using repository implementation
        Optional<ProductAggregate> result = productRepository.findProductByProductIdAndBusinessId(productId, businessId);

        // --- SAFE PRINT LOGIC (No extra dependencies) ---
        if (result.isPresent()) {
            try {
                ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
                String json = mapper.writeValueAsString(result.get());
                System.out.println("\n=== RETRIEVED PRODUCT JSON ProductRepositoryImplIntegrationTest PRINT ===");
                System.out.println(json);
                System.out.println("===========================================\n");
            } catch (Exception e) {
                System.out.println("\n=== RETRIEVED PRODUCT TOSTRING (Serialization Fallback) ===");
                System.out.println(result.get());
                System.out.println("===========================================================\n");
            }
        }

        // Assert: Core Identity
        assertTrue(result.isPresent(), "Expected product to be found in MongoDB");
        assertEquals(productId.value(), result.get().id().value());
        assertEquals(businessId.value(), result.get().businessIdVO().value());

        // Assert: 2025 Domain Invariants
        ProductAggregate retrieved = result.get();
        assertEquals(1, retrieved.version().num(), "Business version should be 1");
        assertFalse(retrieved.isDeleted(), "Product should not be soft-deleted");

        // Assert: Features and isUnique flag
        VariantEntity variant = retrieved.variants().iterator().next();
        assertFalse(variant.getFeatures().isEmpty(), "Variant should contain features");

        boolean hasUniqueFeature = variant.getFeatures().stream()
                .anyMatch(FeatureAbstractClass::isUnique);
        assertTrue(hasUniqueFeature, "At least one feature should be marked as unique per 2025 data");
    }

    private ProductAggregate createValidProductAggregateWithFeatures() {
        // Create features using 2025 constructor signature (including isUnique)
        FeatureBasicEntity colorFeature = new FeatureBasicEntity(
                FeatureIdVO.generate(),
                new NameVO("Color"),
                new DescriptionVO("Midnight Blue"),
                new LabelVO("Color"),
                true // isUnique
        );

        FeatureBasicEntity materialFeature = new FeatureBasicEntity(
                FeatureIdVO.generate(),
                new NameVO("Material"),
                new DescriptionVO("Organic Cotton"),
                new LabelVO("Material"),
                false // NOT unique
        );

        Set<FeatureAbstractClass> features = Set.of(colorFeature, materialFeature);

        // Create initial variant with 2025 precision requirements
        VariantEntity variant = new VariantEntity(
                VariantIdVO.generate(),
                new SkuVO("SKU-2025-001"),
                new PriceVO(new BigDecimal("49.99"), 2, Currency.getInstance("USD")),
                new PriceVO(new BigDecimal("44.99"), 2, Currency.getInstance("USD")),
                features,
                new CareInstructionVO("* Dry clean only"),
                new WeightVO(new BigDecimal("0.350"), WeightUnitEnums.KILOGRAM),
                VariantStatusEnums.ACTIVE
        );

        GalleryVO gallery = new GalleryVO(List.of(new ImageUrlVO("https://cdn.example.com/p1.jpg")));

        return new ProductAggregate(
                productId,
                businessId,
                new CategoryVO("Apparel"),
                new DescriptionVO("Sample product for 2025 persistence testing"),
                gallery,
                Set.of(variant),
                new VersionVO(1), // Business Context Version
                false // isDeleted
        );
    }
}