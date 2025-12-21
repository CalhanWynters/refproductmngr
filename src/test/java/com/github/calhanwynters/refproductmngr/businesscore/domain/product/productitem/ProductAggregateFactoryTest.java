package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("ProductAggregateFactory Invariant and Reconstruction Tests")
class ProductAggregateFactoryTest {

    private BusinessIdVO validBizId;
    private CategoryVO validCategory;
    private DescriptionVO validDescription;
    private GalleryVO validGallery;

    @BeforeEach
    void setUp() {
        validBizId = BusinessIdVO.random();
        validCategory = new CategoryVO("Clothing");
        validDescription = new DescriptionVO("High-quality organic cotton t-shirt.");
        validGallery = new GalleryVO(List.of(new ImageUrlVO("https://cdn.example.com/default.jpg")));
    }

    @Nested
    @DisplayName("Aggregate Creation Invariants")
    class CreationInvariants {

        @Test
        @DisplayName("Should successfully create a valid aggregate with one variant")
        void testCreate_Success() {
            // Arrange
            VariantEntity initialVariant = createDefaultVariant("TSHIRT-NVY-L");

            // Act
            ProductAggregate product = ProductAggregateFactory.create(
                    validBizId, validCategory, validDescription, validGallery, Set.of(initialVariant));

            // Assert
            assertAll(
                    () -> assertThat(product.id()).isNotNull(),
                    () -> assertThat(product.variants()).hasSize(1),
                    // FIX: Update from isZero() to isEqualTo(1) to match your factory logic
                    () -> assertThat(product.version().num()).isEqualTo(1),
                    () -> assertThat(product.isDeleted()).isFalse()
            );
        }


        @Test
        @DisplayName("Should throw IllegalArgumentException when creating product with no variants")
        void testCreate_NoVariants_ThrowsException() {
            assertThatThrownBy(() -> ProductAggregateFactory.create(
                    validBizId, validCategory, validDescription, validGallery, Collections.emptySet()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("at least one variant");
        }
    }

    @Nested
    @DisplayName("Aggregate Reconstruction")
    class ReconstructionTests {

        @Test
        @DisplayName("Should preserve existing state during reconstruction")
        void testReconstruct_MaintainsExistingState() {
            // Arrange
            ProductIdVO existingId = ProductIdVO.generate();
            VersionVO existingVersion = new VersionVO(15);
            VariantEntity existingVariant = createDefaultVariant("OLD-SKU-001");

            // Act
            ProductAggregate reconstructed = ProductAggregateFactory.reconstruct(
                    existingId, validBizId, validCategory, validDescription, validGallery,
                    Set.of(existingVariant), existingVersion, true
            );

            // Assert
            assertThat(reconstructed.id()).isEqualTo(existingId);
            assertThat(reconstructed.version().num()).isEqualTo(15);
            assertThat(reconstructed.isDeleted()).isTrue();
        }
    }

    /**
     * Test Data Factory (Object Mother) to keep tests focused.
     */
    private VariantEntity createDefaultVariant(String sku) {
        return ProductAggregateFactory.createVariant(
                new SkuVO(sku),
                new PriceVO(new BigDecimal("25.00"), 2, Currency.getInstance("USD")),
                new PriceVO(new BigDecimal("25.00"), 2, Currency.getInstance("USD")),
                Collections.emptySet(),
                new CareInstructionVO("* Wash cold"),
                new WeightVO(new BigDecimal("0.2"), WeightUnitEnums.KILOGRAM),
                VariantStatusEnums.DRAFT
        );
    }
}