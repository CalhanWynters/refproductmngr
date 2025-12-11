package com.github.calhanwynters.refproductmngr.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Set strictness to LENIENT for the whole class to stop UnnecessaryStubbing exceptions
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ProductAggregateBehaviorTest {

    @InjectMocks
    private ProductAggregateBehavior behavior;

    // We will use these Mocks for the state-checking methods (isPublishable, etc.)
    @Mock
    private ProductAggregate mockAggregate;
    @Mock
    private VariantEntity mockActiveVariant;
    @Mock
    private VariantEntity mockInactiveVariantOne; // Renamed to ensure uniqueness
    @Mock
    private VariantEntity mockInactiveVariantTwo; // Added a second unique mock for sets
    @Mock
    private GalleryVO mockGalleryVO;
    @Mock
    private DescriptionVO mockDescriptionVO;
    @Mock
    private VersionVO mockVersionVO;
    @Mock
    private ProductIdVO mockProductIdVO;
    @Mock
    private BusinessIdVO mockBusinessIdVO;
    @Mock
    private CategoryVO mockCategoryVO;

    // Helper fields to create *real* VOs for the updateDescription test cases
    private ProductIdVO realProductId;
    private BusinessIdVO realBusinessId;
    private CategoryVO realCategory;
    private DescriptionVO realDescription;
    private VersionVO realVersion;
    private GalleryVO realGallery;

    @BeforeEach
    void setUp() {
        // --- Setup for Mocks (used in state-checking tests) ---
        lenient().when(mockAggregate.id()).thenReturn(mockProductIdVO);
        lenient().when(mockAggregate.businessIdVO()).thenReturn(mockBusinessIdVO);
        lenient().when(mockAggregate.category()).thenReturn(mockCategoryVO);
        lenient().when(mockAggregate.gallery()).thenReturn(mockGalleryVO);
        lenient().when(mockAggregate.variants()).thenReturn(Collections.emptySet());
        lenient().when(mockAggregate.version()).thenReturn(mockVersionVO);
        lenient().when(mockAggregate.description()).thenReturn(mockDescriptionVO);

        // Configure specific variant behavior (used widely)
        when(mockActiveVariant.isActive()).thenReturn(true);
        when(mockActiveVariant.getStatus()).thenReturn(VariantStatusEnums.ACTIVE);

        when(mockInactiveVariantOne.isActive()).thenReturn(false);
        when(mockInactiveVariantOne.getStatus()).thenReturn(VariantStatusEnums.DRAFT);

        when(mockInactiveVariantTwo.isActive()).thenReturn(false);
        when(mockInactiveVariantTwo.getStatus()).thenReturn(VariantStatusEnums.DRAFT);

        // --- Setup for Real Objects (used in updateDescription tests) ---
        realProductId = new ProductIdVO(UUID.randomUUID().toString());
        realBusinessId = new BusinessIdVO("B456");
        realCategory = new CategoryVO("C789");
        realDescription = new DescriptionVO("Initial Description");
        realVersion = new VersionVO(1);
        List<ImageUrlVO> images = List.of(new ImageUrlVO("https://example.com/valid-image-1.jpg"));
        realGallery = new GalleryVO(images);
    }

    @Test
    @DisplayName("isPublishable should be true when both images and active variants exist")
    void isPublishable_ValidConditions_ReturnsTrue() {
        when(mockGalleryVO.images()).thenReturn(List.of(mock(ImageUrlVO.class)));
        when(mockAggregate.variants()).thenReturn(Set.of(mockActiveVariant));
        boolean result = behavior.isPublishable(mockAggregate);
        assertTrue(result);
    }

    @Test
    @DisplayName("isPublishable should be false when no active variants exist")
    void isPublishable_NoActiveVariants_ReturnsFalse() {
        when(mockGalleryVO.images()).thenReturn(List.of(mock(ImageUrlVO.class)));
        when(mockAggregate.variants()).thenReturn(Set.of(mockInactiveVariantOne)); // Use unique mock
        boolean result = behavior.isPublishable(mockAggregate);
        assertFalse(result);
    }

    @Test
    @DisplayName("hasMinimumImages should be false if the gallery image list is empty")
    void hasMinimumImages_EmptyGallery_ReturnsFalse() {
        when(mockGalleryVO.images()).thenReturn(Collections.emptyList());
        boolean result = behavior.hasMinimumImages(mockAggregate);
        assertFalse(result);
    }

    @Test
    @DisplayName("allVariantsAreDraft should be true if all variants are DRAFT status")
    void allVariantsAreDraft_AllDrafts_ReturnsTrue() {
        // FIX: Use two *unique* mock instances in the Set
        Set<VariantEntity> allDrafts = Set.of(mockInactiveVariantOne, mockInactiveVariantTwo);
        when(mockAggregate.variants()).thenReturn(allDrafts);
        boolean result = behavior.allVariantsAreDraft(mockAggregate);
        assertTrue(result);
    }

    @Test
    @DisplayName("allVariantsAreDraft should be true if there are no variants (empty set)")
    void allVariantsAreDraft_EmptyVariants_ReturnsTrue() {
        // This relies on the lenient default stubbing in setUp
        boolean result = behavior.allVariantsAreDraft(mockAggregate);
        assertTrue(result);
    }

    @Test
    @DisplayName("allVariantsAreDraft should be false if at least one variant is not DRAFT (e.g., ACTIVE)")
    void allVariantsAreDraft_MixedStatus_ReturnsFalse() {
        // FIX: Use unique mocks
        Set<VariantEntity> mixedStatus = Set.of(mockInactiveVariantOne, mockActiveVariant);
        when(mockAggregate.variants()).thenReturn(mixedStatus);
        boolean result = behavior.allVariantsAreDraft(mockAggregate);
        assertFalse(result);
    }

    @Test
    @DisplayName("updateDescription should return a new aggregate with updated description and incremented version")
    void updateDescription_NewDescription_ReturnsNewAggregateWithUpdatedFields() {
        // ARRANGE: Use *real* objects here (Fixes MissingMethodInvocationException)
        ProductAggregate currentRealAggregate = new ProductAggregate(
                realProductId, realBusinessId, realCategory, realDescription,
                realGallery, Collections.emptySet(), realVersion
        );
        DescriptionVO newDescription = new DescriptionVO("Updated description");

        // ACT
        ProductAggregate updatedAggregate = behavior.updateDescription(currentRealAggregate, newDescription);

        // ASSERT
        assertNotNull(updatedAggregate);
        assertNotSame(currentRealAggregate, updatedAggregate, "Should return a new instance (immutability pattern)");
        assertEquals(newDescription, updatedAggregate.description());
        // FIX: Use .version() accessor for the record field (fixes Cannot Resolve Method error)
        assertEquals(2, updatedAggregate.version().version(), "Version should be incremented from 1 to 2");
        assertEquals(currentRealAggregate.id(), updatedAggregate.id(), "ID should remain the same");
    }

    @Test
    @DisplayName("updateDescription should return the same aggregate instance if description is identical")
    void updateDescription_SameDescription_ReturnsSameInstance() {
        // ARRANGE: Use *real* objects here (Fixes MissingMethodInvocationException)
        ProductAggregate currentRealAggregate = new ProductAggregate(
                realProductId, realBusinessId, realCategory, realDescription,
                realGallery, Collections.emptySet(), realVersion
        );
        DescriptionVO sameDescription = realDescription;

        // ACT
        ProductAggregate updatedAggregate = behavior.updateDescription(currentRealAggregate, sameDescription);

        // ASSERT
        assertSame(currentRealAggregate, updatedAggregate, "Should return the exact same instance if no change is needed");
        // FIX: Use .version() accessor for the record field
        assertEquals(1, updatedAggregate.version().version(), "Version should not increment if input is the same");
    }
}
