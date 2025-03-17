package com.cursee.disenchanting_table.client.network.packet;

import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.network.packet.ForgeConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.world.block.entity.ForgeDisEnchantingBE;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.function.Supplier;

public class ForgeConfigSyncClientHandler {

    public static void handle(ForgeConfigSyncS2CPacket packet, CustomPayloadEvent.Context context) {
        CommonConfigValues.automatic_disenchanting = packet.automatic_disenchanting;
        CommonConfigValues.resets_repair_cost = packet.resets_repair_cost;
        CommonConfigValues.requires_experience = packet.requires_experience;
        CommonConfigValues.uses_points = packet.uses_points;
        CommonConfigValues.experience_cost = packet.experience_cost;
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
