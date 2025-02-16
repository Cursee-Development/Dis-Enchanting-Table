package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.client.network.packet.ForgeItemSyncS2CPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeItemSyncPacket {

    private final NonNullList<ItemStack> items;
    private final BlockPos pos;

    public ForgeItemSyncPacket(NonNullList<ItemStack> items, BlockPos pos) {
        this.items = items;
        this.pos = pos;
    }

    public void encode(FriendlyByteBuf data) {
        data.writeInt(this.items.size());
        this.items.forEach(data::writeItem);
        data.writeBlockPos(this.pos);
    }

    public static ForgeItemSyncPacket decode(FriendlyByteBuf data) {

        int size = data.readInt();

        NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
        for (int i = 0; i < size; i++) {
            items.set(i, data.readItem());
        }

        BlockPos position = data.readBlockPos();

        return new ForgeItemSyncPacket(items, position);
    }

    public static void handle(ForgeItemSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(
            () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeItemSyncS2CPacketHandler.handleOnClient(packet, contextSupplier))
        );
        contextSupplier.get().setPacketHandled(true);
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public BlockPos getPos() {
        return pos;
    }
}
