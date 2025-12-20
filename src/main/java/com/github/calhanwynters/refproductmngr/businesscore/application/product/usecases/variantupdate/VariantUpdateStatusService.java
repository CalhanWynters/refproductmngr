package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantupdate;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.exceptions.ProductNotFoundException;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregate;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantStatusEnums;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VariantUpdateStatusService {
    private static final Logger logger = LoggerFactory.getLogger(VariantUpdateStatusService.class);
    private final ProductCommandRepository productCommandRepository;

    public VariantUpdateStatusService(ProductCommandRepository productCommandRepository) {
        this.productCommandRepository = productCommandRepository;
    }

    @Transactional
    public void updateVariantStatusToActive(VariantActiveCommand command) {
        // 1. Load the Aggregate via the Variant's parent
        ProductAggregate product = productCommandRepository.findByVariantId(new VariantIdVO(command.getId()))
                .orElseThrow(() -> new ProductNotFoundException("Variant not found"));

        // 2. Execute Domain Logic (The Aggregate Root handles the change)
        ProductAggregate updatedProduct = product.updateVariantStatus(new VariantIdVO(command.getId()), VariantStatusEnums.ACTIVE);

        // 3. Persist the state change
        productCommandRepository.save(updatedProduct);
    }

    @Transactional
    public void updateVariantStatusToDiscontinue(VariantDiscontinueCommand command) {
        // 1. Load the Aggregate via the Variant's parent
        ProductAggregate product = productCommandRepository.findByVariantId(new VariantIdVO(command.getId()))
                .orElseThrow(() -> new ProductNotFoundException("Variant not found"));

        // 2. Execute Domain Logic (The Aggregate Root handles the change)
        ProductAggregate updatedProduct = product.updateVariantStatus(new VariantIdVO(command.getId()), VariantStatusEnums.DISCONTINUED);

        // 3. Persist the state change
        productCommandRepository.save(updatedProduct);
    }

    @Transactional
    public void updateVariantStatusToDraft(VariantDraftCommand command) {
        // 1. Load the Aggregate via the Variant's parent
        ProductAggregate product = productCommandRepository.findByVariantId(new VariantIdVO(command.getId()))
                .orElseThrow(() -> new ProductNotFoundException("Variant not found"));

        // 2. Execute Domain Logic (The Aggregate Root handles the change)
        ProductAggregate updatedProduct = product.updateVariantStatus(new VariantIdVO(command.getId()), VariantStatusEnums.DRAFT);

        // 3. Persist the state change
        productCommandRepository.save(updatedProduct);
    }

    @Transactional
    public void updateVariantStatusToInactive(VariantInactiveCommand command) {
        // 1. Load the Aggregate via the Variant's parent
        ProductAggregate product = productCommandRepository.findByVariantId(new VariantIdVO(command.getId()))
                .orElseThrow(() -> new ProductNotFoundException("Variant not found"));

        // 2. Execute Domain Logic (The Aggregate Root handles the change)
        ProductAggregate updatedProduct = product.updateVariantStatus(new VariantIdVO(command.getId()), VariantStatusEnums.INACTIVE);

        // 3. Persist the state change
        productCommandRepository.save(updatedProduct);
    }
}

