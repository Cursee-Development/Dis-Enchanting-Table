package com.cursee.disenchanting_table.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;

/// Adapted from [Botania](https://github.com/VazkiiMods/Botania)
public final class S2CBlockEntityUpdatePacket {

    public static void sendToClients(BlockEntity blockEntity) {

        if (!(blockEntity.getLevel() instanceof ServerLevel level)) return;

        Packet<?> packet = blockEntity.getUpdatePacket();

        if (packet == null) return;

        BlockPos pos = blockEntity.getBlockPos();
        level.getChunkSource().chunkMap
                .getPlayers(new ChunkPos(pos), false)
                // .forEach(e -> e.connection.send(packet));
                .forEach(e -> e.connection.send(packet));
    }
}
