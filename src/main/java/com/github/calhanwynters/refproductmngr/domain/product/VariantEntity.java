package com.github.calhanwynters.refproductmngr.domain.product;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("ClassCanBeRecord")
public class VariantEntity {
    private final VariantIdVO id;
    private final SkuVO sku;
    // Changed BigDecimal to PriceVO for encapsulation
    private final PriceVO basePrice;
    private final PriceVO currentPrice;
    private final Set<FeatureAbstractClass> features;
    private final CareInstructionVO careInstructions;
    private final WeightVO weight;
    private final VariantStatusEnums status;

    public VariantEntity(
            VariantIdVO id,
            SkuVO sku,
            // Changed parameter types to PriceVO
            PriceVO basePrice,
            PriceVO currentPrice,
            Set<FeatureAbstractClass> features,
            CareInstructionVO careInstructions,
            WeightVO weight,
            VariantStatusEnums status) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(sku, "sku must not be null");
        Objects.requireNonNull(basePrice, "basePrice must not be null");
        Objects.requireNonNull(currentPrice, "currentPrice must not be null");
        Objects.requireNonNull(features, "features must not be null");
        Objects.requireNonNull(careInstructions, "careInstructions must not be null");
        Objects.requireNonNull(weight, "weight must not be null");
        Objects.requireNonNull(status, "status must not be null");

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
     * Factory method to create a new DRAFT variant using BigDecimals,
     * which are converted internally to PriceVOs with default currency (USD).
     */
    public static VariantEntity createDraft(
            BigDecimal basePriceValue, // Takes raw value
            WeightVO weight,
            CareInstructionVO careInstructions,
            Set<FeatureAbstractClass> features) {
        VariantIdVO generatedId = VariantIdVO.generate();
        SkuVO generatedSku = new SkuVO(String.format("VARIANT-%s", UUID.randomUUID().toString().substring(0, 8).toUpperCase()));

        // Convert raw BigDecimal to a default PriceVO (USD, precision 2)
        PriceVO defaultPrice = new PriceVO(basePriceValue);

        return new VariantEntity(generatedId, generatedSku, defaultPrice, defaultPrice, features, careInstructions, weight, VariantStatusEnums.DRAFT);
    }

    // --- Getters ---

    public VariantIdVO getId() {
        return id;
    }

    public SkuVO getSku() {
        return sku;
    }

    // Getters now return the PriceVO object
    public PriceVO getBasePrice() {
        return basePrice;
    }

    public PriceVO getCurrentPrice() {
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

    // --- Other existing methods (no changes needed) ---

    public boolean hasSameAttributes(VariantEntity other) {
        if (other == null) {
            return false;
        }
        return Objects.equals(careInstructions, other.careInstructions) &&
                Objects.equals(weight, other.weight) &&
                Objects.equals(features, other.features);
    }

    @Override
    public String toString() {
        // toString relies on PriceVO's toString implementation (e.g., "$10.00")
        return String.format("Variant[id=%s, sku=%s, basePrice=%s, currentPrice=%s, features=%s, status=%s]",
                id, sku, basePrice, currentPrice, features, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariantEntity that = (VariantEntity) o;
        // Equality checks work automatically with the PriceVO Value Objects
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
    // These methods now accept and use the PriceVO type

    public VariantEntity changeBasePrice(PriceVO newBasePrice) {
        Objects.requireNonNull(newBasePrice, "newBasePrice must not be null");
        return new VariantEntity(this.id, this.sku, newBasePrice, newBasePrice, this.features, this.careInstructions, this.weight, this.status);
    }

    public VariantEntity changeCurrentPrice(PriceVO newCurrentPrice) {
        Objects.requireNonNull(newCurrentPrice, "newCurrentPrice must not be null");
        // NOTE: Business logic should ideally check if the currencies match before assignment
        return new VariantEntity(this.id, this.sku, this.basePrice, newCurrentPrice, this.features, this.careInstructions, this.weight, this.status);
    }

    // --- Lifecycle/Status Behavior Methods (no changes needed) ---

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

    // Optional: Helper method to ensure currency consistency when changing prices
    private void ensureSameCurrency(PriceVO newPrice) {
        if (!this.currentPrice.currency().equals(newPrice.currency())) {
            throw new IllegalArgumentException("Cannot change price currency on an existing variant.");
        }
    }
}
