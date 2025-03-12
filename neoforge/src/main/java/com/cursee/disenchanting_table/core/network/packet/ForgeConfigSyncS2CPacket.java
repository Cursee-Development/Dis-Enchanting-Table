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
    public final boolean resets_repair_cost;
    public final boolean requires_experience;
    public final boolean uses_points;
    public final int experience_cost;

    public ForgeConfigSyncS2CPacket(boolean automatic_disenchanting, boolean resets_repair_cost, boolean requires_experience, boolean uses_points, int experience_cost) {
        this.automatic_disenchanting = automatic_disenchanting;
        this.resets_repair_cost = resets_repair_cost;
        this.requires_experience = requires_experience;
        this.uses_points = uses_points;
        this.experience_cost = experience_cost;
    }

    public void encode(FriendlyByteBuf data) {
        data.writeBoolean(this.automatic_disenchanting);
        data.writeBoolean(this.resets_repair_cost);
        data.writeBoolean(this.requires_experience);
        data.writeBoolean(this.uses_points);
        data.writeInt(this.experience_cost);
    }

    public static ForgeConfigSyncS2CPacket decode(FriendlyByteBuf data) {
        return new ForgeConfigSyncS2CPacket(data.readBoolean(), data.readBoolean(), data.readBoolean(), data.readBoolean(), data.readInt());
    }

    public static void handle(ForgeConfigSyncS2CPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeConfigSyncClientHandler.registerS2CPacketHandler(packet, contextSupplier))
        );
        contextSupplier.get().setPacketHandled(true);
    }
}
