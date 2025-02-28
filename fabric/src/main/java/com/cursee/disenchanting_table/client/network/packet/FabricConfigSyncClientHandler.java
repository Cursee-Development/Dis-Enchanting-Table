package com.cursee.disenchanting_table.client.network.packet;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class FabricConfigSyncClientHandler {

    public static void registerS2CPacketHandler(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender responseSender) {
        CommonConfigValues.automatic_disenchanting = data.readBoolean();
        CommonConfigValues.resets_repair_cost = data.readBoolean();
        CommonConfigValues.requires_experience = data.readBoolean();
        CommonConfigValues.uses_points = data.readBoolean();
        CommonConfigValues.experience_cost = data.readInt();
    }
}
