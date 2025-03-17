package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.DisEnchantingTable;
import com.cursee.disenchanting_table.core.network.packet.FabricConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.network.packet.FabricItemSyncS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetwork {

//    public static class Packets {
//        public static final ResourceLocation CONFIG_SYNC_S2C = DisEnchantingTable.identifier("config_sync");
//        public static final ResourceLocation ITEM_SYNC_S2C = DisEnchantingTable.identifier("item_sync");
//
//        public static void registerPacketIDs() {}
//    }
//
//    public static void sendToPlayer(FriendlyByteBuf data, ServerPlayer player, ResourceLocation packetID) {
//        ServerPlayNetworking.send(player, packetID, data);
//    }

    public static final StreamCodec<RegistryFriendlyByteBuf, FabricConfigSyncS2CPacket> CONFIG_SYNC_CODEC =
            StreamCodec.ofMember(FabricConfigSyncS2CPacket::write, FabricConfigSyncS2CPacket::read);
    public static final CustomPacketPayload.Type<FabricConfigSyncS2CPacket> CONFIG_SYNC_ID =
            new CustomPacketPayload.Type<FabricConfigSyncS2CPacket>(DisEnchantingTable.identifier("config_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FabricItemSyncS2CPacket> ITEM_SYNC_CODEC =
            StreamCodec.ofMember(FabricItemSyncS2CPacket::write, FabricItemSyncS2CPacket::read);
    public static final CustomPacketPayload.Type<FabricItemSyncS2CPacket> ITEM_SYNC_ID =
            new CustomPacketPayload.Type<FabricItemSyncS2CPacket>(DisEnchantingTable.identifier("item_sync"));
}
