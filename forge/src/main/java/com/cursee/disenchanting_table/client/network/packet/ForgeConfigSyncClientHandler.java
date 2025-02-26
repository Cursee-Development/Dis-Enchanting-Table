package com.cursee.disenchanting_table.client.network.packet;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.network.packet.ForgeConfigSyncS2CPacket;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeConfigSyncClientHandler {

    public static void registerS2CPacketHandler(ForgeConfigSyncS2CPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> CommonConfigValues.automatic_disenchanting = packet.automatic_disenchanting);
    }
}
