package com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregateFactory;

/**
 * 2025 Central mapper for polymorphic Feature entities.
 * Utilizes ProductAggregateFactory to ensure domain invariants are enforced.
 */
public final class FeatureMapper {

    private FeatureMapper() {}

    /**
     * Maps Entity (Domain) -> DTO (Application)
     * Includes the 2025 isUnique property.
     */
    public static FeatureDTO toDTO(FeatureAbstractClass entity) {
        if (entity == null) return null;

        String id = entity.getId().value();
        String name = entity.getNameVO().value();
        String label = entity.getLabelVO().value();
        String desc = entity.getDescription() != null ? entity.getDescription().text() : null;
        boolean isUnique = entity.isUnique();

        return switch (entity) {
            case FeatureBasicEntity ignored ->
                    new BasicFeatureDTO(id, name, label, desc, isUnique);

            case FeatureFixedPriceEntity e ->
                    new FixedPriceFeatureDTO(id, name, label, desc, isUnique, e.getFixedPrice());

            case FeatureScalingPriceEntity e ->
                    new ScalingPriceFeatureDTO(id, name, label, desc, isUnique,
                            e.getMeasurementUnit().unit(),
                            e.getBaseAmount(),
                            e.getIncrementAmount(),
                            e.getMaxQuantity());

            default -> throw new IllegalArgumentException("Unknown feature type: " + entity.getClass());
        };
    }

    /**
     * Maps DTO (Application) -> Entity (Domain)
     * Uses ProductAggregateFactory to enforce Always-Valid construction.
     */
    public static FeatureAbstractClass toEntity(FeatureDTO dto) {
        if (dto == null) return null;

        // 1. Map Shared Value Objects
        NameVO nameVO = new NameVO(dto.name());
        LabelVO labelVO = new LabelVO(dto.label());
        DescriptionVO descVO = dto.description() != null ? new DescriptionVO(dto.description()) : null;
        boolean isUnique = dto.isUnique();

        // 2. Identify Intent: Create vs. Update
        boolean isUpdate = dto.id() != null && !dto.id().isBlank();
        FeatureIdVO idVO = isUpdate ? FeatureIdVO.fromString(dto.id()) : null;

        // 3. Delegate to Factory via Switch Expression
        return switch (dto) {
            case BasicFeatureDTO d -> isUpdate
                    ? ProductAggregateFactory.reconstructBasicFeature(idVO, nameVO, labelVO, descVO, isUnique)
                    : ProductAggregateFactory.createBasicFeature(nameVO, labelVO, descVO, isUnique);

            case FixedPriceFeatureDTO d -> isUpdate
                    ? ProductAggregateFactory.reconstructFixedPriceFeature(idVO, nameVO, labelVO, descVO, d.fixedPrice(), isUnique)
                    : ProductAggregateFactory.createFixedPriceFeature(nameVO, labelVO, descVO, d.fixedPrice(), isUnique);

            case ScalingPriceFeatureDTO d -> {
                MeasurementUnitVO unitVO = new MeasurementUnitVO(d.unit());
                yield isUpdate
                        ? ProductAggregateFactory.reconstructScalingPriceFeature(idVO, nameVO, labelVO, descVO, unitVO, d.baseAmount(), d.incrementAmount(), d.maxQuantity(), isUnique)
                        : ProductAggregateFactory.createScalingPriceFeature(nameVO, labelVO, descVO, unitVO, d.baseAmount(), d.incrementAmount(), d.maxQuantity(), isUnique);
            }
        };
    }

}
