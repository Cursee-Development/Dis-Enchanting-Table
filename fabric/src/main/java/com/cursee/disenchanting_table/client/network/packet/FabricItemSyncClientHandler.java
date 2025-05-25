package com.cursee.disenchanting_table.client.network.packet;

import com.cursee.disenchanting_table.core.network.packet.FabricItemSyncS2CPacket;
import com.cursee.disenchanting_table.core.world.block.entity.DisenchantingTableBlockEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FabricItemSyncClientHandler {

//    public static void registerS2CPacketHandler(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender responseSender) {
//        int size = data.readInt();
//        NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);
//        for(int i = 0; i < size; i++) {
//            list.set(i, data.readItem());
//        }
//        BlockPos position = data.readBlockPos();
//
//        if (client.level == null) return;
//        BlockEntity blockEntity = client.level.getBlockEntity(position);
//        if(blockEntity instanceof FabricDisEnchantingBE blockEntityX) {
//            blockEntityX.setInventory(list);
//        }
//    }

    public static void handle(FabricItemSyncS2CPacket packet, ClientPlayNetworking.Context context) {
        Minecraft client = context.client();

        if (client.level == null) return;

        BlockEntity blockEntity = client.level.getBlockEntity(packet.blockPosition);
        if (blockEntity instanceof DisenchantingTableBlockEntity table) {
            table.setInventory(packet.inventory);
        }
    }
}
