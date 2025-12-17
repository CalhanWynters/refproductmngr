package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class VariantEntityFactoryTest {

    private WeightVO mockWeight;
    private CareInstructionVO mockCareInstructions;
    private Set<FeatureAbstractClass> mockFeatures;
    private BigDecimal basePriceValue;

    @BeforeEach
    void setUp() {
        // Mock external dependencies
        mockWeight = mock(WeightVO.class);
        mockCareInstructions = mock(CareInstructionVO.class);
        mockFeatures = Collections.emptySet();
        basePriceValue = new BigDecimal("50.00");
    }

    @Test
    @DisplayName("createDraft should generate valid VOs and set status to DRAFT")
    void createDraft_ValidInputs_CreatesDraftEntity() {
        // 1. Call the factory method
        VariantEntity draftEntity = VariantEntityFactory.createDraft(
                basePriceValue,
                mockWeight,
                mockCareInstructions,
                mockFeatures
        );

        // 2. Verify the entity is not null
        assertNotNull(draftEntity, "The created entity should not be null");

        // 3. Verify all generated/passed VOs are present and valid
        assertNotNull(draftEntity.id(), "ID should be automatically generated");
        assertNotNull(draftEntity.sku(), "SKU should be automatically generated");
        assertNotNull(draftEntity.basePrice(), "Price VO should be created from BigDecimal input");

        // 4. Verify initial state/business rules set by the factory
        assertEquals(VariantStatusEnums.DRAFT, draftEntity.status(), "New entities must start in DRAFT status");

        // 5. Verify the generated SKU format (a simple check, relying on SkuVO internal test for deep validation)
        assertTrue(draftEntity.sku().sku().startsWith("VARIANT-"));

        // 6. Verify input parameters were used correctly (e.g., price value matches input)
        assertEquals(0, basePriceValue.compareTo(draftEntity.basePrice().value()));
        assertEquals(draftEntity.basePrice(), draftEntity.currentPrice(), "Base and Current price should be identical for a new draft");
        assertEquals(mockWeight, draftEntity.weight());
    }
}
