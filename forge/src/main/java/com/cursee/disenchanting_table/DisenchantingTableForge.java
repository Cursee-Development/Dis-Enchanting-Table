package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.core.network.ForgeNetwork;
import com.cursee.disenchanting_table.core.network.packet.ForgeConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.registry.ModRegistryForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class DisenchantingTableForge {

    public static IEventBus EVENT_BUS;

    public DisenchantingTableForge(FMLJavaModLoadingContext context) {
        DisenchantingTable.init();
        EVENT_BUS = context.getModEventBus();
        ModRegistryForge.register(EVENT_BUS);
        if (FMLEnvironment.dist == Dist.CLIENT) new DisenchantingTableClientForge(EVENT_BUS);
        MinecraftForge.EVENT_BUS.addListener(DisenchantingTableServerForge::new);

        ForgeNetwork.init();
        MinecraftForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> ForgeConfigSyncS2CPacket.createAndSend(event.getEntity(), event.getLevel()));
    }

    @SuppressWarnings("all")
    public DisenchantingTableForge() {
        this(FMLJavaModLoadingContext.get());
    }
}