package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import java.util.Objects;
import java.util.Set;
import java.util.Collections;

public record VariantEntity(
        VariantIdVO id,
        SkuVO sku,
        PriceVO basePrice,
        PriceVO currentPrice,
        Set<FeatureAbstractClass> features,
        CareInstructionVO careInstructions,
        WeightVO weight,
        VariantStatusEnums status
) {
    /**
     * Compact constructor for validation and defensive copying.
     */
    public VariantEntity {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(sku, "sku must not be null");
        Objects.requireNonNull(basePrice, "basePrice must not be null");
        Objects.requireNonNull(currentPrice, "currentPrice must not be null");
        Objects.requireNonNull(features, "features must not be null");
        Objects.requireNonNull(careInstructions, "careInstructions must not be null");
        Objects.requireNonNull(weight, "weight must not be null");
        Objects.requireNonNull(status, "status must not be null");

        // Defensive copy to ensure the record remains immutable
        features = Collections.unmodifiableSet(features);
    }

    /**
     * Compares the physical attributes of two variants,
     * ignoring identity (ID/SKU) and pricing/status.
     */
    public boolean hasSameAttributes(VariantEntity other) {
        if (other == null) return false;
        return Objects.equals(careInstructions, other.careInstructions) &&
                Objects.equals(weight, other.weight) &&
                Objects.equals(features, other.features);
    }

    public boolean isActive() {
        return this.status == VariantStatusEnums.ACTIVE;
    }

    @Override
    public String toString() {
        return String.format(
                "Variant[id=%s, sku=%s, basePrice=%s, currentPrice=%s, features=%s, status=%s]",
                id, sku, basePrice, currentPrice, features, status
        );
    }

    // Manual equals() and hashCode() removed for brevity as records
    // provide these automatically using all fields.
}
