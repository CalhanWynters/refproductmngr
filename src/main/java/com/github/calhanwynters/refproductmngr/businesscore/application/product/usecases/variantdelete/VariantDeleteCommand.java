package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantdelete;

public class VariantDeleteCommand {
    private final String variantId;

    public VariantDeleteCommand(String variantId) {
        this.variantId = variantId;
    }

    public String getVariantId() {
        return variantId;
    }
}
