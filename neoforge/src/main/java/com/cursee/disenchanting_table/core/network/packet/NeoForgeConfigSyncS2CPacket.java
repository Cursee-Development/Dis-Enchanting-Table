package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.DisEnchantingTable;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record NeoForgeConfigSyncS2CPacket(boolean automatic_disenchanting, boolean resets_repair_cost, boolean requires_experience, boolean uses_points, int experience_cost) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<NeoForgeConfigSyncS2CPacket> TYPE =
            new CustomPacketPayload.Type<>(DisEnchantingTable.identifier("config_sync"));
    public static final StreamCodec<ByteBuf, NeoForgeConfigSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, NeoForgeConfigSyncS2CPacket::automatic_disenchanting,
            ByteBufCodecs.BOOL, NeoForgeConfigSyncS2CPacket::resets_repair_cost,
            ByteBufCodecs.BOOL, NeoForgeConfigSyncS2CPacket::requires_experience,
            ByteBufCodecs.BOOL, NeoForgeConfigSyncS2CPacket::uses_points,
            ByteBufCodecs.INT, NeoForgeConfigSyncS2CPacket::experience_cost,
            NeoForgeConfigSyncS2CPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
