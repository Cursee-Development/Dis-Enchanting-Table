package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.block.entity.renderer.HopperDisEnchantingTableBER;
import com.cursee.disenchanting_table.core.network.ModNetworkFabric;
import com.cursee.disenchanting_table.core.registry.ModBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class DisEnchantingTableClientFabric implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        DisEnchantingTableClient.init();
        BlockEntityRenderers.register(ModBlockEntities.HOPPER_DISENCHANTING_TABLE, HopperDisEnchantingTableBER::new);
        ModNetworkFabric.registerS2CPackets();
    }
}
