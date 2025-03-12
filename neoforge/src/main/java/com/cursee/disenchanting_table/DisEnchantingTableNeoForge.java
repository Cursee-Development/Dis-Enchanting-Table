package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.core.registry.RegistryNeoForge;
import com.cursee.monolib.core.sailing.Sailing;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class DisEnchantingTableNeoForge {

    public static IEventBus EVENT_BUS = null;

    public DisEnchantingTableNeoForge(IEventBus modEventBus) {
        DisEnchantingTable.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        DisEnchantingTableNeoForge.EVENT_BUS = modEventBus;
        RegistryNeoForge.register(DisEnchantingTableNeoForge.EVENT_BUS);
    }
}