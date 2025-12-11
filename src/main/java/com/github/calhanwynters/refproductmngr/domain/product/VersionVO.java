package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;

/**
 * An immutable Value Object representing the optimistic locking version of an aggregate.
 */
public record VersionVO(int version) {
    public VersionVO {
        if (version < 0) {
            throw new IllegalArgumentException("Version cannot be negative");
        }
    }

    /**
     * Creates a new VersionVO instance representing the next sequential version.
     * This ensures the immutability of the original object.
     * @return A new VersionVO incremented by one.
     */
    public VersionVO nextVersion() {
        // Creates a *new* instance with the incremented value.
        return new VersionVO(this.version + 1);
    }
}
