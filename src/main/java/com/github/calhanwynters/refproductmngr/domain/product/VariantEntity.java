package com.github.calhanwynters.refproductmngr.domain.product;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Represents an immutable product variant as a standard public class.
 * This class replaces the Java record syntax provided previously,
 * implementing immutability through final fields and a behavior-rich interface.
 */
@SuppressWarnings("ClassCanBeRecord")
public class VariantEntity {
    // Explicit declaration of fields (private and final for immutability)
    private final VariantIdVO id;
    private final String sku;
    private final BigDecimal basePrice;
    private final BigDecimal currentPrice;
    private final Set<FeatureAbstractClass> features;
    private final CareInstructionVO careInstructions;
    private final WeightVO weight;
    private final VariantStatusEnums status;

    /**
     * Public Constructor for creating the VariantEntity instance, with validation.
     */
    public VariantEntity(
            VariantIdVO id,
            String sku,
            BigDecimal basePrice,
            BigDecimal currentPrice,
            Set<FeatureAbstractClass> features,
            CareInstructionVO careInstructions,
            WeightVO weight,
            VariantStatusEnums status) {
        // Validation logic
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(sku, "sku must not be null");
        Objects.requireNonNull(basePrice, "basePrice must not be null");
        Objects.requireNonNull(currentPrice, "currentPrice must not be null");
        Objects.requireNonNull(features, "features must not be null");
        Objects.requireNonNull(careInstructions, "careInstructions must not be null");
        Objects.requireNonNull(weight, "weight must not be null");
        Objects.requireNonNull(status, "status must not be null");

        // Assigning parameters to the final fields
        this.id = id;
        this.sku = sku;
        this.basePrice = basePrice;
        this.currentPrice = currentPrice;
        this.features = features;
        this.careInstructions = careInstructions;
        this.weight = weight;
        this.status = status;
    }

    /**
     * Factory method to create a basic new DRAFT variant.
     * This hides the complexity of generating IDs and SKUs, and sets
     * the initial status and current price automatically.
     *
     * @param basePrice The initial base monetary amount.
     * @param weight The physical weight of the product.
     * @param careInstructions Specific care instructions for the product.
     * @param features Optional list of features.
     * @return A new Variant instance initialized as DRAFT.
     */
    public static VariantEntity createDraft(
            BigDecimal basePrice,
            WeightVO weight,
            CareInstructionVO careInstructions,
            Set<FeatureAbstractClass> features) {
        // Logic for auto-generation is encapsulated in the factory method
        VariantIdVO generatedId = VariantIdVO.generate();
        String generatedSku = String.format("VARIANT-%s", UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return new VariantEntity(generatedId, generatedSku, basePrice, basePrice, features, careInstructions, weight, VariantStatusEnums.DRAFT);
    }

    // --- Public Getter methods (equivalent to record component accessors) ---

    public VariantIdVO getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public Set<FeatureAbstractClass> getFeatures() {
        return features;
    }

    public CareInstructionVO getCareInstructions() {
        return careInstructions;
    }

    public WeightVO getWeight() {
        return weight;
    }

    public VariantStatusEnums getStatus() {
        return status;
    }

    // --- Other existing methods ---

    /**
     * Checks if this variant has the same physical attributes as another variant.
     * Note: This relies on correct implementations of equals() for the VO classes (CareInstructionVO, WeightVO, FeatureAbstractClass).
     */
    public boolean hasSameAttributes(VariantEntity other) {
        if (other == null) {
            return false;
        }
        return Objects.equals(careInstructions, other.careInstructions) &&
                Objects.equals(weight, other.weight) &&
                Objects.equals(features, other.features);
    }

    // --- Standard Overrides (equals, hashCode, toString) ---
    // These must be explicitly implemented when converting from a record to a class for correct behavior in collections.

    @Override
    public String toString() {
        return String.format("Variant[id=%s, sku=%s, basePrice=%s, currentPrice=%s, features=%s, status=%s]",
                id, sku, basePrice, currentPrice, features, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariantEntity that = (VariantEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sku, that.sku) &&
                Objects.equals(basePrice, that.basePrice) &&
                Objects.equals(currentPrice, that.currentPrice) &&
                Objects.equals(features, that.features) &&
                Objects.equals(careInstructions, that.careInstructions) &&
                Objects.equals(weight, that.weight) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, basePrice, currentPrice, features, careInstructions, weight, status);
    }

    // --- Behavior Methods (return new instances to maintain immutability) ---

    public VariantEntity changeBasePrice(BigDecimal newBasePrice) {
        // When base price changes, current price often resets to the new base price
        return new VariantEntity(this.id, this.sku, newBasePrice, newBasePrice, this.features, this.careInstructions, this.weight, this.status);
    }

    public VariantEntity changeCurrentPrice(BigDecimal newCurrentPrice) {
        return new VariantEntity(this.id, this.sku, this.basePrice, newCurrentPrice, this.features, this.careInstructions, this.weight, this.status);
    }

    // --- Lifecycle/Status Behavior Methods ---

    public boolean isActive() {
        return this.status == VariantStatusEnums.ACTIVE;
    }

    public VariantEntity activate() {
        if (this.status == VariantStatusEnums.DISCONTINUED) {
            throw new IllegalStateException("Cannot activate a discontinued variant.");
        }
        return new VariantEntity(this.id, this.sku, this.basePrice, this.currentPrice, this.features, this.careInstructions, this.weight, VariantStatusEnums.ACTIVE);
    }

    public VariantEntity deactivate() {
        return new VariantEntity(this.id, this.sku, this.basePrice, this.currentPrice, this.features, this.careInstructions, this.weight, VariantStatusEnums.INACTIVE);
    }

    public VariantEntity markAsDiscontinued() {
        return new VariantEntity(this.id, this.sku, this.basePrice, this.currentPrice, this.features, this.careInstructions, this.weight, VariantStatusEnums.DISCONTINUED);
    }
}
