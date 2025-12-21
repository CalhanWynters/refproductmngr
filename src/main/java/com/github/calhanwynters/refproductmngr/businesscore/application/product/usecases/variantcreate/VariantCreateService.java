package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantcreate;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.VariantDTO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.exceptions.ProductNotFoundException;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregate;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregateFactory;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VariantCreateService {
    private final ProductCommandRepository productCommandRepository;

    public VariantCreateService(ProductCommandRepository productCommandRepository) {
        this.productCommandRepository = productCommandRepository;
    }

    @Transactional
    public void execute(String productId, VariantDTO dto) {
        // 1. Load the Aggregate
        ProductAggregate product = productCommandRepository.findById(new ProductIdVO(productId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        // 2. Build the Variant via Factory
        // Use Set.of() instead of null to be explicit and avoid the redundancy warning
        VariantEntity newVariant = ProductAggregateFactory.createVariant(
                dto.sku(),
                dto.basePrice(),
                dto.currentPrice(),
                dto.currencyCode(),
                dto.weightValue(), // Ensure Factory signature is BigDecimal
                dto.weightUnit(),
                dto.careInstructions(),
                dto.status(),
                java.util.Set.of()
        );

        // 3. Mutate and Save
        product.addVariant(newVariant);
        productCommandRepository.save(product);
    }

}
