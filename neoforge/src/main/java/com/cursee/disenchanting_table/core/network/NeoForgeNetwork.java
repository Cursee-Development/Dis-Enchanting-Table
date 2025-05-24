package com.cursee.disenchanting_table.core.network;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.client.network.packet.NeoForgeConfigSyncClientHandler;
import com.cursee.disenchanting_table.client.network.packet.NeoForgeItemSyncClientHandler;
import com.cursee.disenchanting_table.core.network.packet.NeoForgeConfigSyncS2CPacket;
import com.cursee.disenchanting_table.core.network.packet.NeoForgeItemSyncS2CPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NeoForgeNetwork {

    public static void init() {}

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                NeoForgeConfigSyncS2CPacket.TYPE,
                NeoForgeConfigSyncS2CPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        NeoForgeConfigSyncClientHandler::handle,
                        NeoForgeNetwork::handleConfigSyncPacketOnServer
                ));

        registrar.playToClient(
                NeoForgeItemSyncS2CPacket.TYPE,
                NeoForgeItemSyncS2CPacket.REGISTRY_STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        NeoForgeItemSyncClientHandler::handle,
                        NeoForgeNetwork::handleItemSyncPacketOnServer
                ));
    }

    public static void handleConfigSyncPacketOnServer(NeoForgeConfigSyncS2CPacket packet, IPayloadContext context) {}
    public static void handleItemSyncPacketOnServer(NeoForgeItemSyncS2CPacket packet, IPayloadContext context) {}

    public static <MSG extends CustomPacketPayload> void sendToServer(MSG message) {
        // INSTANCE.send(message, PacketDistributor.SERVER.noArg());
        PacketDistributor.sendToServer(message);
    }

    public static <MSG extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, MSG message) {
        // INSTANCE.send(message, player.connection.getConnection());
        PacketDistributor.sendToPlayer(player, message);
    }
}
