package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.core.registry.RegistryForge;
import com.cursee.monolib.core.sailing.Sailing;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class DisEnchantingTableForge {

    public static IEventBus EVENT_BUS = null;
    
    public DisEnchantingTableForge(FMLJavaModLoadingContext context) {
        DisEnchantingTable.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        DisEnchantingTableForge.EVENT_BUS = context.getModEventBus();
        RegistryForge.register(DisEnchantingTableForge.EVENT_BUS);
    }
}