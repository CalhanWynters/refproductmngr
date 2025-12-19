package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.productdelete;

public class ProductDeleteCommand {
    private final String id;
    private final String businessId;

    public ProductDeleteCommand(String id, String businessId) {
        this.id = id;
        this.businessId = businessId;
    }

    public String getId() {
        return id;
    }

    public String getBusinessId() {
        return businessId;
    }
}