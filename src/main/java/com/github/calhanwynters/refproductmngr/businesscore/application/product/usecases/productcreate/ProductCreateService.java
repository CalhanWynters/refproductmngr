package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.productcreate;

import com.github.calhanwynters.refproductmngr.businesscore.application.product.mappers.FeatureMapper;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.feature.FeatureAbstractClass;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Currency;
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
        // 1. Map Features
        Set<FeatureAbstractClass> featureEntities = command.features().stream()
                .map(FeatureMapper::toEntity)
                .collect(Collectors.toSet());

        // 2. Build Initial Variant via Factory
        VariantEntity initialVariant = ProductAggregateFactory.createVariant(
                new SkuVO(command.initialVariantSku()),
                new PriceVO(command.initialPrice(), 2, Currency.getInstance(command.currencyCode())),
                new PriceVO(command.initialPrice(), 2, Currency.getInstance(command.currencyCode())),
                featureEntities,
                new CareInstructionVO("Default instructions"),
                new WeightVO(command.weightValue(), WeightUnitEnums.valueOf(command.weightUnit())),
                VariantStatusEnums.DRAFT
        );

        // 3. Assemble Aggregate via Factory
        // We pass the business context values.
        // The Factory will decide what the starting VersionVO(num) should be.
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
