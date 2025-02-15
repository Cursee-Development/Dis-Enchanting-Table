package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.core.world.block.entity.HopperDisEnchantingTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NewForgeItemSyncPacketClientHandler {
    public static void handleOnClient(NewForgeItemSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
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
