package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantdelete;


import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.exceptions.VariantNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VariantDeleteService {
    private static final Logger logger = LoggerFactory.getLogger(VariantDeleteService.class);
    private final ProductCommandRepository productCommandRepository;

    public VariantDeleteService(ProductCommandRepository productCommandRepository) {
        this.productCommandRepository = productCommandRepository;
    }

    public void deleteVariant(VariantDeleteCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("VariantDeleteCommand cannot be null");
        }

        String variantId = command.getVariantId();
        boolean deleted = productCommandRepository.deleteVariantById(variantId);

        if (!deleted) {
            logger.warn("Attempted to delete non-existent variant with ID {}.", variantId);
            throw new VariantNotFoundException("Variant not found for deletion");
        }

        logger.info("Variant with ID {} deleted successfully.", variantId);
    }
}
