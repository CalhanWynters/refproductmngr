package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantEntity;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantStatusEnums;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ProductAggregateBehaviorTest {

    @InjectMocks
    private ProductAggregateBehavior behavior;

    @Mock private ProductAggregate mockAggregate;
    @Mock private VariantEntity mockActiveVariant;
    @Mock private VariantEntity mockInactiveVariantOne;
    @Mock private VariantEntity mockInactiveVariantTwo;
    @Mock private GalleryVO mockGalleryVO;

    // Real VOs for stateful transformation tests
    private ProductIdVO realProductId;
    private BusinessIdVO realBusinessId;
    private CategoryVO realCategory;
    private DescriptionVO realDescription;
    private VersionVO realVersion;
    private GalleryVO realGallery;
    private Set<VariantEntity> activeVariantSet;

    @BeforeEach
    void setUp() {
        // Shared Mock behavior
        lenient().when(mockAggregate.gallery()).thenReturn(mockGalleryVO);
        lenient().when(mockActiveVariant.isActive()).thenReturn(true);
        lenient().when(mockActiveVariant.status()).thenReturn(VariantStatusEnums.ACTIVE);
        lenient().when(mockInactiveVariantOne.isActive()).thenReturn(false);
        lenient().when(mockInactiveVariantOne.status()).thenReturn(VariantStatusEnums.DRAFT);

        // Setup Real Objects for 2025 invariants
        realProductId = new ProductIdVO(UUID.randomUUID().toString());
        realBusinessId = new BusinessIdVO("B2025");
        realCategory = new CategoryVO("Electronics");
        realDescription = new DescriptionVO("High-end tech product");
        realVersion = new VersionVO(1);
        realGallery = new GalleryVO(List.of(new ImageUrlVO("https://example.com/image.jpg")));
        activeVariantSet = Set.of(mockActiveVariant);
    }

    @Test
    @DisplayName("isPublishable should be false if the product is deleted, even if valid")
    void isPublishable_DeletedProduct_ReturnsFalse() {
        when(mockAggregate.isDeleted()).thenReturn(true);
        when(mockGalleryVO.images()).thenReturn(List.of(mock(ImageUrlVO.class)));
        when(mockAggregate.variants()).thenReturn(Set.of(mockActiveVariant));

        assertFalse(behavior.isPublishable(mockAggregate), "Deleted products must not be publishable in 2025");
    }

    @Test
    @DisplayName("isPublishable should be true for non-deleted product with images and active variants")
    void isPublishable_ValidActiveProduct_ReturnsTrue() {
        when(mockAggregate.isDeleted()).thenReturn(false);
        when(mockGalleryVO.images()).thenReturn(List.of(mock(ImageUrlVO.class)));
        when(mockAggregate.variants()).thenReturn(Set.of(mockActiveVariant));

        assertTrue(behavior.isPublishable(mockAggregate));
    }

    @Test
    @DisplayName("hasActiveVariants should return false immediately if aggregate is deleted")
    void hasActiveVariants_DeletedAggregate_ShortCircuitsToFalse() {
        when(mockAggregate.isDeleted()).thenReturn(true);
        // Even if the set contains an active variant
        when(mockAggregate.variants()).thenReturn(Set.of(mockActiveVariant));

        assertFalse(behavior.hasActiveVariants(mockAggregate));
    }

    @Test
    @DisplayName("updateDescription should preserve isDeleted status when creating new instance")
    void updateDescription_PreservesDeletedStatus() {
        // ARRANGE: A deleted product
        ProductAggregate deletedProduct = new ProductAggregate(
                realProductId, realBusinessId, realCategory, realDescription,
                realGallery, activeVariantSet, realVersion, true
        );
        DescriptionVO nextDesc = new DescriptionVO("This is a valid product text.");

        // ACT
        ProductAggregate updated = behavior.updateDescription(deletedProduct, nextDesc);

        // ASSERT
        assertTrue(updated.isDeleted(), "Updating description should not implicitly restore a product");
        assertEquals(2, updated.version().num());
    }

    @Test
    @DisplayName("restoreWithNewDescription should set isDeleted to false and increment version")
    void restoreWithNewDescription_RestoresAndUpdates() {
        // ARRANGE: A deleted product at version 5
        ProductAggregate deletedProduct = new ProductAggregate(
                realProductId, realBusinessId, realCategory, realDescription,
                realGallery, activeVariantSet, new VersionVO(5), true
        );
        DescriptionVO newDesc = new DescriptionVO("Restored description");

        // ACT
        ProductAggregate restored = behavior.restoreWithNewDescription(deletedProduct, newDesc);

        // ASSERT
        assertFalse(restored.isDeleted(), "Product should be restored (isDeleted = false)");
        assertEquals(newDesc, restored.description());
        assertEquals(6, restored.version().num(), "Version should increment exactly once");
    }

    @Test
    @DisplayName("restoreWithNewDescription should act like updateDescription if already active")
    void restoreWithNewDescription_AlreadyActive_JustUpdates() {
        ProductAggregate activeProduct = new ProductAggregate(
                realProductId, realBusinessId, realCategory, realDescription,
                realGallery, activeVariantSet, realVersion, false
        );

        ProductAggregate result = behavior.restoreWithNewDescription(activeProduct, new DescriptionVO("This is a valid product text."));

        assertFalse(result.isDeleted());
        assertEquals(2, result.version().num());
    }
}
