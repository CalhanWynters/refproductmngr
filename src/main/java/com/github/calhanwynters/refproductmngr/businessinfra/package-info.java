@org.springframework.modulith.ApplicationModule(
        id = "business-infra",
        allowedDependencies = {
                "business-core",               // Dependency on the main module
                "business-core::bc-api",
                "business-core::bc-domain-product", // Dependency on a specific Named Interface
                "business-core::bc-domain-product-common",
                "business-core::bc-domain-product-feature",
                "business-core::bc-domain-product-productitem",
                "business-core::bc-domain-product-variant"
        }
)
package com.github.calhanwynters.refproductmngr.businessinfra;
