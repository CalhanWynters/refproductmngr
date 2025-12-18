package com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.FeatureAbstractClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class VariantEntityBehaviorTest {

    // A baseline active entity to use in tests, built with mock dependencies
    private VariantEntity activeUsdVariant;
    private PriceVO initialBasePrice;
    private final Currency USD = Currency.getInstance("USD");
    private final Currency EUR = Currency.getInstance("EUR");


    @BeforeEach
    void setUp() {
        // Setup common VOs
        initialBasePrice = new PriceVO(new BigDecimal("100.00"), 2, USD);
        PriceVO initialCurrentPrice = new PriceVO(new BigDecimal("90.00"), 2, USD);

        // Build a baseline Entity using mocks for everything but the price and status we care about
        activeUsdVariant = new VariantEntity(
                VariantIdVO.generate(),
                new SkuVO("BASE-SKU"),
                initialBasePrice,
                initialCurrentPrice,
                Set.of(mock(FeatureAbstractClass.class)),
                mock(CareInstructionVO.class),
                mock(WeightVO.class),
                VariantStatusEnums.ACTIVE // Start as ACTIVE
        );
    }

    // --- Price Change Behavior Tests ---

    @Test
    @DisplayName("changeBasePrice should update both base and current price to the new value")
    void changeBasePrice_UpdatesBothPrices() {
        PriceVO newPrice = new PriceVO(new BigDecimal("150.00"), 2, USD);

        VariantEntity updatedVariant = VariantEntityBehavior.changeBasePrice(activeUsdVariant, newPrice);

        // Verify that the new entity is indeed a *new* instance (immutability check)
        assertNotSame(activeUsdVariant, updatedVariant);

        // Verify prices were updated correctly
        assertEquals(newPrice, updatedVariant.basePrice());
        assertEquals(newPrice, updatedVariant.currentPrice(), "Current price should match the new base price after this operation");

        // Verify other attributes are unchanged
        assertEquals(activeUsdVariant.id(), updatedVariant.id());
        assertEquals(activeUsdVariant.status(), updatedVariant.status());
    }

    @Test
    @DisplayName("changeCurrentPrice should only update the current price")
    void changeCurrentPrice_UpdatesOnlyCurrentPrice() {
        PriceVO salePrice = new PriceVO(new BigDecimal("75.00"), 2, USD);

        VariantEntity updatedVariant = VariantEntityBehavior.changeCurrentPrice(activeUsdVariant, salePrice);

        assertNotSame(activeUsdVariant, updatedVariant);

        // Verify the current price was updated
        assertEquals(salePrice, updatedVariant.currentPrice());

        // Verify the base price remains unchanged
        assertEquals(initialBasePrice, updatedVariant.basePrice(), "Base price should not change during current price update");
    }

    @Test
    @DisplayName("changeCurrentPrice should throw exception if currency changes")
    void changeCurrentPrice_CurrencyMismatch_ThrowsException() {
        PriceVO euroPrice = new PriceVO(new BigDecimal("80.00"), 2, EUR);

        assertThrows(IllegalArgumentException.class, () -> VariantEntityBehavior.changeCurrentPrice(activeUsdVariant, euroPrice), "Should prevent changing currency using the changeCurrentPrice behavior method.");
    }

    // --- Status/Lifecycle Behavior Tests ---

    @Test
    @DisplayName("deactivate should change status to INACTIVE")
    void deactivate_ChangesStatusToInactive() {
        VariantEntity inactiveVariant = VariantEntityBehavior.deactivate(activeUsdVariant);

        assertNotSame(activeUsdVariant, inactiveVariant);
        assertEquals(VariantStatusEnums.INACTIVE, inactiveVariant.status());
        assertTrue(activeUsdVariant.isActive()); // Original remains active
    }

    @Test
    @DisplayName("activate should change status to ACTIVE")
    void activate_ChangesStatusToActive() {
        // First create an INACTIVE variant
        VariantEntity inactiveVariant = VariantEntityBehavior.deactivate(activeUsdVariant);
        assertEquals(VariantStatusEnums.INACTIVE, inactiveVariant.status());

        // Now test activation
        VariantEntity activatedVariant = VariantEntityBehavior.activate(inactiveVariant);

        assertNotSame(inactiveVariant, activatedVariant);
        assertEquals(VariantStatusEnums.ACTIVE, activatedVariant.status());
    }

    @Test
    @DisplayName("markAsDiscontinued should set status to DISCONTINUED")
    void markAsDiscontinued_SetsStatus() {
        VariantEntity discontinuedVariant = VariantEntityBehavior.markAsDiscontinued(activeUsdVariant);

        assertNotSame(activeUsdVariant, discontinuedVariant);
        assertEquals(VariantStatusEnums.DISCONTINUED, discontinuedVariant.status());
    }

    @Test
    @DisplayName("activate should throw exception if attempting to activate a DISCONTINUED variant")
    void activate_DiscontinuedVariant_ThrowsIllegalStateException() {
        VariantEntity discontinuedVariant = VariantEntityBehavior.markAsDiscontinued(activeUsdVariant);

        assertThrows(IllegalStateException.class, () -> VariantEntityBehavior.activate(discontinuedVariant), "Cannot re-activate a product that has been discontinued.");
    }
}
