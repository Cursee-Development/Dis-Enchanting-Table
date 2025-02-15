package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.core.world.block.entity.HopperDisEnchantingTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.function.Supplier;

public class ForgeItemSyncPacket {

    public int size;
    public NonNullList<ItemStack> items;
    public BlockPos pos;
    public ForgeItemSyncPacket(int size, NonNullList<ItemStack> items, BlockPos pos) {
        this.size = size;
        this.items = items;
        this.pos = pos;
    }

    public void encode(FriendlyByteBuf data) {
        data.writeInt(this.size);
        for (ItemStack item : this.items) {
            data.writeItem(item);
        }
        data.writeBlockPos(this.pos);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            NonNullList<ItemStack> list = this.items;
            BlockPos position = this.pos;

            if(Minecraft.getInstance().level.getBlockEntity(position) instanceof HopperDisEnchantingTableBlockEntity blockEntity) {
                blockEntity.setInventory(list);
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }

    public static ForgeItemSyncPacket decode(FriendlyByteBuf data) {
        int size = data.readInt();
        NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);
        for(int i = 0; i < size; i++) {
            list.set(i, data.readItem());
        }

        // dummy
        return new ForgeItemSyncPacket(size, list, data.readBlockPos());
    }
}
