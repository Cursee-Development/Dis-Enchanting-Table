package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.client.network.packet.FabricConfigSyncClientHandler;
import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.registry.FabricNetwork;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * <ol>
 *     <li>Server encodes and sends a new instance of {@link FriendlyByteBuf}</li>
 *     <li>Client receives the {@link FriendlyByteBuf}, and handles the data</li>
 * </ol>
 */
public class FabricConfigSyncS2CPacket implements CustomPacketPayload {

    public final boolean automatic_disenchanting;
    public final boolean resets_repair_cost;
    public final boolean requires_experience;
    public final boolean uses_points;
    public final int experience_cost;

    public FabricConfigSyncS2CPacket(boolean automatic_disenchanting, boolean resets_repair_cost, boolean requires_experience, boolean uses_points, int experience_cost) {
        this.automatic_disenchanting = automatic_disenchanting;
        this.resets_repair_cost = resets_repair_cost;
        this.requires_experience = requires_experience;
        this.uses_points = uses_points;
        this.experience_cost = experience_cost;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return FabricNetwork.CONFIG_SYNC_ID;
    }

    public void write(RegistryFriendlyByteBuf data) {
        data.writeBoolean(this.automatic_disenchanting);
        data.writeBoolean(this.resets_repair_cost);
        data.writeBoolean(this.requires_experience);
        data.writeBoolean(this.uses_points);
        data.writeInt(this.experience_cost);
    }

    public static FabricConfigSyncS2CPacket read(RegistryFriendlyByteBuf data) {
        return new FabricConfigSyncS2CPacket(data.readBoolean(), data.readBoolean(), data.readBoolean(), data.readBoolean(), data.readInt());
    }

    public void handle(ClientPlayNetworking.Context context) {
        FabricConfigSyncClientHandler.handle(this, context);
    }

//    public static void registerS2CPacketSender(Entity entity, Level level) {
//        if (!(entity instanceof ServerPlayer player)) return;
//        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
//        data.writeBoolean(CommonConfigValues.automatic_disenchanting);
//        data.writeBoolean(CommonConfigValues.resets_repair_cost);
//        data.writeBoolean(CommonConfigValues.requires_experience);
//        data.writeBoolean(CommonConfigValues.uses_points);
//        data.writeInt(CommonConfigValues.experience_cost);
//        FabricNetwork.sendToPlayer(data, player, FabricNetwork.Packets.CONFIG_SYNC_S2C);
//    }
}
