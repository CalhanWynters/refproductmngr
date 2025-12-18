package com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.ProductDTO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
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

        // Setup the VariantEntity
        PriceVO basePrice = new PriceVO(new BigDecimal("100.00"), 2, Currency.getInstance("USD"));
        PriceVO currentPrice = new PriceVO(new BigDecimal("90.00"), 2, Currency.getInstance("USD"));
        VariantEntity variant = new VariantEntity(
                VariantIdVO.generate(),  // Generate a valid UUID
                new SkuVO("SKU-123"),
                basePrice,
                currentPrice,
                Collections.emptySet(), // Assuming no features for simplicity
                new CareInstructionVO("* Follow these care instructions:"), // Ensure it starts with a bullet point
                new WeightVO(new BigDecimal("1.5"), WeightUnitEnums.KILOGRAM),
                VariantStatusEnums.ACTIVE
        );

        // Setup the main ProductAggregate
        ProductAggregate aggregate = new ProductAggregate(
                new ProductIdVO(productId),
                new BusinessIdVO(businessId),
                new CategoryVO(category),
                new DescriptionVO(description),
                new GalleryVO(Collections.singletonList(new ImageUrlVO(imageUrl))),
                Collections.singleton(variant),
                new VersionVO(1)
        );

        // Act
        ProductDTO result = ProductMapper.toDTO(aggregate);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.id());
        assertEquals(businessId, result.businessId());
        assertEquals(category, result.category());
        assertEquals(description, result.description());
        assertEquals(1, result.imageUrls().size());
        assertEquals(imageUrl, result.imageUrls().get(0));  // Access the first image URL
        assertEquals(1, result.variants().size());
        assertEquals("SKU-123", result.variants().iterator().next().sku());
        assertEquals(1, result.version());
        assertTrue(result.isPublishable()); // Assuming it meets the publishable criteria
    }

    @Test
    void testToDTO_NullAggregate() {
        // Act
        ProductDTO result = ProductMapper.toDTO(null); // Pass null to the method

        // Assert
        assertNull(result);  // Assert that the result is null
    }
}
