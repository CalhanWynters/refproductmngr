package com.github.calhanwynters.refproductmngr.businesscore.application.product.usecases.variantupdate;

public class VariantDraftCommand {
    private final String id;

    public VariantDraftCommand (String id) {
        this.id = id;
    }

    public String getId() { return id; }
}
