package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class FeatureFixedPriceEntityTest {

    // Define common immutable Value Objects once to reduce boilerplate code
    // FIX: Use a valid UUID string to prevent ExceptionInInitializerError during class loading.
    private static final FeatureIdVO ID_VO = new FeatureIdVO("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
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

        assertNotNull(feature.getFixedPrice());
        assertEquals(new BigDecimal("5.00"), feature.getFixedPrice());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when fixed price is null")
    void testCreateFeatureFixedPriceEntity_NullFixedPrice() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new FeatureFixedPriceEntity(ID_VO, NAME_VO, DESC_VO, LABEL_VO, null)
        );

        assertEquals("Fixed price must not be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when fixed price is negative")
    void testCreateFeatureFixedPriceEntity_NegativeFixedPrice() {
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
}
