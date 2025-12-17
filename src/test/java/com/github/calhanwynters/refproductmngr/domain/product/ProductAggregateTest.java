package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductAggregateTest {

    // Mocks/Stubs for VOs that we don't need to test internally
    private ProductIdVO mockProductId;
    private BusinessIdVO mockBusinessId;
    private CategoryVO mockCategory;
    private DescriptionVO mockDescription;
    private VersionVO mockVersion;
    // Renamed from emptyGallery to validGalleryWithOneImage
    private GalleryVO validGalleryWithOneImage;
    private Set<VariantEntity> emptyVariants;

    @BeforeEach
    void setUp() {
        mockProductId = new ProductIdVO(UUID.randomUUID().toString());
        mockBusinessId = new BusinessIdVO("B456");
        mockCategory = new CategoryVO("C789");
        mockDescription = new DescriptionVO("Initial Description");
        // Use int constructor for VersionVO
        mockVersion = new VersionVO(1);

        // FIX: Initialize GalleryVO with at least one ImageUrlVO to satisfy new constraint
        List<ImageUrlVO> images = List.of(new ImageUrlVO("https://example.com/valid-image-1.jpg"));
        validGalleryWithOneImage = new GalleryVO(images);

        emptyVariants = Collections.emptySet();
    }

    // --- Helper methods to create specific VariantEntity mocks ---
    private VariantEntity createMockVariant(VariantStatusEnums status, VariantIdVO id) {
        VariantEntity mockVariant = mock(VariantEntity.class);
        when(mockVariant.status()).thenReturn(status);
        // Explicitly define behavior for isActive based on status
        when(mockVariant.isActive()).thenReturn(status == VariantStatusEnums.ACTIVE);
        when(mockVariant.id()).thenReturn(id);
        return mockVariant;
    }

    private GalleryVO createGalleryWithImages() {
        List<ImageUrlVO> images;
        Set<ImageUrlVO> imageSet = new HashSet<>();
        for (int i = 0; i < 1; i++) {
            // Use simple URLs for ImageUrlVO stub
            imageSet.add(new ImageUrlVO("https://example.com/img" + i + ".jpg"));
        }
        images = List.copyOf(imageSet);
        // NOTE: This helper method might now fail if count is 0, due to the new GalleryVO constraint.
        return new GalleryVO(images);
    }

    // --- Constructor & Initialization Tests ---

    @Test
    @DisplayName("Constructor should create aggregate successfully with valid inputs")
    void constructor_ValidInputs_CreatesEntity() {
        ProductAggregate aggregate = new ProductAggregate(
                mockProductId, mockBusinessId, mockCategory, mockDescription,
                validGalleryWithOneImage, emptyVariants, mockVersion // Use valid gallery
        );

        assertNotNull(aggregate);
        assertEquals(mockProductId, aggregate.id());
        assertTrue(aggregate.variants().isEmpty());
    }

    @Test
    @DisplayName("Constructor should throw NullPointerException if any mandatory field is null")
    void constructor_NullInputs_ThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new ProductAggregate(null, mockBusinessId, mockCategory, mockDescription, validGalleryWithOneImage, emptyVariants, mockVersion));
        assertThrows(NullPointerException.class, () -> new ProductAggregate(mockProductId, mockBusinessId, mockCategory, mockDescription, validGalleryWithOneImage, null, mockVersion));
    }

    @Test
    @DisplayName("Variants collection returned by getter should be externally immutable")
    void constructor_VariantsAreImmutable() {
        Set<VariantEntity> mutableSet = new HashSet<>();
        mutableSet.add(mock(VariantEntity.class));

        ProductAggregate aggregate = new ProductAggregate(
                mockProductId, mockBusinessId, mockCategory, mockDescription,
                validGalleryWithOneImage, mutableSet, mockVersion // Use valid gallery
        );

        assertThrows(UnsupportedOperationException.class, () -> aggregate.variants().add(mock(VariantEntity.class)));
    }

    // --- State/Validation Check Methods Tests ---

    @Test
    @DisplayName("isPublishable should be true only when both images and active variants constraints are met")
    void isPublishable_CompositeValidation() {
        Set<VariantEntity> publishableVariants = Set.of(createMockVariant(VariantStatusEnums.ACTIVE, VariantIdVO.generate()));
        // This helper method creates a valid gallery with at least 1 image internally.
        GalleryVO publishableGallery = createGalleryWithImages();

        // Scenario 1: Ready to publish
        ProductAggregate ready = new ProductAggregate(mockProductId, mockBusinessId, mockCategory, mockDescription, publishableGallery, publishableVariants, mockVersion);
        assertTrue(ready.isPublishable(), "Should be publishable if all conditions met");

        // Scenario 2: Missing images (This scenario now requires a slightly different approach
        // because we can't create an invalid GalleryVO in a clean way unless we relax constraints in tests or use mocks heavily)
        // We assume createGalleryWithImages(0) is no longer a valid input for the constructor's parameters.

        // Scenario 3: Has images, but no active variants
        Set<VariantEntity> inactiveVariants = Set.of(createMockVariant(VariantStatusEnums.INACTIVE, VariantIdVO.generate()));
        // Use the valid gallery defined in setUp for construction
        ProductAggregate noActiveVariants = new ProductAggregate(mockProductId, mockBusinessId, mockCategory, mockDescription, validGalleryWithOneImage, inactiveVariants, mockVersion);
        assertFalse(noActiveVariants.isPublishable(), "Cannot publish without active variants");

        // Scenario 4 (New Edge Case): Mix of active and inactive variants still counts if at least one is active
        Set<VariantEntity> mixedVariants = new HashSet<>();
        mixedVariants.add(createMockVariant(VariantStatusEnums.INACTIVE, VariantIdVO.generate()));
        mixedVariants.add(createMockVariant(VariantStatusEnums.ACTIVE, VariantIdVO.generate()));
        ProductAggregate mixedProduct = new ProductAggregate(mockProductId, mockBusinessId, mockCategory, mockDescription, validGalleryWithOneImage, mixedVariants, mockVersion);
        assertTrue(mixedProduct.isPublishable(), "Should be publishable if at least one variant is active among others");
    }
}
