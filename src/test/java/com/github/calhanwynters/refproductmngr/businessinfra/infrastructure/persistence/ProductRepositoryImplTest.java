package com.github.calhanwynters.refproductmngr.businessinfra.infrastructure.persistence;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductRepositoryImplTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    private ProductIdVO productId;
    private BusinessIdVO businessId;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        // satisfy UUID regex
        productId = ProductIdVO.generate();
        // satisfy [A-Z0-9-]+ regex
        businessId = new BusinessIdVO("BIZ-456");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Should return a product when it exists in the database")
    void testFindProductByProductIdAndBusinessId_Success() {
        // Arrange
        ProductAggregate product = createValidProductAggregate();

        when(mongoTemplate.query(ProductAggregate.class)
                .matching(any(Query.class))
                .oneValue())
                .thenReturn(product);

        // Act
        Optional<ProductAggregate> result = productRepository.findProductByProductIdAndBusinessId(productId, businessId);

        // Assert
        assertTrue(result.isPresent(), "Expected product to be found");
        assertEquals(product, result.get());
    }

    @Test
    @DisplayName("Should return empty Optional when no product matches the criteria")
    void testFindProductByProductIdAndBusinessId_NotFound() {
        // Arrange
        when(mongoTemplate.query(ProductAggregate.class)
                .matching(any(Query.class))
                .oneValue())
                .thenReturn(null);

        // Act
        Optional<ProductAggregate> result = productRepository.findProductByProductIdAndBusinessId(productId, businessId);

        // Assert
        assertFalse(result.isPresent(), "Result should be empty when no record is found");
    }

    @Test
    @DisplayName("Should wrap database errors in ProductRepositoryException")
    void testFindProductByProductIdAndBusinessId_Exception() {
        // Arrange
        when(mongoTemplate.query(ProductAggregate.class))
                .thenThrow(new RuntimeException("Database connection failure"));

        // Act & Assert
        ProductRepositoryException exception = assertThrows(ProductRepositoryException.class, () ->
                productRepository.findProductByProductIdAndBusinessId(productId, businessId)
        );

        assertEquals("Storage access failure", exception.getMessage());
    }

    /**
     * Helper to build a valid Aggregate satisfying all Domain invariants:
     * - Valid UUID ProductId
     * - Uppercase BusinessId
     * - HTTPS ImageUrl
     * - Gallery with >= 1 image
     */
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
}
