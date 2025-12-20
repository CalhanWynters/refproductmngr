package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantcreate;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.VariantDTO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.exceptions.ProductNotFoundException;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregate;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

@Service
public class VariantCreateService {
    private final ProductCommandRepository productCommandRepository;

    public VariantCreateService(ProductCommandRepository productCommandRepository) {
        this.productCommandRepository = productCommandRepository;
    }

    @Transactional
    public void execute(String productId, VariantDTO dto) {
        // 1. Load
        ProductAggregate product = productCommandRepository.findById(new ProductIdVO(productId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // 2. Map DTO to Entity
        VariantEntity newVariant = new VariantEntity(
                VariantIdVO.generate(),
                new SkuVO(dto.sku()),
                new PriceVO(dto.basePrice(), 2, Currency.getInstance(dto.currencyCode())),
                new PriceVO(dto.currentPrice(), 2, Currency.getInstance(dto.currencyCode())),
                new java.util.HashSet<>(),
                new CareInstructionVO(dto.careInstructions()),
                new WeightVO(dto.weightValue(), WeightUnitEnums.valueOf(dto.weightUnit())),
                VariantStatusEnums.valueOf(dto.status())
        );

        // 3. Modify (Delegates to Aggregate Root)
        ProductAggregate updatedProduct = product.addVariant(newVariant);

        // 4. Save (Triggers DB updates and Outbox Snapshot)
        productCommandRepository.save(updatedProduct);
    }

    /**
     * Extracts the mapping and aggregate reconstruction logic.
     * Keeps the original ProductAggregate file untouched.
     */
    private ProductAggregate createUpdatedProductWithNewVariant(ProductAggregate product, VariantDTO dto) {
        // Build the new entity
        VariantEntity newVariant = new VariantEntity(
                VariantIdVO.generate(),
                new SkuVO(dto.sku()),
                new PriceVO(dto.basePrice(), 2, Currency.getInstance(dto.currencyCode())),
                new PriceVO(dto.currentPrice(), 2, Currency.getInstance(dto.currencyCode())),
                new HashSet<>(),
                new CareInstructionVO(dto.careInstructions()),
                new WeightVO(dto.weightValue(), WeightUnitEnums.valueOf(dto.weightUnit())),
                VariantStatusEnums.valueOf(dto.status())
        );

        // Manage the collection
        Set<VariantEntity> updatedVariants = new HashSet<>(product.variants());
        updatedVariants.add(newVariant);

        // Reconstruct the immutable Aggregate
        return new ProductAggregate(
                product.id(),
                product.businessIdVO(),
                product.category(),
                product.description(),
                product.gallery(),
                updatedVariants,
                product.version(),
                product.isDeleted()
        );
    }
}
