package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantdelete;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteAllVariantsByProductIdService {
    private static final Logger logger = LoggerFactory.getLogger(DeleteAllVariantsByProductIdService.class);
    private final ProductCommandRepository productCommandRepository;

    public DeleteAllVariantsByProductIdService(ProductCommandRepository productCommandRepository) {
        this.productCommandRepository = productCommandRepository;
    }

    public void deleteAllVariants(DeleteAllVariantsByProductIdCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("DeleteAllVariantsByProductIdCommand cannot be null");
        }

        String productId = command.getProductId();

        // Delete all variants associated with the product
        productCommandRepository.deleteAllVariantsByProductId(new ProductIdVO(productId));

        logger.info("All variants for Product ID {} deleted successfully.", productId);
    }
}