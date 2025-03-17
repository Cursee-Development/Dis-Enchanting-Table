package com.cursee.disenchanting_table.core.registry;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.client.network.packet.NeoForgeConfigSyncClientHandler;
import com.cursee.disenchanting_table.client.network.packet.NeoForgeItemSyncClientHandler;
import com.cursee.disenchanting_table.core.network.packet.NeoForgeConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.network.packet.NeoForgeItemSyncS2CPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ForgeNetwork {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                NeoForgeConfigSyncS2CPacket.TYPE,
                NeoForgeConfigSyncS2CPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        NeoForgeConfigSyncClientHandler::handle,
                        ForgeNetwork::handleConfigSyncPacketOnServer
                ));

        registrar.playToClient(
                NeoForgeItemSyncS2CPacket.TYPE,
                NeoForgeItemSyncS2CPacket.REGISTRY_STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        NeoForgeItemSyncClientHandler::handle,
                        ForgeNetwork::handleItemSyncPacketOnServer
                ));
    }

    public static void handleConfigSyncPacketOnServer(NeoForgeConfigSyncS2CPacket packet, IPayloadContext context) {}
    public static void handleItemSyncPacketOnServer(NeoForgeItemSyncS2CPacket packet, IPayloadContext context) {}

//    private static SimpleChannel INSTANCE;
//
//    public static final CustomPacketPayload.Type<ForgeConfigSyncS2CPacket> CONFIG_SYNC_ID =
//            new CustomPacketPayload.Type<ForgeConfigSyncS2CPacket>(DisEnchantingTable.identifier("config_sync"));
//    public static final CustomPacketPayload.Type<ForgeItemSyncS2CPacket> ITEM_SYNC_ID =
//            new CustomPacketPayload.Type<ForgeItemSyncS2CPacket>(DisEnchantingTable.identifier("item_sync"));
//
//    private static int packetID = 0;
//    private static int id() {
//        return packetID++;
//    }
//
//    public static void register() {
//        SimpleChannel net = ChannelBuilder
//                .named(DisEnchantingTable.identifier(Constants.MOD_ID))
//                .networkProtocolVersion(1)
//                .clientAcceptedVersions(Channel.VersionTest.exact(1))
//                .serverAcceptedVersions(Channel.VersionTest.exact(1))
//                .simpleChannel();
//        INSTANCE = net;
//
//        net.messageBuilder(ForgeConfigSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
//                .decoder(ForgeConfigSyncS2CPacket::new)
//                .encoder(ForgeConfigSyncS2CPacket::toBytes)
//                .consumerMainThread(ForgeConfigSyncS2CPacket::handle)
//                .add();
//
//        net.messageBuilder(ForgeItemSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
//                .decoder(ForgeItemSyncS2CPacket::new)
//                .encoder(ForgeItemSyncS2CPacket::toBytes)
//                .consumerMainThread(ForgeItemSyncS2CPacket::handle)
//                .add();
//    }
//
//    public static <MSG> void sendToServer(MSG message) {
//        INSTANCE.send(message, PacketDistributor.SERVER.noArg());
//    }
//
//    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
//        INSTANCE.send(message, PacketDistributor.PLAYER.noArg());
//    }
}
