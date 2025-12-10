package com.github.calhanwynters.refproductmngr.domain.product;

public record VersionVO(int version) {
    public VersionVO {
        if (version < 0) {
            throw new IllegalArgumentException("Version cannot be negative");
        }
    }

}
