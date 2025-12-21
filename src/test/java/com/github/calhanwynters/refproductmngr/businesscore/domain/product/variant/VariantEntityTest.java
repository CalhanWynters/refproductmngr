package com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VariantEntity Record Logic Tests")
class VariantEntityTest {

    private VariantIdVO validId;
    private SkuVO validSku;
    private PriceVO validBasePrice;
    private PriceVO validCurrentPrice;
    private CareInstructionVO validCareInstructions;
    private WeightVO validWeight;
    private Set<FeatureAbstractClass> validFeatures;

    @BeforeEach
    void setUp() {
        validId = VariantIdVO.generate();
        validSku = new SkuVO("SKU-12345");
        validBasePrice = new PriceVO(new BigDecimal("10.00"), 2, Currency.getInstance("USD"));
        validCurrentPrice = new PriceVO(new BigDecimal("9.99"), 2, Currency.getInstance("USD"));
        validCareInstructions = new CareInstructionVO("* Handle with care");
        validWeight = new WeightVO(new BigDecimal("0.5"), WeightUnitEnums.KILOGRAM);
        validFeatures = new HashSet<>();
    }

    @Test
    @DisplayName("Should create VariantEntity successfully and enforce immutability on features")
    void constructor_ValidInputs_CreatesImmutableEntity() {
        // Arrange
        FeatureBasicEntity feature = new FeatureBasicEntity(
                FeatureIdVO.generate(), new NameVO("Color"), new DescriptionVO("The sky is very Blue"), new LabelVO("Color"), true);
        validFeatures.add(feature);

        // Act
        VariantEntity entity = new VariantEntity(
                validId, validSku, validBasePrice, validCurrentPrice,
                validFeatures, validCareInstructions, validWeight, VariantStatusEnums.ACTIVE
        );

        // Assert
        assertNotNull(entity);
        assertEquals(validId, entity.id());
        assertEquals(1, entity.getFeatures().size());

        // Verify Immutability: Attempting to modify the retrieved set should throw an exception
        assertThrows(UnsupportedOperationException.class, () -> entity.getFeatures().add(feature));
    }

    @Test
    @DisplayName("Should throw NullPointerException if any required record component is null")
    void constructor_NullArguments_ThrowsNPE() {
        assertAll("Null checks",
                () -> assertThrows(NullPointerException.class, () -> new VariantEntity(null, validSku, validBasePrice, validCurrentPrice, validFeatures, validCareInstructions, validWeight, VariantStatusEnums.ACTIVE)),
                () -> assertThrows(NullPointerException.class, () -> new VariantEntity(validId, validSku, validBasePrice, validCurrentPrice, null, validCareInstructions, validWeight, VariantStatusEnums.ACTIVE)),
                () -> assertThrows(NullPointerException.class, () -> new VariantEntity(validId, validSku, validBasePrice, validCurrentPrice, validFeatures, validCareInstructions, validWeight, null))
        );
    }

    @Test
    @DisplayName("hasSameAttributes should compare physical characteristics ignoring identity and price")
    void hasSameAttributes_ComparesPhysicalsOnly() {
        // Arrange
        VariantEntity entity1 = new VariantEntity(validId, validSku, validBasePrice, validCurrentPrice, validFeatures, validCareInstructions, validWeight, VariantStatusEnums.ACTIVE);

        // Entity 2 has different ID and Price, but same physicals (Weight, Care, Features)
        VariantEntity entity2 = new VariantEntity(VariantIdVO.generate(), new SkuVO("SKU-DIFFERENT"),
                new PriceVO(new BigDecimal("50.00"), 2, Currency.getInstance("USD")),
                new PriceVO(new BigDecimal("40.00"), 2, Currency.getInstance("USD")),
                validFeatures, validCareInstructions, validWeight, VariantStatusEnums.DRAFT);

        // Act & Assert
        assertTrue(entity1.hasSameAttributes(entity2), "Entities with same weight, care instructions, and features should match attributes.");
    }

    @Test
    @DisplayName("withStatus should return a new instance with updated status but identical data")
    void withStatus_ReturnsNewInstance() {
        // Arrange
        VariantEntity original = new VariantEntity(validId, validSku, validBasePrice, validCurrentPrice, validFeatures, validCareInstructions, validWeight, VariantStatusEnums.DRAFT);

        // Act
        VariantEntity updated = original.withStatus(VariantStatusEnums.ACTIVE);

        // Assert
        assertNotSame(original, updated);
        assertEquals(VariantStatusEnums.ACTIVE, updated.status());
        assertEquals(original.id(), updated.id());
        assertEquals(original.sku(), updated.sku());
    }

    @Test
    @DisplayName("Equality should be based on all record components")
    void equality_BasedOnAllComponents() {
        // Arrange
        VariantEntity entity1 = new VariantEntity(validId, validSku, validBasePrice, validCurrentPrice, validFeatures, validCareInstructions, validWeight, VariantStatusEnums.ACTIVE);
        VariantEntity entity2 = new VariantEntity(validId, validSku, validBasePrice, validCurrentPrice, validFeatures, validCareInstructions, validWeight, VariantStatusEnums.ACTIVE);

        // Act & Assert
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }
}
