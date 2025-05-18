package com.cursee.examplemod;

import com.cursee.examplemod.core.registry.ModRegistryFabric;
import net.fabricmc.api.ModInitializer;

public class ExampleModFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ExampleMod.init();
        ModRegistryFabric.register();
    }
}
