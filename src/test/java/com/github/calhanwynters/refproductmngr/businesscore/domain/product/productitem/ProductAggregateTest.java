package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
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
    @DisplayName("softDelete should return new instance with incremented version and true flag")
    void softDelete_IncrementsVersionAndSetsFlag() {
        ProductAggregate aggregate = new ProductAggregate(mockProductId, mockBusinessId, mockCategory, mockDescription, validGalleryWithOneImage, singleActiveVariantSet, initialVersion, false);

        ProductAggregate deleted = aggregate.softDelete();

        assertTrue(deleted.isDeleted());
        assertEquals(initialVersion.num() + 1, deleted.version().num());
        assertNotSame(aggregate, deleted);
    }

    @Test
    @DisplayName("isPublishable should be false if the product is soft-deleted")
    void isPublishable_FalseIfDeleted() {
        ProductAggregate aggregate = new ProductAggregate(mockProductId, mockBusinessId, mockCategory, mockDescription, validGalleryWithOneImage, singleActiveVariantSet, initialVersion, false);

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
    @DisplayName("restore should return a new instance with isDeleted false and incremented version")
    void restore_ResetsFlagAndIncrementsVersion() {
        ProductAggregate deletedAggregate = new ProductAggregate(mockProductId, mockBusinessId, mockCategory, mockDescription, validGalleryWithOneImage, singleActiveVariantSet, initialVersion, true);

        ProductAggregate restored = deletedAggregate.restore();

        assertFalse(restored.isDeleted());
        assertEquals(initialVersion.num() + 1, restored.version().num());
    }

    @Test
    @DisplayName("Variants collection should be protected against external modification")
    void variants_AreImmutable() {
        Set<VariantEntity> mutableSet = new HashSet<>();
        mutableSet.add(createMockVariant(VariantStatusEnums.ACTIVE, VariantIdVO.generate()));

        ProductAggregate aggregate = new ProductAggregate(mockProductId, mockBusinessId, mockCategory, mockDescription, validGalleryWithOneImage, mutableSet, initialVersion, false);

        assertThrows(UnsupportedOperationException.class, () ->
                aggregate.variants().add(createMockVariant(VariantStatusEnums.DRAFT, VariantIdVO.generate()))
        );
    }

    @Test
    @DisplayName("updateContent should update description and increment version")
    void updateContent_UpdatesStateAndVersion() {
        ProductAggregate aggregate = new ProductAggregate(mockProductId, mockBusinessId, mockCategory, mockDescription, validGalleryWithOneImage, singleActiveVariantSet, initialVersion, false);
        DescriptionVO newDesc = new DescriptionVO("Updated description");

        ProductAggregate updated = aggregate.updateContent(newDesc, validGalleryWithOneImage);

        assertEquals(newDesc, updated.description());
        assertEquals(initialVersion.num() + 1, updated.version().num());
    }
}
