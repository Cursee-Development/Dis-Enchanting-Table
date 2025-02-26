package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.ForgeCommonConfigHandler;
import com.cursee.disenchanting_table.core.network.packet.ForgeConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.registry.ForgeNetwork;
import com.cursee.disenchanting_table.core.registry.RegistryForge;
import com.cursee.monolib.core.sailing.Sailing;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class DisEnchantingTableForge {

    public static IEventBus EVENT_BUS = null;
    
    public DisEnchantingTableForge() {
        DisEnchantingTable.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);

        EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        RegistryForge.register(EVENT_BUS);

        EVENT_BUS.addListener((Consumer<FMLCommonSetupEvent>) event -> {
            event.enqueueWork(() -> {
                ForgeCommonConfigHandler.onLoad();
                ForgeNetwork.registerS2CPackets();
            });
        });

        MinecraftForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
            if (!(event.getEntity() instanceof ServerPlayer player)) return;
            ForgeNetwork.sendToPlayer(new ForgeConfigSyncS2CPacket(CommonConfigValues.automatic_disenchanting), player);
        });
    }
}