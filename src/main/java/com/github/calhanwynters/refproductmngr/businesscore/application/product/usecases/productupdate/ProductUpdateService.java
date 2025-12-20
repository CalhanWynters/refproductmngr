package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.productupdate;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.exceptions.ProductNotFoundException;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.CategoryVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregate;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductUpdateService {
    private final ProductCommandRepository productRepository;

    public ProductUpdateService(ProductCommandRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void execute(ProductUpdateCommand command) {
        // 1. Load (Infrastructure)
        ProductAggregate product = productRepository.findById(new ProductIdVO(command.productId()))
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // 2. Modify (Domain Behavior)
        ProductAggregate updatedProduct = product.updateBasicInfo(
                new DescriptionVO(command.description()),
                new CategoryVO(command.categoryId())
        );

        // 3. Save (Infrastructure + Outbox)
        productRepository.save(updatedProduct);
    }
}
