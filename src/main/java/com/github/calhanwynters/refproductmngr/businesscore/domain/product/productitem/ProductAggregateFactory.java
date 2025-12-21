package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

/**
 * Full-Service Factory for ProductAggregate in 2025.
 * Coordinates creation across the deep graph: Features -> Variants -> Product.
 */
public class ProductAggregateFactory {

    // --- FEATURE CREATION ---

    public static FeatureBasicEntity createBasicFeature(
            NameVO name, LabelVO label, DescriptionVO desc, boolean isUnique) {
        return new FeatureBasicEntity(FeatureIdVO.generate(), name, desc, label, isUnique);
    }

    public static FeatureBasicEntity reconstructBasicFeature(FeatureIdVO id, NameVO n, LabelVO l, DescriptionVO d, boolean u) {
        return new FeatureBasicEntity(id, n, d, l, u);
    }

    public static FeatureFixedPriceEntity createFixedPriceFeature(
            NameVO name, LabelVO label, DescriptionVO desc, BigDecimal price, boolean isUnique) {
        return new FeatureFixedPriceEntity(FeatureIdVO.generate(), name, desc, label, price, isUnique);
    }

    public static FeatureFixedPriceEntity reconstructFixedPriceFeature(FeatureIdVO id, NameVO n, LabelVO l, DescriptionVO d, BigDecimal p, boolean u) {
        return new FeatureFixedPriceEntity(id, n, d, l, p, u);
    }


    public static FeatureScalingPriceEntity createScalingPriceFeature(
            NameVO name, LabelVO label, DescriptionVO desc, MeasurementUnitVO unit,
            BigDecimal base, BigDecimal increment, int max, boolean isUnique) {
        return new FeatureScalingPriceEntity(
                FeatureIdVO.generate(), name, desc, label, unit, base, increment, max, isUnique);
    }

    public static FeatureScalingPriceEntity reconstructScalingPriceFeature(FeatureIdVO id, NameVO n, LabelVO l, DescriptionVO d, MeasurementUnitVO unit, BigDecimal b, BigDecimal i, int m, boolean u) {
        return new FeatureScalingPriceEntity(id, n, d, l, unit, b, i, m, u);
    }

    // --- VARIANT CREATION ---

    // Inside ProductAggregateFactory.java

    // 1. The Strict Factory (What you have - Keep it!)
    /**
     * DOMAIN ENTRY: Creates a Variant from primitive data.
     * The Set<FeatureAbstractClass> parameter is mandatory to resolve the method signature.
     */
    public static VariantEntity createVariant(
            String sku,
            BigDecimal base,
            BigDecimal current,
            String currency,
            BigDecimal weightValue,   // Changed from double to BigDecimal
            String unit,
            String care,
            String status,
            Set<FeatureAbstractClass> features) {

        return createVariant(
                new SkuVO(sku),
                new PriceVO(base, 2, Currency.getInstance(currency)),
                new PriceVO(current, 2, Currency.getInstance(currency)),
                features != null ? features : new HashSet<>(),
                new CareInstructionVO(care),
                new WeightVO(weightValue, WeightUnitEnums.valueOf(unit)), // Matches WeightVO(BigDecimal, unit)
                VariantStatusEnums.valueOf(status)
        );
    }


    /**
     * STRICT FACTORY: The core assembly method using Value Objects.
     */
    public static VariantEntity createVariant(
            SkuVO sku,
            PriceVO basePrice,
            PriceVO currentPrice,
            Set<FeatureAbstractClass> features,
            CareInstructionVO care,
            WeightVO weight,
            VariantStatusEnums status) {

        return new VariantEntity(
                VariantIdVO.generate(),
                sku,
                basePrice,
                currentPrice,
                features != null ? features : Set.of(),
                care,
                weight,
                status
        );
    }

    // --- AGGREGATE CREATION ---

    /**
     * Creates a NEW ProductAggregate.
     * Enforces the mandatory initial variant invariant.
     */
    public static ProductAggregate create(
            BusinessIdVO businessIdVO,
            CategoryVO category,
            DescriptionVO description,
            GalleryVO gallery,
            Set<VariantEntity> initialVariants
    ) {
        if (initialVariants == null || initialVariants.isEmpty()) {
            throw new IllegalArgumentException("A product must have at least one variant.");
        }

        return new ProductAggregate(
                ProductIdVO.generate(),
                businessIdVO,
                category,
                description,
                gallery,
                initialVariants,
                new VersionVO(1), // Starting business version, NOT a DB lock
                false
        );
    }

    /**
     * Reconstructs an existing ProductAggregate (Bypasses ID generation and default versioning).
     */
    public static ProductAggregate reconstruct(
            ProductIdVO id, BusinessIdVO businessId, CategoryVO category,
            DescriptionVO desc, GalleryVO gallery, Set<VariantEntity> variants,
            VersionVO version, boolean isDeleted) {
        return new ProductAggregate(id, businessId, category, desc, gallery, variants, version, isDeleted);
    }


}
