package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantupdate;

public class VariantInactiveCommand {
    private final String id;

    public VariantInactiveCommand (String id) {
        this.id = id;
    }

    public String getId() { return id; }
}
