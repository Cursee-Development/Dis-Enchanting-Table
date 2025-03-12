package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.ForgeClientConfigHandler;
import com.cursee.disenchanting_table.client.block.entity.renderer.ForgeDisEnchantingBER;
import com.cursee.disenchanting_table.core.registry.ForgeBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DisEnchantingTableClientForge {

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModClientBusEvents {

        @SubscribeEvent
        public static void onClientSetup(final FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                DisEnchantingTableClient.init();
                ForgeClientConfigHandler.onLoad();
            });
        }

        @SubscribeEvent
        public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ForgeBlockEntities.DISENCHANTING_TABLE, ForgeDisEnchantingBER::new);
        }
    }
}
