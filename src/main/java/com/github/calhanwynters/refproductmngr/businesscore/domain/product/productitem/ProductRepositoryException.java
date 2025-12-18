package com.github.calhanwynters.refproductmngr.businesscore.domain.product.productitem;

/**
 * Custom exception for errors occurring in the Product Repository.
 */
public class ProductRepositoryException extends RuntimeException {

  public ProductRepositoryException(String message) {
    super(message);
  }

  public ProductRepositoryException(String message, Throwable cause) {
    super(message, cause);
  }
}