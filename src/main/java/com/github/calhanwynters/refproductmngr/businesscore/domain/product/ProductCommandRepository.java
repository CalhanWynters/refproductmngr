package com.github.calhanwynters.refproductmngr.businesscore.domain.product;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.BusinessIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregate;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.VariantIdVO;

import java.util.Optional;

public interface ProductCommandRepository {
    boolean deleteProductByProductIdAndBusinessId(ProductIdVO id, BusinessIdVO businessId);
    boolean deleteVariantById(String variantId);
    void deleteAllVariantsByProductId(ProductIdVO id);
    Optional<ProductAggregate> findByVariantId(VariantIdVO variantId);

    // Persists the entire state of the Aggregate Root
    void save(ProductAggregate product);


    Optional<ProductAggregate> findById(ProductIdVO id);
}