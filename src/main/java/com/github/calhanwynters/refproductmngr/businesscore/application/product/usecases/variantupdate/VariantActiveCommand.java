package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantupdate;

public class VariantActiveCommand {
    private final String id;

    public VariantActiveCommand (String id) {
        this.id = id;
    }

    public String getId() { return id; }
}
