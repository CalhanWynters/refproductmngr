package com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.ProductDTO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    void testToDTO() {
        // Arrange
        String productId = UUID.randomUUID().toString();
        String businessId = "BUSINESS-12345";
        String category = "Electronics";
        String description = "A high-quality product.";
        String imageUrl = "https://example.com/image.jpg";

        VariantEntity variant = createSampleVariant();

        ProductAggregate aggregate = new ProductAggregate(
                new ProductIdVO(productId),
                new BusinessIdVO(businessId),
                new CategoryVO(category),
                new DescriptionVO(description),
                new GalleryVO(List.of(new ImageUrlVO(imageUrl))),
                Collections.singleton(variant),
                new VersionVO(1),
                false
        );

        // Act
        ProductDTO result = ProductMapper.toDTO(aggregate);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.id());
        assertFalse(result.isDeleted());
        assertTrue(result.isPublishable());
    }

    @Test
    void testToDTO_SoftDeletedProduct() {
        // Arrange
        VariantEntity variant = createSampleVariant();
        ProductAggregate aggregate = new ProductAggregate(
                new ProductIdVO(UUID.randomUUID().toString()),
                new BusinessIdVO("B-1"), new CategoryVO("Cat"), new DescriptionVO("This is a valid text."),
                new GalleryVO(List.of(new ImageUrlVO("https://example.com/img.jpg"))),
                Collections.singleton(variant), new VersionVO(1),
                true // isDeleted is TRUE
        );

        // Act
        ProductDTO result = ProductMapper.toDTO(aggregate);

        // Assert
        assertTrue(result.isDeleted());
        assertFalse(result.isPublishable(), "Deleted products should not be publishable");
    }

    private VariantEntity createSampleVariant() {
        // Provide valid PriceVO objects to avoid NullPointerException
        PriceVO price = new PriceVO(new BigDecimal("100.00"), 2, Currency.getInstance("USD"));

        return new VariantEntity(
                VariantIdVO.generate(),
                new SkuVO("SKU-123"),
                price, // basePrice
                price, // currentPrice
                Collections.emptySet(),
                new CareInstructionVO("* Care instruction"),
                new WeightVO(BigDecimal.ONE, WeightUnitEnums.KILOGRAM),
                VariantStatusEnums.ACTIVE
        );
    }
}
