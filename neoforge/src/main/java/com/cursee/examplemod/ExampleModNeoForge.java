package com.cursee.examplemod;


import com.cursee.examplemod.core.registry.ModRegistryNeoForge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class ExampleModNeoForge {

    public static IEventBus EVENT_BUS;

    public ExampleModNeoForge(final FMLModContainer container) {
        ExampleMod.init();
        EVENT_BUS = container.getEventBus();
        ModRegistryNeoForge.register(EVENT_BUS);
        if (FMLEnvironment.dist == Dist.CLIENT) new ExampleModClientNeoForge(EVENT_BUS);
    }
}