package com.cursee.disenchanting_table.client.network.packet;

import com.cursee.disenchanting_table.core.network.packet.NeoForgeItemSyncS2CPacket;
import com.cursee.disenchanting_table.core.world.block.entity.ForgeDisEnchantingBE;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
// import net.minecraftforge.event.network.CustomPayloadEvent;

public class NeoForgeItemSyncClientHandler {

    public static void handle(NeoForgeItemSyncS2CPacket packet, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;
        BlockEntity blockEntity = client.level.getBlockEntity(BlockPos.of(packet.blockPos()));
        if (blockEntity instanceof ForgeDisEnchantingBE disenchantingTable) {
            // disenchantingTable.setInventory(packet.inventory);
            disenchantingTable.setItem(2, new ItemStack(packet.displayItem()));
        }
    }
//    public static void handleOnClient(ForgeItemSyncS2CPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
//        contextSupplier.get().enqueueWork(() -> {
//
//            Minecraft client = Minecraft.getInstance();
//            if (client.level == null) return;
//
//            BlockEntity blockEntity = client.level.getBlockEntity(packet.getPos());
//            if (blockEntity instanceof ForgeDisEnchantingBE disenchantingTable) {
//                disenchantingTable.setInventory(packet.getItems());
//            }
//        });
//    }
}
