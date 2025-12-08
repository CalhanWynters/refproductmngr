package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class FeatureFixedPriceEntityTest {

    // Define common immutable Value Objects once to reduce boilerplate code
    private static final FeatureIdVO ID_VO = new FeatureIdVO("gift-wrap");
    private static final NameVO NAME_VO = new NameVO("Gift Wrapping");
    private static final DescriptionVO DESC_VO = new DescriptionVO("A beautiful gift wrap for your purchase.");
    private static final LabelVO LABEL_VO = new LabelVO("Gift Wrap");

    @Test
    @DisplayName("Should create entity successfully with valid positive parameters")
    void testCreateFeatureFixedPriceEntity_ValidParameters() {
        FeatureFixedPriceEntity feature = new FeatureFixedPriceEntity(
                ID_VO,
                NAME_VO,
                DESC_VO,
                LABEL_VO,
                new BigDecimal("5.00")
        );

        // This test now serves dual purpose: creation and getter validation
        assertNotNull(feature.getFixedPrice());
        assertEquals(new BigDecimal("5.00"), feature.getFixedPrice());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when fixed price is null")
    void testCreateFeatureFixedPriceEntity_NullFixedPrice() {
        // Using assertThrows to capture the exception thrown by the constructor
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureFixedPriceEntity(ID_VO, NAME_VO, DESC_VO, LABEL_VO, null)
        );

        assertEquals("Fixed price must not be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when fixed price is negative")
    void testCreateFeatureFixedPriceEntity_NegativeFixedPrice() {
        // Using assertThrows to capture the exception thrown by the constructor
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureFixedPriceEntity(ID_VO, NAME_VO, DESC_VO, LABEL_VO, new BigDecimal("-1.00"))
        );

        assertEquals("Fixed price cannot be negative.", exception.getMessage());
    }

    @Test
    @DisplayName("Should create entity successfully with a fixed price of zero")
    void testCreateFeatureFixedPriceEntity_ZeroFixedPrice() {
        FeatureFixedPriceEntity feature = new FeatureFixedPriceEntity(
                ID_VO,
                NAME_VO,
                DESC_VO,
                LABEL_VO,
                BigDecimal.ZERO
        );

        assertNotNull(feature.getFixedPrice());
        assertEquals(BigDecimal.ZERO, feature.getFixedPrice());
    }


    // Removed the original 'testGetFixedPrice' as the functionality is fully covered
    // by 'testCreateFeatureFixedPriceEntity_ValidParameters'
}
