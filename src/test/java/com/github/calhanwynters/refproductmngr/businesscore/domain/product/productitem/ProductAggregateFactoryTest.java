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

    // Helper fields to ensure valid inputs for VOs
    private BusinessIdVO validBusinessId;
    private CategoryVO validCategory;
    private DescriptionVO validDescription;
    private GalleryVO validGallery;
    private VersionVO validVersion;
    private Set<VariantEntity> emptyVariants;
    private Set<VariantEntity> initialVariantsSet;

    @BeforeEach
    void setUp() {
        // Initialize valid, real VOs
        validBusinessId = new BusinessIdVO("B456");
        validCategory = new CategoryVO("C789");
        validDescription = new DescriptionVO("Initial Description");

        // GalleryVO requires at least one image URL now
        List<ImageUrlVO> images = List.of(new ImageUrlVO("https://example.com/img1.jpg"));
        validGallery = new GalleryVO(images);

        validVersion = new VersionVO(1);
        emptyVariants = Collections.emptySet();

        // Prepare a set with initial variants (using a mock for simplicity of variant entity)
        VariantEntity mockVariant = mock(VariantEntity.class);
        initialVariantsSet = Set.of(mockVariant);
    }

    // --- Test create(businessIdVO, value, text, gallery, num, initialVariants) ---

    @Test
    @DisplayName("Create with initial variants should return a valid ProductAggregate with all fields set")
    void createWithVariants_ValidInputs_CreatesAggregate() {
        // Act
        ProductAggregate aggregate = ProductAggregateFactory.create(
                validBusinessId,
                validCategory,
                validDescription,
                validGallery,
                validVersion,
                initialVariantsSet
        );

        // Assert
        assertNotNull(aggregate);
        assertNotNull(aggregate.id(), "Factory should generate a unique ProductIdVO");
        assertEquals(validBusinessId, aggregate.businessIdVO());
        assertEquals(validCategory, aggregate.category());
        assertEquals(validDescription, aggregate.description());
        assertEquals(validGallery, aggregate.gallery());
        assertEquals(validVersion, aggregate.version());
        assertFalse(aggregate.variants().isEmpty(), "Aggregate should contain initial variants");
        assertEquals(1, aggregate.variants().size());
    }

    @Test
    @DisplayName("Generated ProductIdVOs should be unique across multiple factory calls")
    void createWithVariants_MultipleCalls_UniqueIds() {
        // Act
        ProductAggregate aggregate1 = ProductAggregateFactory.create(
                validBusinessId, validCategory, validDescription, validGallery, validVersion, emptyVariants
        );
        ProductAggregate aggregate2 = ProductAggregateFactory.create(
                validBusinessId, validCategory, validDescription, validGallery, validVersion, emptyVariants
        );

        // Assert
        assertNotEquals(aggregate1.id(), aggregate2.id(), "Factory must generate unique UUID-based IDs");
    }


    // --- Test create(businessIdVO, value, text, gallery, num) (Overload without variants) ---

    @Test
    @DisplayName("Create without explicit variants should default to an empty variants set")
    void createWithoutVariants_ValidInputs_CreatesAggregateWithEmptyVariants() {
        // Act
        ProductAggregate aggregate = ProductAggregateFactory.create(
                validBusinessId,
                validCategory,
                validDescription,
                validGallery,
                validVersion
        );

        // Assert
        assertNotNull(aggregate);
        assertTrue(aggregate.variants().isEmpty(), "Aggregate variants set should be empty by default");
    }

    // --- Test input validation (relies on underlying VO/Aggregate constraints) ---

    @Test
    @DisplayName("Factory should propagate exceptions if a mandatory input is null (e.g., BusinessIdVO)")
    void create_NullBusinessId_ThrowsNPE() {
        assertThrows(NullPointerException.class, () -> ProductAggregateFactory.create(
                null, // Null input
                validCategory,
                validDescription,
                validGallery,
                validVersion,
                emptyVariants
        ));
    }
}
