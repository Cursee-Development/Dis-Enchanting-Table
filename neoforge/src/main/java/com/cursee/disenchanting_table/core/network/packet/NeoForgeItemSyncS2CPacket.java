package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.DisEnchantingTable;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record NeoForgeItemSyncS2CPacket(long blockPos, int size, Holder<Item> displayItem) implements CustomPacketPayload {

    public static final Type<NeoForgeItemSyncS2CPacket> TYPE =
            new Type<>(DisEnchantingTable.identifier("item_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, NeoForgeItemSyncS2CPacket> REGISTRY_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, NeoForgeItemSyncS2CPacket::blockPos,
            ByteBufCodecs.VAR_INT, NeoForgeItemSyncS2CPacket::size,
            ByteBufCodecs.holderRegistry(Registries.ITEM), NeoForgeItemSyncS2CPacket::displayItem,
            NeoForgeItemSyncS2CPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
