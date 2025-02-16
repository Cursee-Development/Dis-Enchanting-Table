package com.cursee.disenchanting_table.platform;

import com.cursee.disenchanting_table.core.network.ModNetworkForge;
import com.cursee.disenchanting_table.core.network.packet.NewForgeItemSyncPacket;
import com.cursee.disenchanting_table.core.world.block.entity.ForgeHopperDisEnchantingTableBlockEntity;
import com.cursee.disenchanting_table.core.world.block.entity.HopperDisEnchantingTableBlockEntity;
import com.cursee.disenchanting_table.platform.services.IPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public String getGameDirectory() {

        return FMLPaths.GAMEDIR.get().toString();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {

        return BlockEntityType.Builder.of(func::apply, blocks).build(null);
    }

    @Override
    public BlockEntityType<ForgeHopperDisEnchantingTableBlockEntity> loaderSpecificHoppingTable(Block... blocks) {

        return BlockEntityType.Builder.of(ForgeHopperDisEnchantingTableBlockEntity::new, blocks).build(null);
    }

    @Override
    public void sendToPlayer(ServerPlayer player, ResourceLocation itemSync, FriendlyByteBuf data) {

        int size = data.readInt();
        NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
        for (int i = 0; i < size; i++) {
            items.set(i, data.readItem());
        }
        BlockPos blockPos = data.readBlockPos();

        ModNetworkForge.sendToPlayer(new NewForgeItemSyncPacket(items, blockPos), player);
    }
}