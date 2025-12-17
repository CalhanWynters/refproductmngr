package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import java.util.Objects;

public record CategoryVO(String category) {
    public CategoryVO {
        Objects.requireNonNull(category, "category must not be null");
        category = category.trim(); // Trimmed value
        if (category.isEmpty()) {
            throw new IllegalArgumentException("category cannot be empty");
        }
        if (category.length() > 100) {  // Example maximum length
            throw new IllegalArgumentException("Category cannot exceed 100 characters.");
        }
        if (!category.matches("[A-Za-z0-9 ]+")) {
            throw new IllegalArgumentException("Category can only contain alphanumeric characters and spaces.");
        }
    }

    public String value() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryVO(String category1))) return false;
        return Objects.equals(category, category1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category);
    }

    @Override
    public String toString() {
        return "CategoryVO{category='" + category + "'}";
    }
}
