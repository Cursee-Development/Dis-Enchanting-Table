package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.client.network.packet.FabricConfigSyncClientHandler;
import com.cursee.disenchanting_table.core.ServerConfig;
import com.cursee.disenchanting_table.core.network.FabricNetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class FabricConfigSyncS2CPacket implements CustomPacketPayload {

    public boolean automatic_disenchanting;
    public boolean resets_repair_cost;
    public boolean requires_experience;
    public boolean uses_points;
    public int experience_cost;

    public FabricConfigSyncS2CPacket(boolean automatic_disenchanting, boolean resets_repair_cost, boolean requires_experience, boolean uses_points, int experience_cost) {
        this.automatic_disenchanting = automatic_disenchanting;
        this.resets_repair_cost = resets_repair_cost;
        this.requires_experience = requires_experience;
        this.uses_points = uses_points;
        this.experience_cost = experience_cost;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return FabricNetwork.Packets.CONFIG_SYNC_ID;
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

    public static void createAndSend(Entity entity, ServerLevel level) {
        if (!(entity instanceof ServerPlayer player)) return;
        FabricNetwork.sendToPlayer(player, new FabricConfigSyncS2CPacket(ServerConfig.automatic_disenchanting, ServerConfig.resets_repair_cost, ServerConfig.requires_experience, ServerConfig.uses_points, ServerConfig.experience_cost));
    }
}

///**
// * <ol>
// *     <li>Server encodes and sends a new instance of {@link FriendlyByteBuf}</li>
// *     <li>Client receives the {@link FriendlyByteBuf}, and handles the data</li>
// * </ol>
// */
//public class FabricConfigSyncS2CPacket {
//
//    public static void createAndSend(Entity entity, Level level) {
//        if (!(entity instanceof ServerPlayer player)) return;
//        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
//        data.writeBoolean(ServerConfig.automatic_disenchanting);
//        data.writeBoolean(ServerConfig.resets_repair_cost);
//        data.writeBoolean(ServerConfig.requires_experience);
//        data.writeBoolean(ServerConfig.uses_points);
//        data.writeInt(ServerConfig.experience_cost);
//        FabricNetwork.sendToPlayer(data, player, FabricNetwork.Packets.CONFIG_SYNC_S2C);
//    }
//}
