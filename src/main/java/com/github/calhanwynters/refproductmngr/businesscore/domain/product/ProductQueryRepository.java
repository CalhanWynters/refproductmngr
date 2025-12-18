package com.github.calhanwynters.refproductmngr.businesscore.domain.product;


import java.util.List;


public interface ProductQueryRepository {

    List<ProductAggregate> findProductByProductIdAndBusinessId(ProductIdVO id, BusinessIdVO businessId);
    // Additional methods as necessary

}
