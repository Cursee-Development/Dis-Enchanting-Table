package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.ForgeClientConfigHandler;
import com.cursee.disenchanting_table.client.block.entity.renderer.ForgeDisEnchantingBER;
import com.cursee.disenchanting_table.client.gui.screens.AutoDisEnchantingScreen;
import com.cursee.disenchanting_table.client.gui.screens.ManualDisEnchantingScreen;
import com.cursee.disenchanting_table.core.registry.ForgeBlockEntities;
import com.cursee.disenchanting_table.core.registry.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class DisEnchantingTableClientNeoForge {

    @EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
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

        @SubscribeEvent
        public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenus.MANUAL_DISENCHANTING_TABLE, ManualDisEnchantingScreen::new);
            event.register(ModMenus.AUTO_DISENCHANTING_TABLE, AutoDisEnchantingScreen::new);
        }
    }
}
