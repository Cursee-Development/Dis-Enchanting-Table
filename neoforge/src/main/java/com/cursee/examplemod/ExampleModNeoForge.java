package com.cursee.examplemod;

import com.cursee.examplemod.core.registry.RegistryNeoForge;
import com.cursee.monolib.core.sailing.Sailing;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ExampleModNeoForge {

    public static IEventBus EVENT_BUS = null;

    public ExampleModNeoForge(IEventBus modEventBus) {
        ExampleMod.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        ExampleModNeoForge.EVENT_BUS = modEventBus;
        RegistryNeoForge.register(ExampleModNeoForge.EVENT_BUS);
    }
}