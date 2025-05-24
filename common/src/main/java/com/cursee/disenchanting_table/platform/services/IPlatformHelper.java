package com.cursee.disenchanting_table.platform.services;

import com.cursee.monolib.core.util.TriFunction;
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

import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the root directory of the minecraft instance.
     *
     * @return The String form of the minecraft instance's root folder.
     */
    String getGameDirectory();

    /**
     * Check if the mod is loaded in a client instance.
     *
     * @return True if loaded on a client, false otherwise.
     */
    boolean isClientSide();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    void sendItemSyncToClient(ServerPlayer serverPlayer, NonNullList<ItemStack> inventory, BlockPos blockPos);

    /// Registration

    <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> constructor, Block... validBlocks);

    <T extends AbstractContainerMenu> MenuType<T> registerMenu(BiFunction<Integer, Inventory, T> menuConstructor, FeatureFlagSet flagSet);
    <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void registerScreen(MenuType<M> menuType, TriFunction<M, Inventory, Component, S> screenConstructor);

    CreativeModeTab creativeModeTab(Supplier<ItemStack> icon, Component title, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator);
}