package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.registry.FabricNetwork;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * <ol>
 *     <li>Server encodes and sends a new instance of {@link FriendlyByteBuf}</li>
 *     <li>Client receives the {@link FriendlyByteBuf}, and handles the data</li>
 * </ol>
 */
public class FabricConfigSyncS2CPacket {

    public static void registerS2CPacketSender(Entity entity, Level level) {
        if (!(entity instanceof ServerPlayer player)) return;
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        data.writeBoolean(CommonConfigValues.automatic_disenchanting);
        data.writeBoolean(CommonConfigValues.resets_repair_cost);
        data.writeBoolean(CommonConfigValues.requires_experience);
        data.writeBoolean(CommonConfigValues.uses_points);
        data.writeInt(CommonConfigValues.experience_cost);
        FabricNetwork.sendToPlayer(data, player, FabricNetwork.Packets.CONFIG_SYNC_S2C);
    }
}
