package com.cursee.examplemod;

import com.cursee.examplemod.core.registry.ModRegistryForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class ExampleModForge {

    public static IEventBus EVENT_BUS;

    public ExampleModForge(FMLJavaModLoadingContext context) {
        ExampleMod.init();
        EVENT_BUS = context.getModEventBus();
        ModRegistryForge.register(EVENT_BUS);
        if (FMLEnvironment.dist == Dist.CLIENT) new ExampleModClientForge(EVENT_BUS);
    }

    @SuppressWarnings("removal")
    public ExampleModForge() {
        this(FMLJavaModLoadingContext.get());
    }
}