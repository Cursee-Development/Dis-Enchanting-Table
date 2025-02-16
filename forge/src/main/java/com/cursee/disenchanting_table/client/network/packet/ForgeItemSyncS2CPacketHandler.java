package com.cursee.disenchanting_table.client.network.packet;

import com.cursee.disenchanting_table.core.network.packet.ForgeItemSyncPacket;
import com.cursee.disenchanting_table.core.world.block.entity.HopperDisEnchantingTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeItemSyncS2CPacketHandler {
    public static void handleOnClient(ForgeItemSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {

            Minecraft client = Minecraft.getInstance();
            if (client.level == null) return;

            BlockEntity blockEntity = client.level.getBlockEntity(packet.getPos());
            if (blockEntity instanceof HopperDisEnchantingTableBlockEntity disenchantingTable) {
                disenchantingTable.setInventory(packet.getItems());
            }
        });
    }
}
