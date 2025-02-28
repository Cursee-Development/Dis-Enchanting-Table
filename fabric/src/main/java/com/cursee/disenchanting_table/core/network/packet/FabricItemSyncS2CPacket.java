package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.registry.FabricNetwork;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * <ol>
 *     <li>Server encodes and sends a new instance of {@link FriendlyByteBuf}</li>
 *     <li>Client receives the {@link FriendlyByteBuf}, and handles the data</li>
 * </ol>
 */
public class FabricItemSyncS2CPacket {

    public static void registerS2CPacketSender(Entity entity, NonNullList<ItemStack> inventory, BlockPos pos) {
        if (!(entity instanceof ServerPlayer player)) return;
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        data.writeInt(inventory.size());
        for (ItemStack stack : inventory) {
            data.writeItem(stack);
        }
        data.writeBlockPos(pos);
        FabricNetwork.sendToPlayer(data, player, FabricNetwork.Packets.ITEM_SYNC_S2C);
    }
}
