package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.productdelete;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.exceptions.ProductNotFoundException;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.BusinessIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductDeleteService {
    private static final Logger logger = LoggerFactory.getLogger(ProductDeleteService.class);
    private final ProductCommandRepository productCommandRepository;

    public ProductDeleteService(ProductCommandRepository productCommandRepository) {
        this.productCommandRepository = productCommandRepository;
    }

    public void deleteProduct(ProductDeleteCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("ProductDeleteCommand cannot be null");
        }

        // Constructing Value Objects
        ProductIdVO productId = new ProductIdVO(command.getId());
        BusinessIdVO businessId = new BusinessIdVO(command.getBusinessId());

        boolean deleted = productCommandRepository.deleteProductByProductIdAndBusinessId(productId, businessId);

        if (!deleted) {
            logger.warn("Attempted to delete non-existent product with ID {} for Business {}.", productId.value(), businessId.value());
            throw new ProductNotFoundException("Product not found for deletion");
        }

        logger.info("Product with ID {} for Business {} deleted successfully.", productId.value(), businessId.value());
        // Optionally publish a ProductDeletedEvent here
    }
}