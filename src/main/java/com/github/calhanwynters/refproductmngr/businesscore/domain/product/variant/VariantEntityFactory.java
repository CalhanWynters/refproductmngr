package com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.FeatureAbstractClass;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

class VariantEntityFactory {
    public static VariantEntity createDraft(
            BigDecimal basePriceValue, // Takes raw value
            WeightVO weight,
            CareInstructionVO careInstructions,
            Set<FeatureAbstractClass> features) {
        VariantIdVO generatedId = VariantIdVO.generate();
        SkuVO generatedSku = new SkuVO(String.format("VARIANT-%s", UUID.randomUUID().toString().substring(0, 8).toUpperCase()));

        // Convert raw BigDecimal to a default PriceVO (USD, precision 2)
        PriceVO defaultPrice = new PriceVO(basePriceValue);

        return new VariantEntity(generatedId, generatedSku, defaultPrice, defaultPrice, features, careInstructions, weight, VariantStatusEnums.DRAFT);
    }
}
