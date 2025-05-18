package com.cursee.examplemod;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(Constants.MOD_ID)
public class ExampleModForge {

    public static IEventBus EVENT_BUS;

    public ExampleModForge(FMLJavaModLoadingContext context) {
        ExampleMod.init();
        EVENT_BUS = context.getModEventBus();
        if (FMLEnvironment.dist == Dist.CLIENT) new ExampleModClientForge(EVENT_BUS);
    }

    @SuppressWarnings("removal")
    public ExampleModForge() {
        this(FMLJavaModLoadingContext.get());
    }
}