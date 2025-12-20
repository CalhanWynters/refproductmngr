package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.productcreate;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.common.DescriptionVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.*;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.variant.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Set;

@Service
public class ProductCreateService {
    private final ProductCommandRepository productRepository;

    public ProductCreateService(ProductCommandRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void execute(ProductCreateCommand command) {
        // 1. Create Value Objects (Validation happens here)
        ProductIdVO productId = ProductIdVO.generate();
        BusinessIdVO businessId = new BusinessIdVO(command.businessId());
        CategoryVO category = new CategoryVO(command.category());
        DescriptionVO description = new DescriptionVO(command.description());

        List<ImageUrlVO> imageUrls = command.imageUrls().stream()
                .map(ImageUrlVO::new)
                .toList();
        GalleryVO gallery = new GalleryVO(imageUrls);

        // 2. Create the Initial Variant (DDD: Product must have at least 1 variant)
        VariantEntity initialVariant = new VariantEntity(
                VariantIdVO.generate(),
                new SkuVO(command.initialVariantSku()),
                new PriceVO(command.initialPrice(), 2, Currency.getInstance(command.currencyCode())),
                new PriceVO(command.initialPrice(), 2, Currency.getInstance(command.currencyCode())),
                Collections.emptySet(),
                new CareInstructionVO("Default instructions"),
                new WeightVO(BigDecimal.ZERO, WeightUnitEnums.KILOGRAM),
                VariantStatusEnums.DRAFT
        );

        // 3. Instantiate the Aggregate Root
        ProductAggregate product = new ProductAggregate(
                productId,
                businessId,
                category,
                description,
                gallery,
                Set.of(initialVariant),
                new VersionVO(1), // Initial version
                false // isDeleted = false
        );

        // 4. Persist (Triggers INSERT and Outbox Snapshot)
        productRepository.save(product);
    }
}
