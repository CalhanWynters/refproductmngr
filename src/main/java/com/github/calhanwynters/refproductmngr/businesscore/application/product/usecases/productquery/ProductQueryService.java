package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.productquery;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.dto.ProductQueryDTO;
import com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers.ProductQueryMapper;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.BusinessIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductQueryRepository;

import java.util.List;

public class ProductQueryService {
    private final ProductQueryRepository productQueryRepository;

    public ProductQueryService(ProductQueryRepository productQueryRepository) {
        this.productQueryRepository = productQueryRepository;
    }

    public List<ProductQueryDTO> findProducts(ProductQueryDTO query) {
        // Convert DTO primitives to Domain Value Objects for the Repository
        ProductIdVO id = new ProductIdVO(query.id());
        BusinessIdVO businessId = new BusinessIdVO(query.businessId());

        return productQueryRepository.findProductByProductIdAndBusinessId(id, businessId)
                .stream()
                .map(ProductQueryMapper::toDTO)
                .toList(); // Modern Java 16+ replacement for collect(Collectors.toList())
    }
}
