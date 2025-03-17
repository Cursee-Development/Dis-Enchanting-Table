package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.ForgeCommonConfigHandler;
import com.cursee.disenchanting_table.core.network.packet.NeoForgeConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.registry.RegistryNeoForge;
import com.cursee.monolib.core.sailing.Sailing;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class DisEnchantingTableNeoForge {

    public static IEventBus EVENT_BUS = null;

    public DisEnchantingTableNeoForge(IEventBus modEventBus) {
        DisEnchantingTable.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        DisEnchantingTableNeoForge.EVENT_BUS = modEventBus;
        RegistryNeoForge.register(DisEnchantingTableNeoForge.EVENT_BUS);

        EVENT_BUS.addListener((Consumer<FMLCommonSetupEvent>) event -> {
            event.enqueueWork(() -> {
                ForgeCommonConfigHandler.onLoad();
                // ForgeNetwork.registerS2CPackets();
            });
        });

        NeoForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
            if (!(event.getEntity() instanceof ServerPlayer player)) return;
            // ForgeNetwork.sendToPlayer(new ForgeConfigSyncS2CPacket(CommonConfigValues.automatic_disenchanting, CommonConfigValues.resets_repair_cost, CommonConfigValues.requires_experience, CommonConfigValues.uses_points, CommonConfigValues.experience_cost), player);
            PacketDistributor.sendToPlayer(player, new NeoForgeConfigSyncS2CPacket(CommonConfigValues.automatic_disenchanting, CommonConfigValues.resets_repair_cost, CommonConfigValues.requires_experience, CommonConfigValues.uses_points, CommonConfigValues.experience_cost));
        });
    }
}