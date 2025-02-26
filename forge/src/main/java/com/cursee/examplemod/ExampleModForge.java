package com.cursee.examplemod;

import com.cursee.examplemod.core.registry.RegistryForge;
import com.cursee.monolib.core.sailing.Sailing;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class ExampleModForge {

    public static IEventBus EVENT_BUS = null;
    
    public ExampleModForge(FMLJavaModLoadingContext context) {
        ExampleMod.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        ExampleModForge.EVENT_BUS = context.getModEventBus();
        RegistryForge.register(ExampleModForge.EVENT_BUS);
    }
}