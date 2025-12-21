package com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FeatureMapper 2025 Bidirectional Mapping Tests")
class FeatureMapperTest {

    private static final String VALID_UUID = "123e4567-e89b-12d3-a456-426614174000";

    @Test
    @DisplayName("Should map BasicFeatureEntity to BasicFeatureDTO including isUnique")
    void testToDTO_basicFeature() {
        // Arrange
        FeatureBasicEntity entity = new FeatureBasicEntity(
                FeatureIdVO.fromString(VALID_UUID),
                new NameVO("Color"),
                new DescriptionVO("Blue Color"),
                new LabelVO("Color Label"),
                true // isUnique
        );

        // Act
        FeatureDTO dto = FeatureMapper.toDTO(entity);

        // Assert
        assertInstanceOf(BasicFeatureDTO.class, dto);
        BasicFeatureDTO basicDTO = (BasicFeatureDTO) dto;
        assertEquals(VALID_UUID, basicDTO.id());
        assertEquals("Color", basicDTO.name());
        assertTrue(basicDTO.isUnique());
    }

    @Test
    @DisplayName("Should map FixedPriceFeatureEntity to FixedPriceFeatureDTO")
    void testToDTO_fixedPriceFeature() {
        // Arrange
        FeatureFixedPriceEntity entity = new FeatureFixedPriceEntity(
                FeatureIdVO.fromString(VALID_UUID),
                new NameVO("Gift Wrap"),
                new DescriptionVO("Premium wrapping"),
                new LabelVO("Gift Wrap"),
                new BigDecimal("5.00"),
                false
        );

        // Act
        FeatureDTO dto = FeatureMapper.toDTO(entity);

        // Assert
        assertInstanceOf(FixedPriceFeatureDTO.class, dto);
        FixedPriceFeatureDTO fixedDTO = (FixedPriceFeatureDTO) dto;
        assertEquals(new BigDecimal("5.00"), fixedDTO.fixedPrice());
        assertFalse(fixedDTO.isUnique());
    }

    @Test
    @DisplayName("Should map ScalingPriceFeatureEntity to ScalingPriceFeatureDTO")
    void testToDTO_scalingPriceFeature() {
        // Arrange
        FeatureScalingPriceEntity entity = new FeatureScalingPriceEntity(
                FeatureIdVO.fromString(VALID_UUID),
                new NameVO("Fabric"),
                new DescriptionVO("Custom Length"),
                new LabelVO("Fabric Label"),
                new MeasurementUnitVO("METER"),
                new BigDecimal("10.00"),
                new BigDecimal("2.00"),
                5,
                true
        );

        // Act
        FeatureDTO dto = FeatureMapper.toDTO(entity);

        // Assert
        assertInstanceOf(ScalingPriceFeatureDTO.class, dto);
        ScalingPriceFeatureDTO scalingDTO = (ScalingPriceFeatureDTO) dto;
        assertEquals("METER", scalingDTO.unit());
        assertEquals(new BigDecimal("10.00"), scalingDTO.baseAmount());
        assertTrue(scalingDTO.isUnique());
    }

    @Test
    @DisplayName("Should map BasicFeatureDTO to FeatureBasicEntity")
    void testToEntity_basicFeatureDTO() {
        // Arrange
        BasicFeatureDTO dto = new BasicFeatureDTO(VALID_UUID, "Color", "Color Label", "Blue Color", true);

        // Act
        FeatureAbstractClass entity = FeatureMapper.toEntity(dto);

        // Assert
        assertInstanceOf(FeatureBasicEntity.class, entity);
        assertEquals(VALID_UUID, entity.getId().value());
        assertTrue(entity.isUnique());
    }

    @Test
    @DisplayName("Should map ScalingPriceFeatureDTO to FeatureScalingPriceEntity")
    void testToEntity_scalingPriceFeatureDTO() {
        // Arrange
        ScalingPriceFeatureDTO dto = new ScalingPriceFeatureDTO(
                VALID_UUID, "Fabric", "Fabric Label", "Custom Length", true,
                "METER", new BigDecimal("10.00"), new BigDecimal("2.00"), 5
        );

        // Act
        FeatureAbstractClass entity = FeatureMapper.toEntity(dto);

        // Assert
        assertInstanceOf(FeatureScalingPriceEntity.class, entity);
        FeatureScalingPriceEntity scaling = (FeatureScalingPriceEntity) entity;
        assertEquals("METER", scaling.getMeasurementUnit().unit());
        assertEquals(5, scaling.getMaxQuantity());
        assertTrue(scaling.isUnique());
    }

    @Test
    @DisplayName("Should generate new ID when DTO id is null")
    void testToEntity_NullId_GeneratesNewId() {
        // Arrange
        BasicFeatureDTO dto = new BasicFeatureDTO(null, "Color", "Color Label", "The sky is very Blue", false);

        // Act
        FeatureAbstractClass entity = FeatureMapper.toEntity(dto);

        // Assert
        assertNotNull(entity.getId());
        assertDoesNotThrow(() -> java.util.UUID.fromString(entity.getId().value()));
    }
}