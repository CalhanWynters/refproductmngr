package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

/**
 * An immutable Value Object representing the optimistic locking num of an aggregate.
 */
public record VersionVO(int num) {
    public VersionVO {
        if (num < 0) {
            throw new IllegalArgumentException("Version cannot be negative");
        }
    }

    /**
     * Creates a new VersionVO instance representing the next sequential num.
     * This ensures the immutability of the original object.
     * @return A new VersionVO incremented by one.
     */
    public VersionVO nextVersion() {
        // Creates a *new* instance with the incremented value.
        return new VersionVO(this.num + 1);
    }
}
