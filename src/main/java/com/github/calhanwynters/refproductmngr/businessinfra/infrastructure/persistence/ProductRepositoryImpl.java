package com.github.calhanwynters.refproductmngr.businessinfra.infrastructure.persistence;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductQueryRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.jspecify.annotations.NonNull;
import java.util.Optional;
import java.util.Objects;

@Repository
public class ProductRepositoryImpl implements ProductQueryRepository {

    private final MongoTemplate mongoTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ProductRepositoryImpl.class);

    public ProductRepositoryImpl(@NonNull MongoTemplate mongoTemplate) {
        this.mongoTemplate = Objects.requireNonNull(mongoTemplate);
    }

    @Override
    public @NonNull Optional<ProductAggregate> findProductByProductIdAndBusinessId(
            @NonNull ProductIdVO id,
            @NonNull BusinessIdVO businessId) {

        try {
            // Using Fluent API (2025 Best Practice)
            // FIELD MAPPING:
            // "id" matches the record component: ProductIdVO id
            // "businessIdVO" matches the record component: BusinessIdVO businessIdVO
            ProductAggregate result = mongoTemplate.query(ProductAggregate.class)
                    .matching(Query.query(
                            Criteria.where("id").is(id)
                                    .and("businessIdVO").is(businessId)
                    ))
                    .oneValue();

            return Optional.ofNullable(result);

        } catch (Exception e) {
            logger.error("Failed lookup for ID {} and Business {}: {}",
                    id.value(),
                    businessId.value(),
                    e.getMessage(),
                    e);

            // FIX: Throw the specific exception type expected by your test
            throw new ProductRepositoryException("Storage access failure", e);
        }
    }
}
