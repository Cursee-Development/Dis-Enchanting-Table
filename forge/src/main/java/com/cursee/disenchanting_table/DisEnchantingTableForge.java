package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.client.network.packet.ForgeConfigSyncS2CPacketHandler;
import com.cursee.disenchanting_table.core.CommonConfigForge;
import com.cursee.disenchanting_table.core.network.ModNetworkForge;
import com.cursee.disenchanting_table.core.registry.RegistryForge;
import com.cursee.monolib.core.sailing.Sailing;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod(Constants.MOD_ID)
public class DisEnchantingTableForge {

    public static IEventBus EVENT_BUS = null;
    
    public DisEnchantingTableForge() {
        DisEnchantingTable.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        DisEnchantingTableForge.EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        RegistryForge.register(DisEnchantingTableForge.EVENT_BUS);
        DisEnchantingTableForge.EVENT_BUS.addListener((Consumer<FMLCommonSetupEvent>) event -> event.enqueueWork(() -> {
            ModNetworkForge.registerS2CPackets();
            CommonConfigForge.onCommonLoaded();
        }));
        MinecraftForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
            // event is fired on both logical sides but server players only exist on the server, so this should be fine
            if (event.getEntity() instanceof ServerPlayer player) {
                ModNetworkForge.sendToPlayer(new SyncPacket(CommonConfigForge.REQUIRES_EXPERIENCE.get().get(), CommonConfigForge.RESET_REPAIR_COST.get().get(), !CommonConfigForge.POINTS_OR_LEVELS.get().get().equals("points"), CommonConfigForge.EXPERIENCE_COST.get().get()), player);
            }
        });
    }

    public static class SyncPacket {

        public final boolean requires_experience;
        public final boolean reset_repair_cost;
        public final boolean points_or_levels; // false = points
        public final int experience_cost;

        public SyncPacket(boolean requires_experience, boolean reset_repair_cost, boolean points_or_levels, int experience_cost) {
            this.requires_experience = requires_experience;
            this.reset_repair_cost = reset_repair_cost;
            this.points_or_levels = points_or_levels;
            this.experience_cost = experience_cost;
        }

        public void encode(FriendlyByteBuf data) {
            data.writeBoolean(this.requires_experience);
            data.writeBoolean(this.reset_repair_cost);
            data.writeBoolean(this.points_or_levels);
            data.writeInt(this.experience_cost);
        }

        public static SyncPacket decode(FriendlyByteBuf data) {
            return new SyncPacket(data.readBoolean(), data.readBoolean(), data.readBoolean(), data.readInt());
        }

        public static void handle(SyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
            contextSupplier.get().enqueueWork(
                    () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeConfigSyncS2CPacketHandler.handleOnClient(packet, contextSupplier))
            );
            contextSupplier.get().setPacketHandled(true);
        }
    }
}