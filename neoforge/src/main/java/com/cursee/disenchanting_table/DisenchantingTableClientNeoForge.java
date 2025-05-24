package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.gui.screens.DisenchantingMenuScreen;
import com.cursee.disenchanting_table.client.gui.screens.DisenchantingTableScreen;
import com.cursee.disenchanting_table.client.renderer.blockentity.DisenchantingTableRenderer;
import com.cursee.disenchanting_table.core.registry.ModBlockEntities;
import com.cursee.disenchanting_table.core.registry.ModMenus;
import com.cursee.disenchanting_table.core.world.inventory.DisenchantingMenu;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.function.Consumer;

public class DisenchantingTableClientNeoForge {

    public DisenchantingTableClientNeoForge(final IEventBus modEventBus) {

        modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> {
            event.enqueueWork(() -> {
                DisenchantingTableClient.init();
            });
        });

        modEventBus.addListener((Consumer<RegisterMenuScreensEvent>) event -> {
            event.register(ModMenus.DISENCHANTING_MENU, DisenchantingMenuScreen::new);
            event.register(ModMenus.DISENCHANTING_TABLE, DisenchantingTableScreen::new);
        });

        modEventBus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> {
            event.registerBlockEntityRenderer(ModBlockEntities.DISENCHANTING_TABLE, DisenchantingTableRenderer::new);
        });
    }
}
