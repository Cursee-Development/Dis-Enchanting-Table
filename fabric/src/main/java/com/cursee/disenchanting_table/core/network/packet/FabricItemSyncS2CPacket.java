package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.client.network.packet.FabricItemSyncClientHandler;
import com.cursee.disenchanting_table.core.network.FabricNetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class FabricItemSyncS2CPacket implements CustomPacketPayload {

    public final BlockPos blockPosition;
    public final int size;
    public final NonNullList<ItemStack> inventory;

    public FabricItemSyncS2CPacket(BlockPos blockPos, int size, NonNullList<ItemStack> inventory) {
        this.blockPosition = blockPos;
        this.size = size;
        this.inventory = inventory;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return FabricNetwork.Packets.ITEM_SYNC_ID;
    }

    public void write(RegistryFriendlyByteBuf data) {
        // data.writeResourceLocation(BuiltInRegistries.ITEM.getKey(this.inventory.getItem()));
        data.writeBlockPos(this.blockPosition);
        data.writeInt(this.size);
        for (ItemStack stack : this.inventory) {
            data.writeResourceLocation(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        }
    }

    public static FabricItemSyncS2CPacket read(RegistryFriendlyByteBuf data) {

        BlockPos blockPosition = data.readBlockPos();
        int size = data.readInt();
        NonNullList<ItemStack> createdInventory = NonNullList.withSize(size, ItemStack.EMPTY);
        for (int i = 0; i < size; i++) {
            ResourceLocation location = data.readResourceLocation();
            Optional<Holder.Reference<Item>> optionalReference = BuiltInRegistries.ITEM.get(data.readResourceLocation());
            if (optionalReference.isPresent()) createdInventory.set(i, new ItemStack(optionalReference.get()));
            else {
                throw new IllegalStateException("Item not found in registry: " + location);
            }
        }

        return new FabricItemSyncS2CPacket(blockPosition, size, createdInventory);
    }

    public void handle(ClientPlayNetworking.Context context) {
        FabricItemSyncClientHandler.handle(this, context);
    }
}

///**
// * <ol>
// *     <li>Server encodes and sends a new instance of {@link FriendlyByteBuf}</li>
// *     <li>Client receives the {@link FriendlyByteBuf}, and handles the data</li>
// * </ol>
// */
//public class FabricItemSyncS2CPacket {
//
//    public static void createAndSend(Entity entity, NonNullList<ItemStack> inventory, BlockPos pos) {
//        if (!(entity instanceof ServerPlayer player)) return;
//        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
//        data.writeInt(inventory.size());
//        for (ItemStack stack : inventory) {
//            data.writeItem(stack);
//        }
//        data.writeBlockPos(pos);
//        FabricNetwork.sendToPlayer(data, player, FabricNetwork.Packets.ITEM_SYNC_S2C);
//    }
//}
