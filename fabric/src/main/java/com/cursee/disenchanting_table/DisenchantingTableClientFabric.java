package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.renderer.blockentity.DisenchantingTableRenderer;
import com.cursee.disenchanting_table.core.registry.ModBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class DisenchantingTableClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        DisenchantingTableClient.init();

        BlockEntityRenderers.register(ModBlockEntities.DISENCHANTING_TABLE, DisenchantingTableRenderer::new);
    }
}
