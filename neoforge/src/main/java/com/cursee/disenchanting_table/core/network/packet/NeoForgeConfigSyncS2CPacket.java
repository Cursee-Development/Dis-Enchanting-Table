package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.DisenchantingTable;
import com.cursee.disenchanting_table.core.ServerConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public record NeoForgeConfigSyncS2CPacket(boolean automatic_disenchanting, boolean resets_repair_cost, boolean requires_experience, boolean uses_points, int experience_cost) implements CustomPacketPayload {

    public static final Type<NeoForgeConfigSyncS2CPacket> TYPE =
            new Type<>(DisenchantingTable.identifier("config_sync"));
    public static final StreamCodec<ByteBuf, NeoForgeConfigSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, NeoForgeConfigSyncS2CPacket::automatic_disenchanting,
            ByteBufCodecs.BOOL, NeoForgeConfigSyncS2CPacket::resets_repair_cost,
            ByteBufCodecs.BOOL, NeoForgeConfigSyncS2CPacket::requires_experience,
            ByteBufCodecs.BOOL, NeoForgeConfigSyncS2CPacket::uses_points,
            ByteBufCodecs.INT, NeoForgeConfigSyncS2CPacket::experience_cost,
            NeoForgeConfigSyncS2CPacket::new);

    public static void createAndSend(Entity entity, Level level) {
        if (!(entity instanceof ServerPlayer player)) return;
        PacketDistributor.sendToPlayer(player, new NeoForgeConfigSyncS2CPacket(ServerConfig.automatic_disenchanting, ServerConfig.resets_repair_cost, ServerConfig.requires_experience, ServerConfig.uses_points, ServerConfig.experience_cost));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
