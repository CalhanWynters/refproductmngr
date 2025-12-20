package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantupdate;

public class VariantDiscontinueCommand {
    private final String id;

    public VariantDiscontinueCommand (String id) {
        this.id = id;
    }

    public String getId() { return id; }
}
