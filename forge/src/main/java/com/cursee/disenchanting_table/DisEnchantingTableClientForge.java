package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.ClientConfigForge;
import com.cursee.disenchanting_table.client.block.entity.renderer.HopperDisEnchantingTableBER;
import com.cursee.disenchanting_table.core.network.ModNetworkForge;
import com.cursee.disenchanting_table.core.registry.ModBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.function.Consumer;

public class DisEnchantingTableClientForge {

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModClientBusEvents {

        @SubscribeEvent
        public static void onClientSetup(final FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                DisEnchantingTableClient.init();
                ClientConfigForge.onClientLoaded();
            });
        }

        @SubscribeEvent
        public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.HOPPER_DISENCHANTING_TABLE, HopperDisEnchantingTableBER::new);
        }
    }
}
