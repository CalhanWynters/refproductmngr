package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import java.util.Objects;

public record CategoryVO(String value) {
    public CategoryVO {
        Objects.requireNonNull(value, "value must not be null");
        value = value.trim(); // Trimmed value
        if (value.isEmpty()) {
            throw new IllegalArgumentException("value cannot be empty");
        }
        if (value.length() > 100) {  // Example maximum length
            throw new IllegalArgumentException("Category cannot exceed 100 characters.");
        }
        if (!value.matches("[A-Za-z0-9 ]+")) {
            throw new IllegalArgumentException("Category can only contain alphanumeric characters and spaces.");
        }
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryVO(String category1))) return false;
        return Objects.equals(value, category1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "CategoryVO{value='" + value + "'}";
    }
}
