package com.github.calhanwynters.refproductmngr.businessinfra.infrastructure.persistence;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.BusinessIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductAggregate;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductQueryRepository;

import java.util.List;

public class ProductRepositoryImpl implements ProductQueryRepository {
    @Override
    public List<ProductAggregate> findProductByProductIdAndBusinessId(ProductIdVO id, BusinessIdVO businessId) {
        // Query the database and return a list of ProductAggregate
        // Plan for MongoDB
        return List.of(); // PLACEHOLDER
    }
}
