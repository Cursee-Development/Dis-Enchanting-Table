package com.cursee.disenchanting_table.core.network.packet;

import com.cursee.disenchanting_table.client.network.packet.ForgeConfigSyncClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * <ol>
 *     <li>Server constructs a new packet</li>
 *     <li>Server encodes the packet into a {@link FriendlyByteBuf} before sending</li>
 *     <li>Client receives the {@link FriendlyByteBuf}, and decodes into a new packet</li>
 *     <li>Client constructs a new packet</li>
 *     <li>Client handles packet instance</li>
 * </ol>
 */
public class ForgeConfigSyncS2CPacket {

    public final boolean automatic_disenchanting;

    public ForgeConfigSyncS2CPacket(final boolean automatic_disenchanting) {
        this.automatic_disenchanting = automatic_disenchanting;
    }

    public void encode(FriendlyByteBuf data) {
        data.writeBoolean(this.automatic_disenchanting);
    }

    public static ForgeConfigSyncS2CPacket decode(FriendlyByteBuf data) {
        return new ForgeConfigSyncS2CPacket(data.readBoolean());
    }

    public static void handle(ForgeConfigSyncS2CPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeConfigSyncClientHandler.registerS2CPacketHandler(packet, contextSupplier))
        );
        contextSupplier.get().setPacketHandled(true);
    }
}
