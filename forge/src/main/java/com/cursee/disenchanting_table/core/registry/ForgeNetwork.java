package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.DisEnchantingTable;
import com.cursee.disenchanting_table.core.network.packet.ForgeConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.network.packet.ForgeItemSyncS2CPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*;

public class ForgeNetwork {

    private static SimpleChannel INSTANCE;

    public static final CustomPacketPayload.Type<ForgeConfigSyncS2CPacket> CONFIG_SYNC_ID =
            new CustomPacketPayload.Type<ForgeConfigSyncS2CPacket>(DisEnchantingTable.identifier("config_sync"));
    public static final CustomPacketPayload.Type<ForgeItemSyncS2CPacket> ITEM_SYNC_ID =
            new CustomPacketPayload.Type<ForgeItemSyncS2CPacket>(DisEnchantingTable.identifier("item_sync"));

    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = ChannelBuilder
                .named(DisEnchantingTable.identifier(Constants.MOD_ID))
                .networkProtocolVersion(1)
                .clientAcceptedVersions(Channel.VersionTest.exact(1))
                .serverAcceptedVersions(Channel.VersionTest.exact(1))
                .simpleChannel();
        INSTANCE = net;

        net.messageBuilder(ForgeConfigSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeConfigSyncS2CPacket::new)
                .encoder(ForgeConfigSyncS2CPacket::toBytes)
                .consumerMainThread(ForgeConfigSyncS2CPacket::handle)
                .add();

        net.messageBuilder(ForgeItemSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeItemSyncS2CPacket::new)
                .encoder(ForgeItemSyncS2CPacket::toBytes)
                .consumerMainThread(ForgeItemSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.send(message, PacketDistributor.SERVER.noArg());
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(message, PacketDistributor.PLAYER.noArg());
    }

//    private static final String PROTOCOL_VERSION = "1";
//    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
//            DisEnchantingTable.identifier(Constants.MOD_ID),
//            () -> PROTOCOL_VERSION,
//            PROTOCOL_VERSION::equals,
//            PROTOCOL_VERSION::equals
//    );
//
//    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
//        ForgeNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
//    }
//
//    private static int packetID = 0;
//    private static int createNewPacketID() {
//        return packetID = packetID + 1;
//    }
//
//    public static void registerS2CPackets() {
//        ForgeNetwork.INSTANCE.registerMessage(createNewPacketID(), ForgeConfigSyncS2CPacket.class, ForgeConfigSyncS2CPacket::encode, ForgeConfigSyncS2CPacket::decode, ForgeConfigSyncS2CPacket::handle);
//        ForgeNetwork.INSTANCE.registerMessage(createNewPacketID(), ForgeItemSyncS2CPacket.class, ForgeItemSyncS2CPacket::encode, ForgeItemSyncS2CPacket::decode, ForgeItemSyncS2CPacket::handle);
//    }
}
