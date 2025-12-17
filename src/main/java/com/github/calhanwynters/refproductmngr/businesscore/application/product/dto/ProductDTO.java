package com.github.calhanwynters.refproductmngr.businesscore.application.product.dto;

import java.util.List;
import java.util.Set;

/***
 * Data Transfer Object representing a Product.
 */
public record ProductDTO(
        String id,
        String businessId,
        String category,
        String description,
        List<String> imageUrls,     // List preserves gallery order (Primary image is first)
        Set<VariantDTO> variants,
        int version,
        boolean isPublishable
) {}