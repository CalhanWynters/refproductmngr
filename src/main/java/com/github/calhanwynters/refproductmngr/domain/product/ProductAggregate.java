package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.*;
// Added necessary import


/*** Aggregate Root representing a Product in the domain.
 * An immutable record that controls access to its internal components
 * and enforces business invariants.
 */
public record ProductAggregate(
        ProductIdVO id,
        BusinessIdVO businessIdVO,
        String category,
        DescriptionVO description,
        GalleryVO gallery,
        Set<VariantEntity> variants
) {
    public ProductAggregate {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(businessIdVO, "businessId must not be null");
        Objects.requireNonNull(category, "category must not be null");
        Objects.requireNonNull(description, "featureDescription must not be null");
        Objects.requireNonNull(gallery, "gallery must not be null");
        Objects.requireNonNull(variants, "variants must not be null");
        // Ensure the internal collection is deeply immutable
        variants = Set.copyOf(variants);
    }

    // --- Factory Methods ---

    /**
     * Updated Factory method: Now accepts initial variants as the generic interface
     * and requires the category and businessId to be passed in.
     */
    public static ProductAggregate create(
            BusinessIdVO businessIdVO,
            String category,
            DescriptionVO description,
            GalleryVO gallery,
            Set<VariantEntity> initialVariants
    ) {
        // Updated constructor call with businessId as the second argument:
        return new ProductAggregate(
                ProductIdVO.generate(),
                businessIdVO,
                category,
                description,
                gallery,
                initialVariants
        );
    }

    /**
     * Factory method for creating an item with no variants initially,
     * requires the category and businessId to be passed in.
     */
    public static ProductAggregate create(
            BusinessIdVO businessIdVO, // <-- Requires BusinessId here too
            String category,
            DescriptionVO description,
            GalleryVO gallery
    ) {
        // Delegates to the main create method, passing the new businessId argument:
        return create(businessIdVO, category, description, gallery, Collections.emptySet());
    }

    // --- Behavior Methods ---

    public ProductAggregate changeDescription(DescriptionVO newDescription) {
        // Corrected to return a new instance with the updated description
        return new ProductAggregate(this.id, this.businessIdVO, this.category, newDescription, this.gallery, this.variants);
    }

    public ProductAggregate addImage(ImageUrlVO newImageUrl) {
        Set<ImageUrlVO> updatedImages = new HashSet<>(this.gallery.images());
        updatedImages.add(newImageUrl);
        GalleryVO updatedGallery = new GalleryVO(updatedImages);
        // Corrected to use the updated gallery
        return new ProductAggregate(this.id, this.businessIdVO, this.category, this.description, updatedGallery, this.variants);
    }

    /*** Adds a new variant to the Product.
     * @param newVariant The variant to add (can be any type that implements the interface).
     * @return A new Product instance with the added variant.
     */
    public ProductAggregate addVariant(VariantEntity newVariant) {
        Objects.requireNonNull(newVariant, "newVariant must not be null");

        // FIX 1: Use the public getter getId() instead of direct field access id()
        boolean idAlreadyExists = this.variants.stream()
                .anyMatch(v -> v.getId().equals(newVariant.getId()));

        if (idAlreadyExists) {
            // FIX 2: Use the public getter getId() instead of direct field access id()
            throw new VariantAlreadyExistsException("Variant with ID " + newVariant.getId().value() + " already exists.");
        }

        Set<VariantEntity> updatedVariants = new HashSet<>(this.variants);
        updatedVariants.add(newVariant);

        // Ensure the returned set is immutable
        return new ProductAggregate(this.id, this.businessIdVO, this.category, this.description, this.gallery, Set.copyOf(updatedVariants));
    }


    /*** Finds a variant by its ID.
     * @param variantIdVO The ID to search for.
     * @return An Optional containing the variant, if found within this aggregate.
     */
    public Optional<VariantEntity> findVariantById(VariantIdVO variantIdVO) {
        // FIX 3: Use the public getter getId() instead of direct field access id()
        return this.variants.stream().filter(v -> v.getId().equals(variantIdVO)).findFirst();
    }
}
