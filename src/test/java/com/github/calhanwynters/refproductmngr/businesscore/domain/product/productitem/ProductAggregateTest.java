package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.SkuVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantEntity;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantStatusEnums;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductAggregateTest {

    private ProductIdVO mockProductId;
    private BusinessIdVO mockBusinessId;
    private CategoryVO mockCategory;
    private DescriptionVO mockDescription;
    private VersionVO initialVersion;
    private GalleryVO validGalleryWithOneImage;
    private Set<VariantEntity> singleActiveVariantSet;

    @BeforeEach
    void setUp() {
        mockProductId = new ProductIdVO(UUID.randomUUID().toString());
        mockBusinessId = new BusinessIdVO("B456");
        mockCategory = new CategoryVO("C789");
        mockDescription = new DescriptionVO("Initial Description");
        initialVersion = new VersionVO(0);

        List<ImageUrlVO> images = List.of(new ImageUrlVO("https://example.com/image.jpg"));
        validGalleryWithOneImage = new GalleryVO(images);

        // Aggregate now requires at least one variant to be valid
        singleActiveVariantSet = Set.of(createMockVariant(VariantStatusEnums.ACTIVE, VariantIdVO.generate()));
    }

    private VariantEntity createMockVariant(VariantStatusEnums status, VariantIdVO id) {
        VariantEntity mockVariant = mock(VariantEntity.class);
        when(mockVariant.status()).thenReturn(status);
        when(mockVariant.isActive()).thenReturn(status == VariantStatusEnums.ACTIVE);
        when(mockVariant.id()).thenReturn(id);

        // FIX: Provide a default SkuVO.
        // Without this, the Aggregate's uniqueness check (v.sku().equals(...)) crashes.
        when(mockVariant.sku()).thenReturn(new SkuVO("SKU-" + UUID.randomUUID().toString().substring(0, 8)));

        return mockVariant;
    }


    @Test
    @DisplayName("Constructor should throw IllegalArgumentException if variants are empty (Invariant Check)")
    void constructor_EmptyVariants_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProductAggregate(mockProductId, mockBusinessId, mockCategory, mockDescription, validGalleryWithOneImage, Set.of(), initialVersion, false)
        );
    }

    @Test
    @DisplayName("updateVariantStatus should return new instance with updated variant status")
    void updateVariantStatus_Success() {
        // Arrange: Create a variant and a product containing it
        VariantIdVO variantId = VariantIdVO.generate();
        VariantEntity originalVariant = createMockVariant(VariantStatusEnums.DRAFT, variantId);

        // Set up the withStatus mock to return a new variant instance with the new status
        VariantEntity updatedVariantMock = createMockVariant(VariantStatusEnums.ACTIVE, variantId);
        when(originalVariant.withStatus(VariantStatusEnums.ACTIVE)).thenReturn(updatedVariantMock);

        ProductAggregate product = new ProductAggregate(
                mockProductId, mockBusinessId, mockCategory, mockDescription,
                validGalleryWithOneImage, Set.of(originalVariant), initialVersion, false
        );

        // Act
        ProductAggregate updatedProduct = product.updateVariantStatus(variantId, VariantStatusEnums.ACTIVE);

        // Assert: Verify state change and immutability
        assertNotSame(product, updatedProduct, "Should return a new aggregate instance");

        VariantEntity foundVariant = updatedProduct.variants().stream()
                .filter(v -> v.id().equals(variantId))
                .findFirst()
                .orElseThrow();

        assertEquals(VariantStatusEnums.ACTIVE, foundVariant.status(), "Variant status should be ACTIVE");
        assertEquals(initialVersion.num(), updatedProduct.version().num(), "Version stays same; Repo handles increment");
    }

    @Test
    @DisplayName("updateVariantStatus should throw IllegalArgumentException if variant does not belong to product")
    void updateVariantStatus_WrongVariant_ThrowsException() {
        ProductAggregate product = new ProductAggregate(
                mockProductId, mockBusinessId, mockCategory, mockDescription,
                validGalleryWithOneImage, singleActiveVariantSet, initialVersion, false
        );

        VariantIdVO unknownId = VariantIdVO.generate();

        assertThrows(IllegalArgumentException.class, () ->
                        product.updateVariantStatus(unknownId, VariantStatusEnums.ACTIVE),
                "Should throw error when variant ID is not part of this aggregate"
        );
    }

    @Test
    @DisplayName("updateVariantStatus should throw IllegalStateException when activating variant for product with no images")
    void updateVariantStatus_ActivationWithoutImages_ThrowsException() {
        VariantIdVO variantId = VariantIdVO.generate();
        VariantEntity draftVariant = createMockVariant(VariantStatusEnums.DRAFT, variantId);

        // FIX: Pre-stub the mock to avoid NPE during Aggregate construction
        GalleryVO mockEmptyGallery = mock(GalleryVO.class);
        when(mockEmptyGallery.images()).thenReturn(List.of());

        ProductAggregate productWithNoImages = new ProductAggregate(
                mockProductId, mockBusinessId, mockCategory, mockDescription,
                mockEmptyGallery, Set.of(draftVariant), initialVersion, false
        );

        assertThrows(IllegalStateException.class, () ->
                productWithNoImages.updateVariantStatus(variantId, VariantStatusEnums.ACTIVE)
        );
    }



    @Test
    @DisplayName("softDelete should return new instance with true flag and UNCHANGED version")
    void softDelete_SetsFlagButDoesNotIncrementVersion() {
        ProductAggregate aggregate = new ProductAggregate(
                mockProductId, mockBusinessId, mockCategory, mockDescription,
                validGalleryWithOneImage, singleActiveVariantSet, initialVersion, false
        );

        ProductAggregate deleted = aggregate.softDelete();

        assertTrue(deleted.isDeleted());
        // In legacy-adapted DDD, the domain does not manage version increments
        assertEquals(initialVersion, deleted.version(), "Domain should not increment version; let Infrastructure handle it");
        assertNotSame(aggregate, deleted);
    }

    @Test
    @DisplayName("isPublishable should be false if the product is soft-deleted")
    void isPublishable_FalseIfDeleted() {
        ProductAggregate aggregate = new ProductAggregate(
                mockProductId, mockBusinessId, mockCategory, mockDescription,
                validGalleryWithOneImage, singleActiveVariantSet, initialVersion, false
        );

        ProductAggregate deleted = aggregate.softDelete();

        assertFalse(deleted.isPublishable(), "Deleted products should never be publishable");
    }

    @Test
    @DisplayName("isPublishable should be true when gallery has images and at least one variant is active")
    void isPublishable_ValidConditions() {
        ProductAggregate aggregate = new ProductAggregate(mockProductId, mockBusinessId, mockCategory, mockDescription, validGalleryWithOneImage, singleActiveVariantSet, initialVersion, false);

        assertTrue(aggregate.isPublishable());
    }

    @Test
    @DisplayName("Variants collection should be protected against external modification")
    void variants_AreImmutable() {
        // Arrange
        VariantEntity variant = createMockVariant(VariantStatusEnums.ACTIVE, VariantIdVO.generate());
        Set<VariantEntity> mutableSet = new HashSet<>();
        mutableSet.add(variant);

        ProductAggregate aggregate = new ProductAggregate(
                mockProductId, mockBusinessId, mockCategory, mockDescription,
                validGalleryWithOneImage, mutableSet, initialVersion, false
        );

        // Act & Assert: Set.copyOf() in constructor ensures the internal set is immutable
        assertThrows(UnsupportedOperationException.class, () ->
                        aggregate.variants().add(createMockVariant(VariantStatusEnums.DRAFT, VariantIdVO.generate())),
                "The variants set must be immutable to prevent external side effects"
        );
    }

    @Test
    @DisplayName("addVariant should return new instance with the added variant")
    void addVariant_Success() {
        // Arrange
        ProductAggregate product = new ProductAggregate(
                mockProductId, mockBusinessId, mockCategory, mockDescription,
                validGalleryWithOneImage, singleActiveVariantSet, initialVersion, false
        );

        VariantIdVO newId = VariantIdVO.generate();
        VariantEntity newVariant = createMockVariant(VariantStatusEnums.DRAFT, newId);

        // Explicitly stub a unique SKU for this new variant
        when(newVariant.sku()).thenReturn(new SkuVO("NEW-UNIQUE-SKU"));

        // Act
        ProductAggregate updatedProduct = product.addVariant(newVariant);

        // Assert
        assertNotSame(product, updatedProduct);
        assertEquals(2, updatedProduct.variants().size());
    }


    @Test
    @DisplayName("addVariant should throw IllegalArgumentException when SKU already exists")
    void addVariant_DuplicateSku_ThrowsException() {
        // Arrange: Use the SKU already present in 'singleActiveVariantSet'
        VariantEntity existingVariant = singleActiveVariantSet.iterator().next();
        SkuVO duplicateSku = new SkuVO("EXISTING-SKU");
        when(existingVariant.sku()).thenReturn(duplicateSku);

        ProductAggregate product = new ProductAggregate(
                mockProductId, mockBusinessId, mockCategory, mockDescription,
                validGalleryWithOneImage, singleActiveVariantSet, initialVersion, false
        );

        VariantEntity duplicateVariant = createMockVariant(VariantStatusEnums.DRAFT, VariantIdVO.generate());
        when(duplicateVariant.sku()).thenReturn(duplicateSku);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                        product.addVariant(duplicateVariant),
                "Should block addition of a variant with a duplicate SKU"
        );
    }

    @Test
    @DisplayName("updateBasicInfo should return a new instance with updated description and category")
    void updateBasicInfo_Success() {
        // Arrange
        ProductAggregate product = new ProductAggregate(
                mockProductId, mockBusinessId, mockCategory, mockDescription,
                validGalleryWithOneImage, singleActiveVariantSet, initialVersion, false
        );

        // FIX: Using only alphanumeric characters and spaces to satisfy CategoryVO regex: [A-Za-z0-9 ]+
        DescriptionVO newDescription = new DescriptionVO("Updated Premium Cotton T Shirt Description");
        CategoryVO newCategory = new CategoryVO("CAT NEW 2025");

        // Act
        ProductAggregate updatedProduct = product.updateBasicInfo(newDescription, newCategory);

        // Assert: Verify state change and immutability
        assertEquals(newDescription, updatedProduct.description(), "Description should be updated");
        assertEquals(newCategory, updatedProduct.category(), "Category should be updated");
        assertNotSame(product, updatedProduct, "Should return a new immutable instance");
    }

    @Test
    @DisplayName("updateBasicInfo should throw exception if description or category is null")
    void updateBasicInfo_NullInputs_ThrowsException() {
        // Arrange
        ProductAggregate product = new ProductAggregate(
                mockProductId, mockBusinessId, mockCategory, mockDescription,
                validGalleryWithOneImage, singleActiveVariantSet, initialVersion, false
        );

        // FIX: Expecting NullPointerException because CategoryVO and DescriptionVO
        // constructors both call Objects.requireNonNull(value, ...)
        assertThrows(NullPointerException.class, () ->
                        product.updateBasicInfo(null, new CategoryVO("Valid Category")),
                "Should throw NPE for null description"
        );

        assertThrows(NullPointerException.class, () ->
                        product.updateBasicInfo(new DescriptionVO("Valid Description"), null),
                "Should throw NPE for null category"
        );
    }



}
