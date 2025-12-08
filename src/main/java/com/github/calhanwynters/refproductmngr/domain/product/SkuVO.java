package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.Objects;

public record SkuVO(String sku) {
    public SkuVO {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("sku cannot be null or empty");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkuVO(String sku1))) return false;
        return Objects.equals(sku, sku1);
    }

    @Override
    public String toString() {
        return sku;
    }
}
