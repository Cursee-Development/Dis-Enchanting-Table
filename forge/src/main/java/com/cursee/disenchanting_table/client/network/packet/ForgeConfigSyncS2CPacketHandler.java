package com.cursee.disenchanting_table.client.network.packet;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.DisEnchantingTableForge;
import com.cursee.disenchanting_table.core.CommonConfiguredValues;
import com.cursee.disenchanting_table.platform.Services;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeConfigSyncS2CPacketHandler {
    public static void handleOnClient(DisEnchantingTableForge.SyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            CommonConfiguredValues.REQUIRES_EXPERIENCE.set(packet.requires_experience);
            CommonConfiguredValues.RESET_REPAIR_COST.set(packet.reset_repair_cost);
            CommonConfiguredValues.POINTS_OR_LEVELS.set(packet.points_or_levels ? "levels" : "points");
            CommonConfiguredValues.EXPERIENCE_COST.set(packet.experience_cost);

            if (Services.PLATFORM.isDevelopmentEnvironment()) Constants.LOG.info("synced server values to client");
        });
    }
}
