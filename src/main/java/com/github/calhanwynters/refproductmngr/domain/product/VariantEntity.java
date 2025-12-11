package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;
import java.util.Set;

@SuppressWarnings("ClassCanBeRecord")
public class VariantEntity {
    private final VariantIdVO id;
    private final SkuVO sku;
    private final PriceVO basePrice;
    private final PriceVO currentPrice;
    private final Set<FeatureAbstractClass> features;
    private final CareInstructionVO careInstructions;
    private final WeightVO weight;
    private final VariantStatusEnums status;

    public VariantEntity(VariantIdVO id, SkuVO sku, PriceVO basePrice, PriceVO currentPrice, Set<FeatureAbstractClass> features, CareInstructionVO careInstructions, WeightVO weight, VariantStatusEnums status) {
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

    // --- Getters ---
    public VariantIdVO getId() { return id; }
    public SkuVO getSku() { return sku; }
    public PriceVO getBasePrice() { return basePrice; }
    public PriceVO getCurrentPrice() { return currentPrice; }
    public Set<FeatureAbstractClass> getFeatures() { return features; }
    public CareInstructionVO getCareInstructions() { return careInstructions; }
    public WeightVO getWeight() { return weight; }
    public VariantStatusEnums getStatus() { return status; }

    // --- Other existing methods (no changes needed) ---
    public boolean hasSameAttributes(VariantEntity other) {
        if (other == null) { return false; }
        return Objects.equals(careInstructions, other.careInstructions) &&
                Objects.equals(weight, other.weight) &&
                Objects.equals(features, other.features);
    }

    public boolean isActive() {
        return this.status == VariantStatusEnums.ACTIVE;
    }

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
}
