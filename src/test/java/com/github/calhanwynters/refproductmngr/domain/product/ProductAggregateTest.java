package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductAggregateTest {
/*
    private BusinessIdVO businessId;
    private CategoryVO category;
    private DescriptionVO description;
    private GalleryVO gallery;
    private Set<VariantEntity> variants;
    private ProductAggregate product;

    @BeforeEach
    void setUp() {
        businessId = new BusinessIdVO("business-123");
        category = new CategoryVO("Electronics");
        description = new DescriptionVO("A good electronic product");
        gallery = new GalleryVO(new HashSet<>());
        variants = new HashSet<>();

        product = ProductAggregate.create(businessId, category, description, gallery, variants);
    }

    @Test
    void testCreateProductAggregate() {
        assertNotNull(product);
        assertEquals(businessId, product.businessIdVO());
        assertEquals(category, product.category());
        assertEquals(description, product.description());
        assertEquals(gallery, product.gallery());
        assertTrue(product.variants().isEmpty());
    }

    @Test
    void testChangeDescription() {
        DescriptionVO newDescription = new DescriptionVO("A better electronic product");
        ProductAggregate updatedProduct = product.changeDescription(newDescription);

        assertNotEquals(product.description(), updatedProduct.description());
        assertEquals(newDescription, updatedProduct.description());
    }

    @Test
    void testAddImage() {
        ImageUrlVO newImageUrl = new ImageUrlVO("http://example.com/image.jpg");
        ProductAggregate updatedProduct = product.addImage(newImageUrl);

        assertNotEquals(product.gallery(), updatedProduct.gallery());
        assertTrue(updatedProduct.gallery().images().contains(newImageUrl));
    }

    @Test
    void testAddVariant() {
        VariantEntity newVariant = new VariantEntity(new VariantIdVO("variant-1"), "Variant 1", 100.0);
        ProductAggregate updatedProduct = product.addVariant(newVariant);

        assertNotEquals(product.variants(), updatedProduct.variants());
        assertTrue(updatedProduct.variants().contains(newVariant));
    }

    @Test
    void testAddDuplicateVariantThrowsException() {
        VariantEntity existingVariant = new VariantEntity(new VariantIdVO("variant-1"), "Variant 1", 100.0);
        ProductAggregate updatedProduct = product.addVariant(existingVariant);

        Exception exception = assertThrows(VariantAlreadyExistsException.class, () -> {
            updatedProduct.addVariant(existingVariant);
        });

        assertEquals("Variant with ID " + existingVariant.getId().value() + " already exists.", exception.getMessage());
    }

    @Test
    void testFindVariantById() {
        VariantEntity existingVariant = new VariantEntity(new VariantIdVO("variant-1"), "Variant 1", 100.0);
        product = product.addVariant(existingVariant);

        Optional<VariantEntity> foundVariant = product.findVariantById(existingVariant.getId());

        assertTrue(foundVariant.isPresent());
        assertEquals(existingVariant, foundVariant.get());
    }

    @Test
    void testFindVariantByIdNotFound() {
        Optional<VariantEntity> foundVariant = product.findVariantById(new VariantIdVO("non-existing"));

        assertFalse(foundVariant.isPresent());
    }

 */
}
