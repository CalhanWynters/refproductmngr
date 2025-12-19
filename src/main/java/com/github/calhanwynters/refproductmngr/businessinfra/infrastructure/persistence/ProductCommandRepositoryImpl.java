package com.github.calhanwynters.refproductmngr.businessinfra.infrastructure.persistence;

import com.github.calhanwynters.refproductmngr.businesscore.domain.product.ProductCommandRepository;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.BusinessIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem.ProductIdVO;
import com.github.calhanwynters.refproductmngr.businesscore.domain.product.exceptions.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class ProductCommandRepositoryImpl implements ProductCommandRepository {
    private static final Logger logger = LoggerFactory.getLogger(ProductCommandRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductCommandRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean deleteProductByProductIdAndBusinessId(ProductIdVO id, BusinessIdVO businessId) {
        String sql = "DELETE FROM products WHERE id = ? AND business_id_vo = ?";

        try {
            int deletedCount = jdbcTemplate.update(sql, id.value(), businessId.value());

            if (deletedCount > 0) {
                logger.info("Product with ID {} for Business {} deleted successfully.", id.value(), businessId.value());
                return true; // Deletion successful
            } else {
                logger.warn("No product found for ID {} and Business {} to delete.", id.value(), businessId.value());
                return false; // No product found
            }
        } catch (Exception e) {
            logger.error("Error while attempting to delete product with ID {} and Business {}: {}",
                    id.value(), businessId.value(), e.getMessage());
            throw new ProductNotFoundException("Failed to delete product", e);
        }
    }

}
