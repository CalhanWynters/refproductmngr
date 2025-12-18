package com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.VariantDTO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.FeatureBasicEntity;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.FeatureIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.LabelVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.NameVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VariantMapperTest {

    @Test
    void testToDTO() {
        VariantEntity variant = new VariantEntity(
                VariantIdVO.fromString("123e4567-e89b-12d3-a456-426614174000"),
                new SkuVO("SKU123"),
                new PriceVO(new BigDecimal("99.99"), 2, Currency.getInstance("USD")),
                new PriceVO(new BigDecimal("89.99"), 2, Currency.getInstance("USD")),
                Set.of(new FeatureBasicEntity(
                        FeatureIdVO.fromString("123e4567-e89b-12d3-a456-426614174001"),
                        new NameVO("Color"),
                        new DescriptionVO("Blue Color"),
                        new LabelVO("Color Label")
                )),
                new CareInstructionVO("* Handle with care"), // Updated
                new WeightVO(new BigDecimal("1.5"), WeightUnitEnums.KILOGRAM),
                VariantStatusEnums.ACTIVE
        );

        VariantDTO dto = VariantMapper.toDTO(variant);

        assertNotNull(dto);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", dto.id());
        assertEquals("SKU123", dto.sku());
        assertEquals(new BigDecimal("99.99"), dto.basePrice());
        assertEquals(new BigDecimal("89.99"), dto.currentPrice());
        assertEquals("USD", dto.currencyCode());
        assertEquals("* Handle with care", dto.careInstructions());
        assertEquals(new BigDecimal("1.5"), dto.weightValue());
        assertEquals("KILOGRAM", dto.weightUnit());
        assertEquals("ACTIVE", dto.status());
        assertEquals(1, dto.features().size()); // Assuming only one feature added
    }
}
