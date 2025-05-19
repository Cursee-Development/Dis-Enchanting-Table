package com.cursee.disenchanting_table;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class DisenchantingTableForge {

    public static IEventBus EVENT_BUS;

    public DisenchantingTableForge(FMLJavaModLoadingContext context) {
        DisenchantingTable.init();
        EVENT_BUS = context.getModEventBus();
        if (FMLEnvironment.dist == Dist.CLIENT) new DisenchantingTableClientForge(EVENT_BUS);
    }

    @SuppressWarnings("removal")
    public DisenchantingTableForge() {
        this(FMLJavaModLoadingContext.get());
    }
}