package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.core.CommonConfigFabric;
import com.cursee.disenchanting_table.core.registry.ModMessages;
import com.cursee.disenchanting_table.core.registry.RegistryFabric;
import com.cursee.monolib.core.sailing.Sailing;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class DisEnchantingTableFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        DisEnchantingTable.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        RegistryFabric.register();

        CommonConfigFabric.onCommonLoaded();
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!(entity instanceof ServerPlayer player)) return;
            FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
            data.writeBoolean(CommonConfigFabric.REQUIRES_EXPERIENCE.get().get());
            data.writeBoolean(CommonConfigFabric.RESET_REPAIR_COST.get().get());
            data.writeBoolean(!CommonConfigFabric.POINTS_OR_LEVELS.get().get().equals("points"));
            data.writeInt(CommonConfigFabric.EXPERIENCE_COST.get().get());
            ServerPlayNetworking.send(player, ModMessages.CONFIG_SYNC_S2C, data);
        });
    }
}
