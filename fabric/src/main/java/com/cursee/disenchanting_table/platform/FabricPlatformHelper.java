package com.cursee.disenchanting_table.platform;

import com.cursee.disenchanting_table.core.network.FabricNetwork;
import com.cursee.disenchanting_table.core.network.packet.FabricItemSyncS2CPacket;
import com.cursee.disenchanting_table.platform.services.IPlatformHelper;
import com.cursee.monolib.core.util.TriFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public String getGameDirectory() {

        return FabricLoader.getInstance().getGameDir().toString();
    }

    @Override
    public boolean isClientSide() {

        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    public void sendItemSyncToClient(ServerPlayer serverPlayer, NonNullList<ItemStack> inventory, BlockPos blockPos) {
        // FabricItemSyncS2CPacket.createAndSend(serverPlayer, inventory, blockPos);
        FabricNetwork.sendToPlayer(serverPlayer, new FabricItemSyncS2CPacket(blockPos, inventory.size(), inventory));
    }

    /// Registration

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> constructor, Set<Block> validBlocks) {
        return FabricBlockEntityTypeBuilder.<T>create(constructor::apply, validBlocks.toArray(new Block[0])).build();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> registerMenu(BiFunction<Integer, Inventory, T> menuConstructor, FeatureFlagSet flagSet) {
        return new MenuType<>(menuConstructor::apply, flagSet);
    }

    @Override
    public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void registerScreen(MenuType<M> menuType, TriFunction<M, Inventory, Component, S> screenConstructor) {
        MenuScreens.register(menuType, screenConstructor::apply);
    }

    @Override
    public CreativeModeTab creativeModeTab(Supplier<ItemStack> icon, Component title, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
        return FabricItemGroup.builder().icon(icon).title(title).displayItems(displayItemsGenerator).build();
    }
}
