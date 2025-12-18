package com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.BasicFeatureDTO;
import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.FeatureDTO;
import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.FixedPriceFeatureDTO;
import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.ScalingPriceFeatureDTO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.*;

/**
 * Central mapper for polymorphic Feature entities.
 * Implements bidirectional mapping between Domain Entities and Application DTOs.
 */
public final class FeatureMapper {

    private FeatureMapper() {} // Prevent instantiation

    /**
     * Maps Entity (Domain) -> DTO (Application)
     * Used for output: sending data to the UI/API.
     */
    public static FeatureDTO toDTO(FeatureAbstractClass entity) {
        if (entity == null) return null;

        // Extract shared domain properties once to keep the switch cases clean
        String id = entity.getId().value();
        String name = entity.getNameVO().value();
        String label = entity.getLabelVO().value();
        String desc = entity.getDescription() != null ? entity.getDescription().description() : null;

        return switch (entity) {
            case FeatureBasicEntity basic ->
                    new BasicFeatureDTO(id, name, desc, label);

            case FeatureFixedPriceEntity e ->
                    new FixedPriceFeatureDTO(id, name, desc, label, e.getFixedPrice());

            case FeatureScalingPriceEntity e ->
                    new ScalingPriceFeatureDTO(id, name, desc, label,
                            e.getMeasurementUnit().unit(),
                            e.getBaseAmount(),
                            e.getIncrementAmount(),
                            e.getMaxQuantity());

            default -> throw new IllegalArgumentException("Unknown feature category: " + entity.getClass());
        };

    }

    /**
     * Maps DTO (Application) -> Entity (Domain)
     * Used for input: creating or updating features.
     */
    public static FeatureAbstractClass toEntity(FeatureDTO dto) {
        if (dto == null) return null;

        // Convert the raw DTO string to a Domain Value Object
        DescriptionVO domainDesc = dto.description() != null ? new DescriptionVO(dto.description()) : null;

        return switch (dto) {
            case BasicFeatureDTO d -> new FeatureBasicEntity(
                    FeatureIdVO.fromString(d.id()),
                    new NameVO(d.name()),
                    domainDesc,
                    new LabelVO(d.label())
            );

            case FixedPriceFeatureDTO d -> new FeatureFixedPriceEntity(
                    FeatureIdVO.fromString(d.id()),
                    new NameVO(d.name()),
                    domainDesc,
                    new LabelVO(d.label()),
                    d.fixedPrice()
            );

            case ScalingPriceFeatureDTO d -> new FeatureScalingPriceEntity(
                    FeatureIdVO.fromString(d.id()),
                    new NameVO(d.name()),
                    domainDesc,
                    new LabelVO(d.label()),
                    new MeasurementUnitVO(d.measurementUnit()),
                    d.baseAmount(),
                    d.incrementAmount(),
                    d.maxQuantity()
            );
        };
    }
}
