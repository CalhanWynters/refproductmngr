package com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.BasicFeatureDTO;
import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.FeatureDTO;
import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.FixedPriceFeatureDTO;
import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.ScalingPriceFeatureDTO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FeatureMapperTest {

    @Test
    void testToDTO_basicFeature() {
        FeatureBasicEntity featureEntity = new FeatureBasicEntity(
                FeatureIdVO.fromString("123e4567-e89b-12d3-a456-426614174000"), // Valid UUID
                new NameVO("Color"),
                new DescriptionVO("Blue Color"),
                new LabelVO("Color Label")
        );

        FeatureDTO dto = FeatureMapper.toDTO(featureEntity);

        assertNotNull(dto);
        assertInstanceOf(BasicFeatureDTO.class, dto);
        BasicFeatureDTO basicDTO = (BasicFeatureDTO) dto;
        assertEquals("123e4567-e89b-12d3-a456-426614174000", basicDTO.id());
        assertEquals("Color", basicDTO.name());
        assertEquals("Blue Color", basicDTO.description());
        assertEquals("Color Label", basicDTO.label());
    }

    @Test
    void testToDTO_fixedPriceFeature() {
        FeatureFixedPriceEntity featureEntity = new FeatureFixedPriceEntity(
                FeatureIdVO.fromString("123e4567-e89b-12d3-a456-426614174001"), // Valid UUID
                new NameVO("Gift Wrap"),
                new DescriptionVO("Wrap your product"),
                new LabelVO("Gift Wrap"),
                new BigDecimal("5.00")
        );

        FeatureDTO dto = FeatureMapper.toDTO(featureEntity);

        assertNotNull(dto);
        assertInstanceOf(FixedPriceFeatureDTO.class, dto);
        FixedPriceFeatureDTO fixedPriceDTO = (FixedPriceFeatureDTO) dto;
        assertEquals("123e4567-e89b-12d3-a456-426614174001", fixedPriceDTO.id());
        assertEquals("Gift Wrap", fixedPriceDTO.name());
        assertEquals("Wrap your product", fixedPriceDTO.description());
        assertEquals("Gift Wrap", fixedPriceDTO.label());
        assertEquals(new BigDecimal("5.00"), fixedPriceDTO.fixedPrice());
    }

    @Test
    void testToDTO_scalingPriceFeature() {
        FeatureScalingPriceEntity featureEntity = new FeatureScalingPriceEntity(
                FeatureIdVO.fromString("123e4567-e89b-12d3-a456-426614174002"), // Valid UUID
                new NameVO("Fabric"),
                new DescriptionVO("Custom Length"),
                new LabelVO("Fabric Label"),
                new MeasurementUnitVO("METER"),
                new BigDecimal("10.00"),
                new BigDecimal("2.00"),
                5
        );

        FeatureDTO dto = FeatureMapper.toDTO(featureEntity);

        assertNotNull(dto);
        assertInstanceOf(ScalingPriceFeatureDTO.class, dto);
        ScalingPriceFeatureDTO scalingPriceDTO = (ScalingPriceFeatureDTO) dto;
        assertEquals("123e4567-e89b-12d3-a456-426614174002", scalingPriceDTO.id());
        assertEquals("Fabric", scalingPriceDTO.name());
        assertEquals("Custom Length", scalingPriceDTO.description());
        assertEquals("Fabric Label", scalingPriceDTO.label());
        assertEquals("METER", scalingPriceDTO.measurementUnit());
        assertEquals(new BigDecimal("10.00"), scalingPriceDTO.baseAmount());
        assertEquals(new BigDecimal("2.00"), scalingPriceDTO.incrementAmount());
        assertEquals(5, scalingPriceDTO.maxQuantity());
    }

    @Test
    void testToEntity_basicFeatureDTO() {
        BasicFeatureDTO dto = new BasicFeatureDTO(
                "123e4567-e89b-12d3-a456-426614174003", // Valid UUID
                "Color",
                "Blue Color",
                "Color Label"
        );
        FeatureAbstractClass entity = FeatureMapper.toEntity(dto);

        assertNotNull(entity);
        assertInstanceOf(FeatureBasicEntity.class, entity);
        FeatureBasicEntity basicEntity = (FeatureBasicEntity) entity;
        assertEquals("123e4567-e89b-12d3-a456-426614174003", basicEntity.getId().value());
        assertEquals("Color", basicEntity.getNameVO().value());
        assertEquals("Blue Color", basicEntity.getDescription().description());
        assertEquals("Color Label", basicEntity.getLabelVO().value());
    }

    @Test
    void testToEntity_fixedPriceFeatureDTO() {
        FixedPriceFeatureDTO dto = new FixedPriceFeatureDTO(
                "123e4567-e89b-12d3-a456-426614174004", // Valid UUID
                "Gift Wrap",
                "Wrap your product",
                "Gift Wrap",
                new BigDecimal("5.00")
        );

        FeatureAbstractClass entity = FeatureMapper.toEntity(dto);

        assertNotNull(entity);
        assertInstanceOf(FeatureFixedPriceEntity.class, entity);
        FeatureFixedPriceEntity fixedPriceEntity = (FeatureFixedPriceEntity) entity;
        assertEquals("123e4567-e89b-12d3-a456-426614174004", fixedPriceEntity.getId().value());
        assertEquals("Gift Wrap", fixedPriceEntity.getNameVO().value());
        assertEquals("Wrap your product", fixedPriceEntity.getDescription().description());
        assertEquals("Gift Wrap", fixedPriceEntity.getLabelVO().value());
        assertEquals(new BigDecimal("5.00"), fixedPriceEntity.getFixedPrice());
    }

    @Test
    void testToEntity_scalingPriceFeatureDTO() {
        ScalingPriceFeatureDTO dto = new ScalingPriceFeatureDTO(
                "123e4567-e89b-12d3-a456-426614174005", // Valid UUID
                "Fabric",
                "Custom Length",
                "Fabric Label",
                new MeasurementUnitVO("METER").unit(),
                new BigDecimal("10.00"),
                new BigDecimal("2.00"),
                5
        );

        FeatureAbstractClass entity = FeatureMapper.toEntity(dto);

        assertNotNull(entity);
        assertInstanceOf(FeatureScalingPriceEntity.class, entity);
        FeatureScalingPriceEntity scalingPriceEntity = (FeatureScalingPriceEntity) entity;
        assertEquals("123e4567-e89b-12d3-a456-426614174005", scalingPriceEntity.getId().value());
        assertEquals("Fabric", scalingPriceEntity.getNameVO().value());
        assertEquals("Custom Length", scalingPriceEntity.getDescription().description());
        assertEquals("Fabric Label", scalingPriceEntity.getLabelVO().value());
        assertEquals("METER", scalingPriceEntity.getMeasurementUnit().unit());
        assertEquals(new BigDecimal("10.00"), scalingPriceEntity.getBaseAmount());
        assertEquals(new BigDecimal("2.00"), scalingPriceEntity.getIncrementAmount());
        assertEquals(5, scalingPriceEntity.getMaxQuantity());
    }

    // Additional tests for FixedPriceFeatureDTO and ScalingPriceFeatureDTO can be added similarly
}
