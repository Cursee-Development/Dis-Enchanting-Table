package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.renderer.blockentity.DisenchantingTableRenderer;
import com.cursee.disenchanting_table.core.registry.ModBlockEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Consumer;

public class DisenchantingTableClientForge {

    DisenchantingTableClientForge(final IEventBus modEventBus) {
        modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> {
            event.enqueueWork(() -> {
                DisenchantingTableClient.init();
            });
        });

        modEventBus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> {
            event.registerBlockEntityRenderer(ModBlockEntities.DISENCHANTING_TABLE, DisenchantingTableRenderer::new);
        });
    }
}
