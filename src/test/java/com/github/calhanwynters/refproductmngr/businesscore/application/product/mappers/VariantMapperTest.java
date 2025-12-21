package com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.VariantDTO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.FeatureBasicEntity;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.FeatureIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.LabelVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.NameVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VariantMapperTest {

    @Test
    @DisplayName("Should map VariantEntity to VariantDTO with polymorphic features and isUnique flag")
    void testToDTO() {
        // Arrange
        // Note: isUnique is now a required parameter for Feature entities in 2025
        FeatureBasicEntity colorFeature = new FeatureBasicEntity(
                FeatureIdVO.fromString("123e4567-e89b-12d3-a456-426614174001"),
                new NameVO("Color"),
                new DescriptionVO("Blue Color"),
                new LabelVO("Color Label"),
                true // isUnique flag
        );

        VariantEntity variant = new VariantEntity(
                VariantIdVO.fromString("123e4567-e89b-12d3-a456-426614174000"),
                new SkuVO("SKU123"),
                new PriceVO(new BigDecimal("99.99"), 2, Currency.getInstance("USD")),
                new PriceVO(new BigDecimal("89.99"), 2, Currency.getInstance("USD")),
                Set.of(colorFeature),
                new CareInstructionVO("* Handle with care"),
                new WeightVO(new BigDecimal("1.5"), WeightUnitEnums.KILOGRAM),
                VariantStatusEnums.ACTIVE
        );

        // Act
        VariantDTO dto = VariantMapper.toDTO(variant);

        // Assert
        assertNotNull(dto, "The resulting DTO should not be null.");

        // Identity and Commercials
        assertEquals("123e4567-e89b-12d3-a456-426614174000", dto.id());
        assertEquals("SKU123", dto.sku());
        assertEquals(0, new BigDecimal("99.99").compareTo(dto.basePrice()), "Base price should match.");
        assertEquals(0, new BigDecimal("89.99").compareTo(dto.currentPrice()), "Current price should match.");
        assertEquals("USD", dto.currencyCode());

        // Physicals
        assertEquals("* Handle with care", dto.careInstructions());
        assertEquals(0, new BigDecimal("1.5").compareTo(dto.weightValue()), "Weight value should match.");
        assertEquals("KILOGRAM", dto.weightUnit());
        assertEquals("ACTIVE", dto.status());

        // Features (Polymorphic Delegation to FeatureMapper)
        assertEquals(1, dto.features().size(), "DTO should contain exactly one feature.");
        var mappedFeature = dto.features().iterator().next();
        assertEquals("Color", mappedFeature.name());
        assertTrue(mappedFeature.isUnique(), "The isUnique flag should have been mapped correctly.");
    }
}
