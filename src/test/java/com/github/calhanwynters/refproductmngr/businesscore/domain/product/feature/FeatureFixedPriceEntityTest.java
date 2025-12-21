package com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FeatureFixedPriceEntityTest {

    private static final FeatureIdVO ID_VO = new FeatureIdVO("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
    private static final NameVO NAME_VO = new NameVO("Gift Wrapping");
    private static final DescriptionVO DESC_VO = new DescriptionVO("A beautiful gift wrap for your purchase.");
    private static final LabelVO LABEL_VO = new LabelVO("Gift Wrap");
    private static final Boolean IS_UNIQUE = true;

    @Test
    @DisplayName("Should create entity successfully with valid positive parameters")
    void testCreateFeatureFixedPriceEntity_ValidParameters() {
        // Arrange
        BigDecimal price = new BigDecimal("5.00");

        // Act
        FeatureFixedPriceEntity feature = new FeatureFixedPriceEntity(
                ID_VO, NAME_VO, DESC_VO, LABEL_VO, price, IS_UNIQUE
        );

        // Assert
        assertNotNull(feature);
        assertEquals(new BigDecimal("5.00"), feature.getFixedPrice(), "Price should match and be scaled.");
        assertTrue(feature.isUnique(), "IsUnique flag should be true.");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when fixed price is null")
    void testCreateFeatureFixedPriceEntity_NullFixedPrice() {
        // Arrange
        BigDecimal nullPrice = null;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureFixedPriceEntity(ID_VO, NAME_VO, DESC_VO, LABEL_VO, nullPrice, IS_UNIQUE)
        );

        assertEquals("Fixed price must not be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when fixed price is negative")
    void testCreateFeatureFixedPriceEntity_NegativeFixedPrice() {
        // Arrange
        BigDecimal negativePrice = new BigDecimal("-1.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureFixedPriceEntity(ID_VO, NAME_VO, DESC_VO, LABEL_VO, negativePrice, IS_UNIQUE)
        );

        assertEquals("Fixed price cannot be negative.", exception.getMessage());
    }

    @Test
    @DisplayName("Should create entity successfully with a fixed price of zero")
    void testCreateFeatureFixedPriceEntity_ZeroFixedPrice() {
        // Arrange
        BigDecimal zeroPrice = BigDecimal.ZERO;

        // Act
        FeatureFixedPriceEntity feature = new FeatureFixedPriceEntity(
                ID_VO, NAME_VO, DESC_VO, LABEL_VO, zeroPrice, IS_UNIQUE
        );

        // Assert
        // Note: The entity scales to 2 decimal places (0.00)
        assertEquals(new BigDecimal("0.00"), feature.getFixedPrice());
    }

    @Test
    @DisplayName("Should normalize price to two decimal places")
    void testPriceNormalization() {
        // Arrange
        BigDecimal rawPrice = new BigDecimal("10.555"); // Should round to 10.56

        // Act
        FeatureFixedPriceEntity feature = new FeatureFixedPriceEntity(
                ID_VO, NAME_VO, DESC_VO, LABEL_VO, rawPrice, IS_UNIQUE
        );

        // Assert
        assertEquals(new BigDecimal("10.56"), feature.getFixedPrice(), "Price should be rounded HALF_UP to 2 scales.");
    }
}
