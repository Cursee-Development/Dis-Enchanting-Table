package com.cursee.disenchanting_table.client.network.packet;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.core.CommonConfiguredValues;
import com.cursee.disenchanting_table.platform.Services;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class FabricConfigSyncS2CPacketHandler {

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender responseSender) {
        CommonConfiguredValues.REQUIRES_EXPERIENCE.set(data.readBoolean());
        CommonConfiguredValues.RESET_REPAIR_COST.set(data.readBoolean());
        CommonConfiguredValues.POINTS_OR_LEVELS.set(data.readBoolean() ? "levels" : "points");
        CommonConfiguredValues.EXPERIENCE_COST.set(data.readInt());

        if (Services.PLATFORM.isDevelopmentEnvironment()) Constants.LOG.info("synced server values to client");
    }
}
