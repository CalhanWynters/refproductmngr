package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantdelete;

public class DeleteAllVariantsByProductIdCommand {
    private final String productId;

    public DeleteAllVariantsByProductIdCommand(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
