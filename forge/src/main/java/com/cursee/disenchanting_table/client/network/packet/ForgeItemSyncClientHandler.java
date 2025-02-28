package com.cursee.disenchanting_table.client.network.packet;

import com.cursee.disenchanting_table.core.network.packet.ForgeItemSyncS2CPacket;
import com.cursee.disenchanting_table.core.world.block.entity.ForgeDisEnchantingBE;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeItemSyncClientHandler {
    public static void handleOnClient(ForgeItemSyncS2CPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {

            Minecraft client = Minecraft.getInstance();
            if (client.level == null) return;

            BlockEntity blockEntity = client.level.getBlockEntity(packet.getPos());
            if (blockEntity instanceof ForgeDisEnchantingBE disenchantingTable) {
                disenchantingTable.setInventory(packet.getItems());
            }
        });
    }
}
