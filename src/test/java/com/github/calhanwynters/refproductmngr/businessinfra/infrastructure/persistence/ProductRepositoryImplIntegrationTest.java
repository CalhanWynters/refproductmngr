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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper; // Add this import
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers // Automatically manages container lifecycle
public class ProductRepositoryImplIntegrationTest {

    // Define the MongoDB container (shared across all tests in this class)
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.0");

    // Link the container's dynamic connection string to Spring Data
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    private ProductRepositoryImpl productRepository;
    private ProductIdVO productId;
    private BusinessIdVO businessId;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepositoryImpl(mongoTemplate);
        productId = ProductIdVO.generate();
        businessId = new BusinessIdVO("BIZ-456");
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(ProductAggregate.class);
    }

    @Test
    @DisplayName("Should persist and retrieve a product from the database using real MongoDB container")
    void testPersistAndRetrieveProduct() {
        // Arrange
        ProductAggregate product = createValidProductAggregate();

        // Act: Save directly via template
        mongoTemplate.save(product);

        // Act: Retrieve using repository implementation
        Optional<ProductAggregate> result = productRepository.findProductByProductIdAndBusinessId(productId, businessId);

        // Assert: Using record accessor methods id() and businessIdVO()
        assertTrue(result.isPresent(), "Expected product to be found");
        assertEquals(productId.value(), result.get().id().value());
        assertEquals(businessId.value(), result.get().businessIdVO().value());
    }

    private ProductAggregate createValidProductAggregate() {
        ImageUrlVO image = new ImageUrlVO("https://cdn.example.com/item.jpg");
        GalleryVO gallery = new GalleryVO(List.of(image));

        // Create at least one variant to satisfy domain validation
        VariantEntity defaultVariant = new VariantEntity(
                VariantIdVO.generate(),
                new SkuVO("SKU-DEFAULT"),
                new PriceVO(BigDecimal.TEN),
                new PriceVO(BigDecimal.TEN),
                Collections.emptySet(),
                new CareInstructionVO("* Please wash your hands."),
                new WeightVO(BigDecimal.ONE, WeightUnitEnums.KILOGRAM),
                VariantStatusEnums.ACTIVE
        );

        return new ProductAggregate(
                productId,
                businessId,
                new CategoryVO("Electronics"),
                new DescriptionVO("Sample Description"),
                gallery,
                Set.of(defaultVariant), // Pass the set containing the variant
                new VersionVO(1),
                false
        );
    }

    @Test
    @DisplayName("Should persist and retrieve a product from the database using real MongoDB container")
    void testPrintPersistAndRetrieveProduct() throws Exception {
        ProductAggregate product = createValidProductAggregate();
        mongoTemplate.save(product);

        Optional<ProductAggregate> result = productRepository.findProductByProductIdAndBusinessId(productId, businessId);

        // --- PRINT OUT DATA ---
        if (result.isPresent()) {
            ObjectMapper mapper = new ObjectMapper()
                    .enable(SerializationFeature.INDENT_OUTPUT); // Format with indentation

            String json = mapper.writeValueAsString(result.get());

            System.out.println("\n=== RETRIEVED PRODUCT DATA (2025-12-18) ===");
            System.out.println(json);
            System.out.println("===========================================\n");
        }
        // ----------------------

        assertTrue(result.isPresent());
        assertEquals(productId.value(), result.get().id().value());
    }

    @Test
    @DisplayName("Should persist and retrieve a product with features from the database")
    void testPersistAndRetrieveProductWithFeatures() {
        // Arrange
        ProductAggregate product = createValidProductAggregateWithFeatures();

        // Act: Save directly via template
        mongoTemplate.save(product);

        // Act: Retrieve using repository implementation
        Optional<ProductAggregate> result = productRepository.findProductByProductIdAndBusinessId(product.id(), product.businessIdVO());

        // Assert: Check if the product retrieved has the expected features
        assertTrue(result.isPresent(), "Expected product to be found");
        assertEquals(product.id().value(), result.get().id().value());
        assertEquals(product.businessIdVO().value(), result.get().businessIdVO().value());

        // Check if features are present in the first variant
        assertFalse(result.get().variants().isEmpty(), "Expected product to have at least one variant");
        Set<VariantEntity> variants = result.get().variants();
        assertTrue(variants.stream().anyMatch(v -> !v.getFeatures().isEmpty()), "Expected at least one variant to have features");
    }

    private ProductAggregate createValidProductAggregateWithFeatures() {
        // Create features using the extracted method
        Set<FeatureAbstractClass> features = createFeatureSet();

        // Create a variant with features
        VariantEntity variant = new VariantEntity(
                VariantIdVO.generate(),  // Generate a valid UUID for the variant ID
                new SkuVO("SKU-123"),
                new PriceVO(BigDecimal.valueOf(19.99)),
                new PriceVO(BigDecimal.valueOf(19.99)),
                features,
                new CareInstructionVO("* Hand wash only"),
                new WeightVO(BigDecimal.valueOf(0.5), WeightUnitEnums.KILOGRAM),
                VariantStatusEnums.ACTIVE
        );

        ImageUrlVO image = new ImageUrlVO("https://cdn.example.com/item.jpg");
        GalleryVO gallery = new GalleryVO(List.of(image));

        return new ProductAggregate(
                productId,
                businessId,
                new CategoryVO("Electronics"),
                new DescriptionVO("Sample Description"),
                gallery,
                Set.of(variant),
                new VersionVO(1),
                false
        );
    }


    // New method to create the features
    private Set<FeatureAbstractClass> createFeatureSet() {
        FeatureAbstractClass colorFeature = new FeatureBasicEntity(
                FeatureIdVO.generate(),  // Generate a valid UUID for color feature
                new NameVO("Color"),
                new DescriptionVO("The color of the product"),
                new LabelVO("Color Label")
        );

        FeatureAbstractClass sizeFeature = new FeatureBasicEntity(
                FeatureIdVO.generate(),  // Generate a valid UUID for size feature
                new NameVO("Size"),
                new DescriptionVO("The size of the product"),
                new LabelVO("Size Label")
        );

        return Set.of(colorFeature, sizeFeature);
    }

    @Test
    @DisplayName("Should print out retrieved product details from the database")
    void testPrintPersistAndRetrieveProductWithFeatures() {
        // Arrange
        ProductAggregate product = createValidProductAggregateWithFeatures();

        // Act: Save directly via template
        mongoTemplate.save(product);

        // Act: Retrieve using repository implementation
        Optional<ProductAggregate> result = productRepository.findProductByProductIdAndBusinessId(product.id(), product.businessIdVO());

        // --- Printout Data ---
        if (result.isPresent()) {
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            try {
                // Handle the checked exception using try-catch
                String json = objectMapper.writeValueAsString(result.get());
                System.out.println("\n=== RETRIEVED PRODUCT DATA ===");
                System.out.println(json);
                System.out.println("================================\n");
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                // In a test environment, you might just want to print the stack trace or fail the test explicitly
                e.printStackTrace();
                fail("Failed to serialize product to JSON for printing: " + e.getMessage());
            }
        } else {
            System.out.println("No product found.");
        }
        // ----------------------

        // Assert: Ensure the product was found
        assertTrue(result.isPresent(), "Expected product to be found");
        assertEquals(product.id().value(), result.get().id().value());
        assertEquals(product.businessIdVO().value(), result.get().businessIdVO().value());

        // Check if features are present in the first variant
        assertFalse(result.get().variants().isEmpty(), "Expected product to have at least one variant");
        Set<VariantEntity> variants = result.get().variants();
        assertTrue(variants.stream().anyMatch(v -> !v.getFeatures().isEmpty()), "Expected at least one variant to have features");
    }





}