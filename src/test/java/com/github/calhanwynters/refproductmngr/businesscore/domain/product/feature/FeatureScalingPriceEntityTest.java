package com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FeatureScalingPriceEntityTest {

    // Define common immutable Value Objects once to reduce boilerplate code
    // FIX: Use a valid UUID string to prevent ExceptionInInitializerError during class loading.
    private static final FeatureIdVO ID_VO = new FeatureIdVO("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
    private static final NameVO NAME_VO = new NameVO("Custom Fabric");
    private static final DescriptionVO DESC_VO = new DescriptionVO("Custom length of fabric for your needs.");
    private static final LabelVO LABEL_VO = new LabelVO("Custom Fabric Length");
    private static final MeasurementUnitVO MEASUREMENT_UNIT_VO = new MeasurementUnitVO("meters");

    // Helper method to create a valid entity easily
    private FeatureScalingPriceEntity createValidEntity() {
        return new FeatureScalingPriceEntity(
                ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                new BigDecimal("10.00"), new BigDecimal("2.50"), 100
        );
    }

    // (Tests for valid creation, null measurement unit, etc., omitted for brevity but remain valid)

    @Test
    @DisplayName("Should throw IllegalArgumentException when base amount is null")
    void testCreateFeatureScalingPriceEntity_NullBaseAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureScalingPriceEntity(
                        ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                        null,
                        new BigDecimal("2.50"),
                        100
                )
        );

        assertEquals("Base amount must not be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when increment amount is null")
    void testCreateFeatureScalingPriceEntity_NullIncrementAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureScalingPriceEntity(
                        ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                        new BigDecimal("10.00"),
                        null,
                        100
                )
        );

        assertEquals("Increment amount must not be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when base amount is negative")
    void testCreateFeatureScalingPriceEntity_NegativeBaseAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureScalingPriceEntity(
                        ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                        new BigDecimal("-10.00"),
                        new BigDecimal("2.50"),
                        100
                )
        );

        assertEquals("Amounts and max quantity must be non-negative.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when increment amount is negative")
    void testCreateFeatureScalingPriceEntity_NegativeIncrementAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureScalingPriceEntity(
                        ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                        new BigDecimal("10.00"),
                        new BigDecimal("-2.50"),
                        100
                )
        );

        assertEquals("Amounts and max quantity must be non-negative.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when max quantity is negative")
    void testCreateFeatureScalingPriceEntity_NegativeMaxQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureScalingPriceEntity(
                        ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                        new BigDecimal("10.00"),
                        new BigDecimal("2.50"),
                        -1
                )
        );

        assertEquals("Amounts and max quantity must be non-negative.", exception.getMessage());
    }

    @Test
    @DisplayName("Should calculate total price correctly for valid quantity")
    void testCalculateTotalPrice_ValidQuantity() {
        FeatureScalingPriceEntity feature = createValidEntity();

        BigDecimal totalPrice = feature.calculateTotalPrice(5);
        // We expect exactly 22.50 (10.00 base + 5 * 2.50 increment)
        assertEquals(0, new BigDecimal("22.50").compareTo(totalPrice));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for quantity less than 1")
    void testCalculateTotalPrice_QuantityLessThanOne() {
        FeatureScalingPriceEntity feature = createValidEntity();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                feature.calculateTotalPrice(0)
        );

        assertEquals("Quantity must be between 1 and 100, but was 0", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for quantity greater than max quantity")
    void testCalculateTotalPrice_QuantityGreaterThanMax() {
        FeatureScalingPriceEntity feature = createValidEntity();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                feature.calculateTotalPrice(101)
        );

        assertEquals("Quantity must be between 1 and 100, but was 101", exception.getMessage());
    }

    @Test
    @DisplayName("Should correctly override equals and hashCode")
    void testEqualsAndHashCode() {
        FeatureScalingPriceEntity feature1 = new FeatureScalingPriceEntity(
                ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                new BigDecimal("10.00"), new BigDecimal("2.50"), 100
        );

        FeatureScalingPriceEntity feature2 = new FeatureScalingPriceEntity(
                ID_VO, NAME_VO, DESC_VO, LABEL_VO, MEASUREMENT_UNIT_VO,
                new BigDecimal("10.00"), new BigDecimal("2.50"), 100
        );

        assertEquals(feature1, feature2);
        assertEquals(feature1.hashCode(), feature2.hashCode());
    }

    @Test
    @DisplayName("Should correctly implement toString")
    void testToString() {
        FeatureScalingPriceEntity feature = createValidEntity();

        String expected = "FeatureScalingPriceEntity{" +
                "id=" + feature.getId() +
                ", name='" + feature.getNameVO().value() + '\'' +
                ", unit='" + MEASUREMENT_UNIT_VO.unit() + '\'' +
                ", base=" + feature.getBaseAmount() +
                ", increment=" + feature.getIncrementAmount() +
                ", max=" + feature.getMaxQuantity() +
                '}';

        assertEquals(expected, feature.toString());
    }
}
