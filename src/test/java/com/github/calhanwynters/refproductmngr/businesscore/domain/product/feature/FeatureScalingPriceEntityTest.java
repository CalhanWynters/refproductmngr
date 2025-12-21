package com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FeatureScalingPriceEntityTest {

    private static final FeatureIdVO ID_VO = new FeatureIdVO("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
    private static final NameVO NAME_VO = new NameVO("Custom Fabric");
    private static final DescriptionVO DESC_VO = new DescriptionVO("Custom length of fabric for your needs.");
    private static final LabelVO LABEL_VO = new LabelVO("Custom Fabric Length");
    private static final MeasurementUnitVO MEASUREMENT_UNIT_VO = new MeasurementUnitVO("meters");
    private static final Boolean IS_UNIQUE = false;

    // Helper method updated for 2025 constructor signature
    private FeatureScalingPriceEntity createValidEntity() {
        return new FeatureScalingPriceEntity(
                ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                new BigDecimal("10.00"), new BigDecimal("2.50"), 100, IS_UNIQUE
        );
    }

    @Test
    @DisplayName("Should create entity successfully with valid parameters")
    void testCreateFeatureScalingPriceEntity_Success() {
        // Arrange & Act
        FeatureScalingPriceEntity entity = createValidEntity();

        // Assert
        assertNotNull(entity);
        assertEquals(MEASUREMENT_UNIT_VO, entity.getMeasurementUnit());
        assertEquals(IS_UNIQUE, entity.isUnique());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when base amount is null")
    void testCreateFeatureScalingPriceEntity_NullBaseAmount() {
        // Arrange
        BigDecimal nullBase = null;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureScalingPriceEntity(
                        ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                        nullBase, new BigDecimal("2.50"), 100, IS_UNIQUE
                )
        );

        assertEquals("Base amount must not be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when increment amount is null")
    void testCreateFeatureScalingPriceEntity_NullIncrementAmount() {
        // Arrange
        BigDecimal nullIncrement = null;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureScalingPriceEntity(
                        ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                        new BigDecimal("10.00"), nullIncrement, 100, IS_UNIQUE
                )
        );

        assertEquals("Increment amount must not be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when base amount is negative")
    void testCreateFeatureScalingPriceEntity_NegativeBaseAmount() {
        // Arrange
        BigDecimal negativeBase = new BigDecimal("-10.00");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureScalingPriceEntity(
                        ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                        negativeBase, new BigDecimal("2.50"), 100, IS_UNIQUE
                )
        );

        assertEquals("Amounts and max quantity must be non-negative.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when max quantity is negative")
    void testCreateFeatureScalingPriceEntity_NegativeMaxQuantity() {
        // Arrange
        int negativeMax = -1;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureScalingPriceEntity(
                        ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                        new BigDecimal("10.00"), new BigDecimal("2.50"), negativeMax, IS_UNIQUE
                )
        );

        assertEquals("Amounts and max quantity must be non-negative.", exception.getMessage());
    }

    @Test
    @DisplayName("Should calculate total price correctly for valid quantity")
    void testCalculateTotalPrice_ValidQuantity() {
        // Arrange
        FeatureScalingPriceEntity feature = createValidEntity();

        // Act
        BigDecimal totalPrice = feature.calculateTotalPrice(5);

        // Assert
        // Logic: 10.00 base + (5 * 2.50) = 22.50
        assertEquals(0, new BigDecimal("22.50").compareTo(totalPrice), "Calculation should result in 22.50");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for quantity greater than max quantity")
    void testCalculateTotalPrice_QuantityGreaterThanMax() {
        // Arrange
        FeatureScalingPriceEntity feature = createValidEntity();
        int invalidQty = 101;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                feature.calculateTotalPrice(invalidQty)
        );

        assertEquals("Quantity must be between 1 and 100, but was 101", exception.getMessage());
    }

    @Test
    @DisplayName("Should correctly override equals and hashCode")
    void testEqualsAndHashCode() {
        // Arrange
        FeatureScalingPriceEntity feature1 = createValidEntity();
        FeatureScalingPriceEntity feature2 = new FeatureScalingPriceEntity(
                ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                new BigDecimal("10.00"), new BigDecimal("2.50"), 100, IS_UNIQUE
        );

        // Act & Assert
        assertEquals(feature1, feature2, "Entities with identical values should be equal.");
        assertEquals(feature1.hashCode(), feature2.hashCode(), "Hash codes should match for identical entities.");
    }

    @Test
    @DisplayName("Should correctly implement toString")
    void testToString() {
        // Arrange
        FeatureScalingPriceEntity feature = createValidEntity();

        // Act
        String result = feature.toString();

        // Assert
        assertTrue(result.contains("id=" + ID_VO));
        assertTrue(result.contains("unit='meters'"));
        assertTrue(result.contains("base=10.00"));
    }
}
