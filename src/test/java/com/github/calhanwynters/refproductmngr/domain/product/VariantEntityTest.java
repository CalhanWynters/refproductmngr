package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class VariantEntityTest {

    // Mock/Dummy dependencies (assuming VOs are tested separately)
    private VariantIdVO mockId;
    private SkuVO mockSku;
    private PriceVO mockBasePrice;
    private PriceVO mockCurrentPrice;
    private CareInstructionVO mockCareInstructions;
    private WeightVO mockWeight;
    private Set<FeatureAbstractClass> mockFeatures;

    @BeforeEach
    void setUp() {
        // Use realistic dummy values for VOs
        mockId = VariantIdVO.generate();
        mockSku = new SkuVO("SKU-12345");
        mockBasePrice = new PriceVO(new BigDecimal("10.00"), 2, Currency.getInstance("USD"));
        mockCurrentPrice = new PriceVO(new BigDecimal("9.99"), 2, Currency.getInstance("USD"));
        // Using mock objects for dependencies that are complex entities/abstractions
        mockCareInstructions = mock(CareInstructionVO.class);
        mockWeight = mock(WeightVO.class);
        mockFeatures = Collections.emptySet(); // Use empty set for simplicity in baseline tests
    }

    // --- Constructor Validation Tests ---

    @Test
    @DisplayName("Should create VariantEntity successfully with valid inputs")
    void constructor_ValidInputs_CreatesEntity() {
        VariantEntity entity = new VariantEntity(
                mockId,
                mockSku,
                mockBasePrice,
                mockCurrentPrice,
                mockFeatures,
                mockCareInstructions,
                mockWeight,
                VariantStatusEnums.ACTIVE
        );

        assertNotNull(entity);
        assertEquals(mockId, entity.getId());
        assertEquals(VariantStatusEnums.ACTIVE, entity.getStatus());
    }

    @Test
    @DisplayName("Should throw NullPointerException if any required argument is null")
    void constructor_NullArguments_ThrowsNPE() {
        // Test with a null ID
        assertThrows(NullPointerException.class, () -> new VariantEntity(null, mockSku, mockBasePrice, mockCurrentPrice, mockFeatures, mockCareInstructions, mockWeight, VariantStatusEnums.ACTIVE));

        // Test with a null SKU
        assertThrows(NullPointerException.class, () -> new VariantEntity(mockId, null, mockBasePrice, mockCurrentPrice, mockFeatures, mockCareInstructions, mockWeight, VariantStatusEnums.ACTIVE));

        // Test with a null Status
        assertThrows(NullPointerException.class, () -> new VariantEntity(mockId, mockSku, mockBasePrice, mockCurrentPrice, mockFeatures, mockCareInstructions, mockWeight, null));
        // You can add more checks for all other parameters if desired.
    }

    // --- Equality and HashCode Tests ---

    @Test
    @DisplayName("equals and hashCode should consider all fields for equality (as defined in your implementation)")
    void equalsAndHashCode_CompareEntities() {
        VariantEntity entity1 = new VariantEntity(mockId, mockSku, mockBasePrice, mockCurrentPrice, mockFeatures, mockCareInstructions, mockWeight, VariantStatusEnums.ACTIVE);
        VariantEntity entity2 = new VariantEntity(mockId, mockSku, mockBasePrice, mockCurrentPrice, mockFeatures, mockCareInstructions, mockWeight, VariantStatusEnums.ACTIVE);

        // Entities created with the exact same VOs should be equal
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());

        // A new entity with a different SKU should NOT be equal (based on your equals implementation)
        SkuVO differentSku = new SkuVO("SKU-DIFFERENT");
        VariantEntity entity3 = new VariantEntity(mockId, differentSku, mockBasePrice, mockCurrentPrice, mockFeatures, mockCareInstructions, mockWeight, VariantStatusEnums.ACTIVE);

        assertNotEquals(entity1, entity3);
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }
}
