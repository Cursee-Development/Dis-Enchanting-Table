package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.core.network.NeoForgeNetwork;
import com.cursee.disenchanting_table.core.network.packet.NeoForgeConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.registry.ModRegistryNeoForge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class DisenchantingTableNeoForge {

    public static IEventBus EVENT_BUS;

    public DisenchantingTableNeoForge(final FMLModContainer container) {
        DisenchantingTable.init();
        EVENT_BUS = container.getEventBus();
        ModRegistryNeoForge.register(EVENT_BUS);
        if (FMLEnvironment.dist == Dist.CLIENT) new DisenchantingTableClientNeoForge(EVENT_BUS);
        NeoForge.EVENT_BUS.addListener(DisenchantingTableServerNeoForge::new);

        NeoForgeNetwork.init();
        NeoForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> NeoForgeConfigSyncS2CPacket.createAndSend(event.getEntity(), event.getLevel()));
    }
}