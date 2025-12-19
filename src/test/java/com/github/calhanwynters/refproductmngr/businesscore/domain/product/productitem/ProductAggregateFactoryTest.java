package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ProductAggregateFactoryTest {

    private BusinessIdVO validBusinessId;
    private CategoryVO validCategory;
    private DescriptionVO validDescription;
    private GalleryVO validGallery;
    private Set<VariantEntity> initialVariantsSet;

    @BeforeEach
    void setUp() {
        validBusinessId = new BusinessIdVO("B456");
        validCategory = new CategoryVO("C789");
        validDescription = new DescriptionVO("Initial Description");

        // GalleryVO requires at least one image URL per 2025 constraints
        List<ImageUrlVO> images = List.of(new ImageUrlVO("https://example.com/img1.jpg"));
        validGallery = new GalleryVO(images);

        // Prepare a set with initial variants (using a mock for simplicity)
        VariantEntity mockVariant = mock(VariantEntity.class);
        initialVariantsSet = Set.of(mockVariant);
    }

    @Test
    @DisplayName("Create should return a valid ProductAggregate with all fields set and version 0")
    void create_ValidInputs_CreatesAggregate() {
        // Act
        ProductAggregate aggregate = ProductAggregateFactory.create(
                validBusinessId,
                validCategory,
                validDescription,
                validGallery,
                initialVariantsSet
        );

        // Assert
        assertNotNull(aggregate);
        assertNotNull(aggregate.id(), "Factory should generate a unique ProductIdVO");
        assertEquals(validBusinessId, aggregate.businessIdVO());
        assertEquals(validCategory, aggregate.category());
        assertEquals(validDescription, aggregate.description());
        assertEquals(validGallery, aggregate.gallery());
        assertEquals(0, aggregate.version().num(), "New products must start at version 0");
        assertFalse(aggregate.isDeleted(), "New products should not be marked as deleted");
        assertEquals(1, aggregate.variants().size());
    }

    @Test
    @DisplayName("Create must throw IllegalArgumentException if initial variants are empty (2025 Invariant)")
    void create_EmptyVariants_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ProductAggregateFactory.create(
                        validBusinessId,
                        validCategory,
                        validDescription,
                        validGallery,
                        Collections.emptySet()
                )
        );
        assertTrue(exception.getMessage().contains("must have at least one initial variant"));
    }

    @Test
    @DisplayName("Reconstruct should restore all fields exactly as provided from persistence")
    void reconstruct_ValidInputs_RestoresAggregate() {
        // Arrange
        ProductIdVO existingId = ProductIdVO.generate();
        VersionVO existingVersion = new VersionVO(5);
        boolean isDeleted = true;

        // Act
        ProductAggregate aggregate = ProductAggregateFactory.reconstruct(
                existingId,
                validBusinessId,
                validCategory,
                validDescription,
                validGallery,
                initialVariantsSet,
                existingVersion,
                isDeleted
        );

        // Assert
        assertEquals(existingId, aggregate.id());
        assertEquals(existingVersion, aggregate.version());
        assertTrue(aggregate.isDeleted());
        assertEquals(initialVariantsSet, aggregate.variants());
    }

    @Test
    @DisplayName("Generated ProductIdVOs should be unique across multiple factory calls")
    void create_MultipleCalls_UniqueIds() {
        ProductAggregate aggregate1 = ProductAggregateFactory.create(validBusinessId, validCategory, validDescription, validGallery, initialVariantsSet);
        ProductAggregate aggregate2 = ProductAggregateFactory.create(validBusinessId, validCategory, validDescription, validGallery, initialVariantsSet);

        assertNotEquals(aggregate1.id(), aggregate2.id(), "Factory must generate unique UUID-based IDs");
    }

    @Test
    @DisplayName("Factory should propagate NullPointerException if mandatory BusinessIdVO is null")
    void create_NullBusinessId_ThrowsNPE() {
        assertThrows(NullPointerException.class, () ->
                ProductAggregateFactory.create(null, validCategory, validDescription, validGallery, initialVariantsSet)
        );
    }
}
