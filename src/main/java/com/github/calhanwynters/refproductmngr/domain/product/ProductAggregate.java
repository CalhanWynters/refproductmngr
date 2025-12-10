package com.github.calhanwynters.refproductmngr.domain.product;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/***
 * Aggregate Root representing a Product in the domain.
 * An immutable record that controls access to its internal components
 * and enforces business invariants.
 */
public record ProductAggregate(
        ProductIdVO id,
        BusinessIdVO businessIdVO,
        CategoryVO category,
        DescriptionVO description,
        GalleryVO gallery,
        Set<VariantEntity> variants,
        VersionVO version
) {
    public ProductAggregate {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(businessIdVO, "businessId must not be null");
        Objects.requireNonNull(category, "category must not be null");
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(gallery, "gallery must not be null");
        Objects.requireNonNull(variants, "variants must not be null");
        Objects.requireNonNull(version, "version must not be null");
        // Ensure the internal collection is deeply immutable
        variants = Set.copyOf(variants);
    }

    // --- Behavior Methods ---

    public ProductAggregate changeDescription(DescriptionVO newDescription) {
        return new ProductAggregate(this.id, this.businessIdVO, this.category, newDescription, this.gallery, this.variants, this.version);
    }

    public ProductAggregate addImage(ImageUrlVO newImageUrl) {
        Set<ImageUrlVO> updatedImages = new HashSet<>(this.gallery.images());
        updatedImages.add(newImageUrl);
        GalleryVO updatedGallery = new GalleryVO(updatedImages);
        return new ProductAggregate(this.id, this.businessIdVO, this.category, this.description, updatedGallery, this.variants, this.version);
    }

    public ProductAggregate removeImage(ImageUrlVO imageUrl) {
        Objects.requireNonNull(imageUrl, "imageUrl must not be null");

        Set<ImageUrlVO> updatedImages = new HashSet<>(this.gallery.images());

        // Attempt to remove the image
        boolean removed = updatedImages.remove(imageUrl);

        // If the image was not found, throw an exception
        if (!removed) {
            throw new ImageNotFoundException("Image URL " + imageUrl.url() + " not found in the gallery."); // Use url() method
        }

        GalleryVO updatedGallery = new GalleryVO(updatedImages);

        return new ProductAggregate(this.id, this.businessIdVO, this.category, this.description, updatedGallery, this.variants, this.version);
    }



    /***
     * Adds a new variant to the Product.
     * @param newVariant The variant to add.
     * @return A new ProductAggregate instance with the added variant.
     */
    public ProductAggregate addVariant(VariantEntity newVariant) {
        Objects.requireNonNull(newVariant, "newVariant must not be null");

        boolean idAlreadyExists = this.variants.stream()
                .anyMatch(v -> v.getId().equals(newVariant.getId()));

        if (idAlreadyExists) {
            throw new VariantAlreadyExistsException("Variant with ID " + newVariant.getId().value() + " already exists.");
        }

        Set<VariantEntity> updatedVariants = new HashSet<>(this.variants);
        updatedVariants.add(newVariant);

        return new ProductAggregate(this.id, this.businessIdVO, this.category, this.description, this.gallery, Set.copyOf(updatedVariants), this.version);
    }

    // --- Validation Methods ---


}
