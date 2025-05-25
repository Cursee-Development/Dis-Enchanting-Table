package com.cursee.disenchanting_table.core.network;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.DisenchantingTable;
import com.cursee.disenchanting_table.core.network.packet.ForgeConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.network.packet.ForgeItemSyncS2CPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*;

public class ForgeNetwork {

    private static SimpleChannel INSTANCE;

    public static final CustomPacketPayload.Type<ForgeConfigSyncS2CPacket> CONFIG_SYNC_ID =
            new CustomPacketPayload.Type<ForgeConfigSyncS2CPacket>(DisenchantingTable.identifier("config_sync"));
    public static final CustomPacketPayload.Type<ForgeItemSyncS2CPacket> ITEM_SYNC_ID =
            new CustomPacketPayload.Type<ForgeItemSyncS2CPacket>(DisenchantingTable.identifier("item_sync"));

    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    public static void init() {
        SimpleChannel net = ChannelBuilder
                .named(DisenchantingTable.identifier(Constants.MOD_ID))
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

    public static <MSG> void sendToPlayer(ServerPlayer player, MSG message) {
        // INSTANCE.send(message, PacketDistributor.PLAYER.noArg());
        INSTANCE.send(message, player.connection.getConnection());
    }
}
