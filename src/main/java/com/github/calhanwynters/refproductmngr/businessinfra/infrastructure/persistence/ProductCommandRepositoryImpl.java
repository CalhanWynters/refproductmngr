package com.github.calhanwynters.refproductmngr.businessinfra.infrastructure.persistence;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.BusinessIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductAggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ProductCommandRepositoryImpl implements ProductCommandRepository {
    private final MongoTemplate mongoTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ProductCommandRepositoryImpl.class);

    public ProductCommandRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean deleteProductByProductIdAndBusinessId(ProductIdVO id, BusinessIdVO businessId) {
        Query query = new Query()
                .addCriteria(Criteria.where("id").is(id.value())
                        .and("businessIdVO").is(businessId.value()));

        try {
            // Attempt to perform the delete operation
            var result = mongoTemplate.remove(query, ProductAggregate.class);

            // Check if any document was deleted
            if (result.getDeletedCount() > 0) {
                logger.info("Product with ID {} for Business {} deleted successfully.", id.value(), businessId.value());
                return true; // Deletion successful
            } else {
                logger.warn("No product found for ID {} and Business {} to delete.", id.value(), businessId.value());
                return false; // No product found
            }
        } catch (Exception e) {
            logger.error("Error while attempting to delete product with ID {} and Business {}: {}",
                    id.value(), businessId.value(), e.getMessage());
            throw new RuntimeException("Failed to delete product", e);
        }
    }
}
