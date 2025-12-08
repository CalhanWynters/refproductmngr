package com.github.calhanwynters.refproductmngr;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModularityTests {
    static ApplicationModules modules = ApplicationModules.of(RefproductmngrApplication.class);

    @Test
    void verifiesModularStructure() {
        modules.verify();
    }
}
