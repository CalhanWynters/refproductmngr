package com.github.calhanwynters.refproductmngr.businessinfra.infrastructure.persistence;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.*;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

        return new ProductAggregate(
                productId,
                businessId,
                new CategoryVO("Electronics"),
                new DescriptionVO("Sample Description"),
                gallery,
                Collections.emptySet(),
                new VersionVO(1)
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
}