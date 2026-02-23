package com.nashtechglobal.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * Spring Modulith verification tests.
 * 
 * This test ensures that:
 * 1. Module boundaries are respected (no illegal dependencies)
 * 2. Internal packages (.internal) are not accessed from other modules
 * 3. Only public API classes can be used across module boundaries
 */
class ModularityTests {
    
    static ApplicationModules modules = ApplicationModules.of(Application.class);

    @Test
    void verifiesModularStructure() {
        // This will fail if:
        // - A module accesses another module's .internal package
        // - There are circular dependencies between modules
        // - Module naming conventions are violated
        modules.verify();
    }

    @Test
    void documentModules() {
        // Optional: Generate documentation about the module structure
        // This creates diagrams and documentation in target/spring-modulith-docs
        new Documenter(modules)
            .writeDocumentation()
            .writeIndividualModulesAsPlantUml();
    }
    
    @Test
    void ensureOrdersInternalIsHidden() {
        // Explicitly verify that the orders.internal package
        // is only accessible within the orders module
        modules.verify();
        
        // Additional verification: ensure InventoryService doesn't depend on OrderValidator
        modules.getModuleByName("inventory")
            .ifPresent(inventoryModule -> {
                inventoryModule.verifyDependencies(modules);
            });
    }
}
