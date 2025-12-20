package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.productdelete;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.exceptions.ProductNotFoundException;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.BusinessIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductDeleteService {
    private static final Logger logger = LoggerFactory.getLogger(ProductDeleteService.class);
    private final ProductCommandRepository productCommandRepository;

    public ProductDeleteService(ProductCommandRepository productCommandRepository) {
        this.productCommandRepository = productCommandRepository;
    }

    /**
     * SOFT DELETE: The Pure DDD way.
     * Loads the aggregate, tells it to soft delete, then saves the state.
     */
    @Transactional
    public void softDeleteProduct(ProductDeleteCommand command) {
        ProductIdVO productId = new ProductIdVO(command.getId());

        // 1. Load the Aggregate (Consistency Boundary)
        ProductAggregate product = productCommandRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // 2. Domain Logic: State transition
        ProductAggregate deletedProduct = product.softDelete();

        // 3. Persist (Triggers the PRODUCT_UPDATED/DELETED outbox event)
        productCommandRepository.save(deletedProduct);

        logger.info("Product {} soft-deleted successfully.", productId.value());
    }

    /**
     * HARD DELETE: Physical removal.
     * Usually keeps the 'Direct' repository call because the aggregate is being destroyed.
     */
    @Transactional
    public void hardDeleteProduct(ProductDeleteCommand command) {
        ProductIdVO productId = new ProductIdVO(command.getId());
        BusinessIdVO businessId = new BusinessIdVO(command.getBusinessId());

        // Step 1: Clean up child entities (Physical delete)
        productCommandRepository.deleteAllVariantsByProductId(productId);

        // Step 2: Physical delete of the root
        boolean deleted = productCommandRepository.deleteProductByProductIdAndBusinessId(productId, businessId);

        if (!deleted) {
            throw new ProductNotFoundException("Product not found for hard deletion");
        }

        logger.info("Product {} and variants physically removed from system.", productId.value());
    }
}
