package com.github.calhanwynters.refproductmngr.businesscore.domain.product;


import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.BusinessIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregate;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;

import java.util.List;
import java.util.Optional;


public interface ProductQueryRepository {
    // Return type changed to Optional to reflect a unique lookup
    Optional<ProductAggregate> findProductByProductIdAndBusinessId(ProductIdVO id, BusinessIdVO businessId);
}
