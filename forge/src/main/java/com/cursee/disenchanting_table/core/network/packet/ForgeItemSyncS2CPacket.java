package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.client.network.packet.ForgeConfigSyncClientHandler;
import com.cursee.disenchanting_table.client.network.packet.ForgeItemSyncClientHandler;
import com.cursee.disenchanting_table.core.registry.ForgeNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Supplier;

public class ForgeItemSyncS2CPacket implements CustomPacketPayload {

    public final BlockPos blockPosition;
    public final int size;
    public final NonNullList<ItemStack> inventory;

    public ForgeItemSyncS2CPacket(BlockPos blockPos, int size, NonNullList<ItemStack> inventory) {
        this.blockPosition = blockPos;
        this.size = size;
        this.inventory = inventory;
    }

    public ForgeItemSyncS2CPacket(RegistryFriendlyByteBuf data) {
        this.blockPosition = data.readBlockPos();
        this.size = data.readInt();
        this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
        for (int i = 0; i < this.size; i++) {
            this.inventory.set(i, new ItemStack(BuiltInRegistries.ITEM.get(data.readResourceLocation())));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ForgeNetwork.ITEM_SYNC_ID;
    }

    public void toBytes(RegistryFriendlyByteBuf data) {
        data.writeBlockPos(this.blockPosition);
        data.writeInt(this.size);
        for (int i = 0; i < this.size; i++) {
            data.writeResourceLocation(BuiltInRegistries.ITEM.getKey(this.inventory.get(i).getItem()));
        }
    }

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeItemSyncClientHandler.handle(this, context));
        });
        context.setPacketHandled(true);
    }

//    private final NonNullList<ItemStack> items;
//    private final BlockPos pos;
//
//    public ForgeItemSyncS2CPacket(NonNullList<ItemStack> items, BlockPos pos) {
//        this.items = items;
//        this.pos = pos;
//    }
//
//    public ForgeItemSyncS2CPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
//    }
//
//    public void encode(FriendlyByteBuf data) {
//        data.writeInt(this.items.size());
//        this.items.forEach(data::writeItem);
//        data.writeBlockPos(this.pos);
//    }
//
//    public static ForgeItemSyncS2CPacket decode(FriendlyByteBuf data) {
//
//        int size = data.readInt();
//
//        NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
//        for (int i = 0; i < size; i++) {
//            items.set(i, data.readItem());
//        }
//
//        BlockPos position = data.readBlockPos();
//
//        return new ForgeItemSyncS2CPacket(items, position);
//    }
//
//    public static void handle(ForgeItemSyncS2CPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
//        contextSupplier.get().enqueueWork(
//            () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeItemSyncClientHandler.handleOnClient(packet, contextSupplier))
//        );
//        contextSupplier.get().setPacketHandled(true);
//    }
//
//    public NonNullList<ItemStack> getItems() {
//        return items;
//    }
//
//    public BlockPos getPos() {
//        return pos;
//    }
}
