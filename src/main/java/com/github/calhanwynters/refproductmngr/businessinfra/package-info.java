@org.springframework.modulith.ApplicationModule(
        id = "business-infra",
        allowedDependencies = {
            "business-core :: bc-domain-product", "business-core :: bc-api"
        }
)
package com.github.calhanwynters.refproductmngr.businessinfra;