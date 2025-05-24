package com.cursee.disenchanting_table.client.network.packet;

import com.cursee.disenchanting_table.core.ServerConfig;
import com.cursee.disenchanting_table.core.network.packet.ForgeConfigSyncS2CPacket;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.function.Supplier;

public class ForgeConfigSyncClientHandler {

    public static void handle(ForgeConfigSyncS2CPacket packet, CustomPayloadEvent.Context context) {
        ServerConfig.automatic_disenchanting = packet.automatic_disenchanting;
        ServerConfig.resets_repair_cost = packet.resets_repair_cost;
        ServerConfig.requires_experience = packet.requires_experience;
        ServerConfig.uses_points = packet.uses_points;
        ServerConfig.experience_cost = packet.experience_cost;
    }

//    public static void registerS2CPacketHandler(ForgeConfigSyncS2CPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
//        contextSupplier.get().enqueueWork(() -> {
//            CommonConfigValues.automatic_disenchanting = packet.automatic_disenchanting;
//            CommonConfigValues.resets_repair_cost = packet.resets_repair_cost;
//            CommonConfigValues.requires_experience = packet.requires_experience;
//            CommonConfigValues.uses_points = packet.uses_points;
//            CommonConfigValues.experience_cost = packet.experience_cost;
//        });
//    }
}
