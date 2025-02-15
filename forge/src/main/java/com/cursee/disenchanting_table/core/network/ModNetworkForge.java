package com.cursee.disenchanting_table.core.network;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.DisEnchantingTable;
import com.cursee.disenchanting_table.core.network.packet.ForgeItemSyncPacket;
import com.cursee.disenchanting_table.core.network.packet.NewForgeItemSyncPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworkForge {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            DisEnchantingTable.identifier(Constants.MOD_ID),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        ModNetworkForge.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    private static int packetID = 0;
    private static int createNewPacketID() {
        return packetID = packetID + 1;
    }
    public static void registerS2CPackets() {
        ModNetworkForge.INSTANCE.registerMessage(createNewPacketID(), NewForgeItemSyncPacket.class, NewForgeItemSyncPacket::encode, NewForgeItemSyncPacket::decode, NewForgeItemSyncPacket::handle);
    }
}
