package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.core.world.block.entity.HopperDisEnchantingTableBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FabricItemSyncS2CPacket {

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender responseSender) {
        int size = data.readInt();
        NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);
        for(int i = 0; i < size; i++) {
            list.set(i, data.readItem());
        }
        BlockPos position = data.readBlockPos();

        if (client.level == null) return;
        BlockEntity blockEntity = client.level.getBlockEntity(position);
        if(blockEntity instanceof HopperDisEnchantingTableBlockEntity blockEntityX) {
            blockEntityX.setInventory(list);
        }
    }
}
