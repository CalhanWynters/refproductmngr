package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.CategoryVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryVOTest {

    @Test
    void testValidCategory() {
        String validCategory = "Electronics";
        CategoryVO categoryVO = new CategoryVO(validCategory);
        assertEquals(validCategory, categoryVO.value());
    }

    @Test
    void testNullCategoryThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> new CategoryVO(null));
        assertEquals("category must not be null", exception.getMessage());
    }

    @Test
    void testEmptyCategoryThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new CategoryVO("   ")); // Spaces only
        assertEquals("category cannot be empty", exception.getMessage());
    }

    @Test
    void testCategoryExceedingMaximumLengthThrowsException() {
        String longCategory = "A".repeat(101); // Create a string that exceeds the max length
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new CategoryVO(longCategory));
        assertEquals("Category cannot exceed 100 characters.", exception.getMessage());
    }

    @Test
    void testCategoryWithInvalidCharactersThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new CategoryVO("Electronics!@#")); // Invalid characters
        assertEquals("Category can only contain alphanumeric characters and spaces.", exception.getMessage());
    }

    @Test
    void testEqualsWorksCorrectly() {
        CategoryVO category1 = new CategoryVO("Books");
        CategoryVO category2 = new CategoryVO("Books");
        assertEquals(category1, category2);
        assertNotEquals(new CategoryVO("Electronics"), category1);
        assertNotEquals(null, category1); // Ensure not equal to null
        assertNotEquals(new Object(), category1); // Ensure not equal to different object type
    }

    @Test
    void testHashCodeConsistency() {
        CategoryVO category = new CategoryVO("Toys");
        assertEquals(category.hashCode(), new CategoryVO("Toys").hashCode());
    }

    @Test
    void testToStringFormat() {
        CategoryVO category = new CategoryVO("Furniture");
        assertEquals("CategoryVO{category='Furniture'}", category.toString());
    }
}
