package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.productcreate;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers.FeatureMapper;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.FeatureAbstractClass;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductCreateService {
    private final ProductCommandRepository productRepository;

    public ProductCreateService(ProductCommandRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void execute(ProductCreateCommand command) {
        // 1. Map Features using the polymorphic FeatureMapper
        Set<FeatureAbstractClass> featureEntities = command.features().stream()
                .map(FeatureMapper::toEntity)
                .collect(Collectors.toSet());

        // 2. Build Initial Variant via the "Domain Entry" Factory Method
        // This hides the VO instantiation logic from the Application Service.
        VariantEntity initialVariant = ProductAggregateFactory.createVariant(
                command.initialVariantSku(),
                command.initialPrice(),
                command.initialPrice(), // Current price starts same as base
                command.currencyCode(),
                command.weightValue(),   // Ensure this is BigDecimal for 2025 precision
                command.weightUnit(),
                "Default instructions", // Consider moving this to a domain constant
                VariantStatusEnums.DRAFT.name(),
                featureEntities
        );

        // 3. Assemble Aggregate via Factory
        ProductAggregate product = ProductAggregateFactory.create(
                new BusinessIdVO(command.businessId()),
                new CategoryVO(command.category()),
                new DescriptionVO(command.description()),
                new GalleryVO(command.imageUrls().stream().map(ImageUrlVO::new).toList()),
                Set.of(initialVariant)
        );

        productRepository.save(product);
    }
}

