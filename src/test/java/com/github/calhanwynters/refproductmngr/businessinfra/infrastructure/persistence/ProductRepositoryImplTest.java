package com.github.calhanwynters.refproductmngr.businessinfra.infrastructure.persistence;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
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

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import static org.assertj.core.api.Assertions.assertThat;


// Spring Data MongoDB Fluent API Interfaces
import org.springframework.data.mongodb.core.ExecutableFindOperation.ExecutableFind;
import org.springframework.data.mongodb.core.ExecutableFindOperation.TerminatingFind;

// Core MongoDB Query and Mockito


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
        productId = ProductIdVO.generate();
        businessId = new BusinessIdVO("BIZ-456");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Should return product when IDs match existing database record")
    void testFindProductByProductIdAndBusinessId_Success() {
        // Arrange
        ProductAggregate expectedProduct = createValidProductAggregate();
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        // Using Mockito's type inference to avoid unchecked assignment warnings
        ExecutableFind<ProductAggregate> executableFind = mock();
        TerminatingFind<ProductAggregate> terminatingFind = mock();

        when(mongoTemplate.query(ProductAggregate.class)).thenReturn(executableFind);
        when(executableFind.matching(queryCaptor.capture())).thenReturn(terminatingFind);
        when(terminatingFind.oneValue()).thenReturn(expectedProduct);

        // Act
        var result = productRepository.findProductByProductIdAndBusinessId(productId, businessId);

        // Assert
        assertThat(result).isPresent().contains(expectedProduct);
    }





    @Test
    @DisplayName("Should return empty Optional when no product matches the criteria")
    void testFindProductByProductIdAndBusinessId_NotFound() {
        when(mongoTemplate.query(ProductAggregate.class)
                .matching(any(Query.class))
                .oneValue())
                .thenReturn(null);

        Optional<ProductAggregate> result = productRepository.findProductByProductIdAndBusinessId(productId, businessId);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should wrap database errors in ProductRepositoryException")
    void testFindProductByProductIdAndBusinessId_Exception() {
        when(mongoTemplate.query(ProductAggregate.class))
                .thenThrow(new RuntimeException("Database connection failure"));

        ProductRepositoryException exception = assertThrows(ProductRepositoryException.class, () ->
                productRepository.findProductByProductIdAndBusinessId(productId, businessId)
        );

        assertEquals("Storage access failure", exception.getMessage());
    }

    /**
     * Updated helper to satisfy Domain invariants:
     * - Must have at least one variant.
     * - Includes isDeleted boolean.
     */
    private ProductAggregate createValidProductAggregate() {
        ImageUrlVO image = new ImageUrlVO("https://cdn.example.com/item.jpg");
        GalleryVO gallery = new GalleryVO(List.of(image));

        // Create a valid variant to satisfy the ProductAggregate constructor requirement
        VariantEntity variant = new VariantEntity(
                VariantIdVO.generate(),
                new SkuVO("SKU-VALID-123"),
                new PriceVO(new BigDecimal("50.00"), 2, Currency.getInstance("USD")),
                new PriceVO(new BigDecimal("45.00"), 2, Currency.getInstance("USD")),
                Set.of(),
                new CareInstructionVO("* Machine wash cold"),
                new WeightVO(new BigDecimal("0.5"), WeightUnitEnums.KILOGRAM),
                VariantStatusEnums.ACTIVE
        );

        return new ProductAggregate(
                productId,
                businessId,
                new CategoryVO("Electronics"),
                new DescriptionVO("Sample Description"),
                gallery,
                Set.of(variant), // Non-empty set required
                new VersionVO(1),
                false // isDeleted flag
        );
    }
}
