package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryVOTest {

    @Test
    void testValidCategory() {
        // Valid category
        String validCategory = "Electronics";
        CategoryVO categoryVO = new CategoryVO(validCategory);
        assertEquals(validCategory, categoryVO.value());
    }

    @Test
    void testNullCategoryThrowsException() {
        // Null category
        Exception exception = assertThrows(NullPointerException.class, () -> new CategoryVO(null));
        assertEquals("category must not be null", exception.getMessage());
    }

    @Test
    void testEmptyCategoryThrowsException() {
        // Empty category
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CategoryVO("   "); // Spaces only
        });
        assertEquals("category cannot be empty", exception.getMessage());
    }

    @Test
    void testEqualsWorksCorrectly() {
        CategoryVO category1 = new CategoryVO("Books");
        CategoryVO category2 = new CategoryVO("Books");
        assertEquals(category1, category2);
        assertNotEquals(new CategoryVO("Electronics"), category1);
    }

    @Test
    void testHashCodeConsistency() {
        CategoryVO category = new CategoryVO("Toys");
        assertEquals(category.hashCode(), new CategoryVO("Toys").hashCode());
    }
}
