package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.DisenchantingTable;
import com.cursee.disenchanting_table.core.network.NeoForgeNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record NeoForgeItemSyncS2CPacket(long blockPos, int size, Holder<Item> displayItem) implements CustomPacketPayload {

    public static final Type<NeoForgeItemSyncS2CPacket> TYPE =
            new Type<>(DisenchantingTable.identifier("item_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, NeoForgeItemSyncS2CPacket> REGISTRY_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, NeoForgeItemSyncS2CPacket::blockPos,
            ByteBufCodecs.VAR_INT, NeoForgeItemSyncS2CPacket::size,
            ByteBufCodecs.holderRegistry(Registries.ITEM), NeoForgeItemSyncS2CPacket::displayItem,
            NeoForgeItemSyncS2CPacket::new);

    public static void createAndSend(ServerPlayer serverPlayer, NonNullList<ItemStack> inventory, BlockPos blockPos) {
        NeoForgeNetwork.sendToPlayer(serverPlayer, new NeoForgeItemSyncS2CPacket(blockPos.asLong(), inventory.size(), inventory.get(2).getItemHolder()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
