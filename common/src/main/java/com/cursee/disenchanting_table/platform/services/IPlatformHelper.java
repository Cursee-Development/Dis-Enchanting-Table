package com.cursee.disenchanting_table.platform.services;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

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
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    String getGameDirectory();

    boolean isClientSide();

    <T extends AbstractContainerMenu> MenuType<T> registerMenu(MenuType.MenuSupplier<T> menuConstructor, FeatureFlagSet flagSet);

    @Nullable BlockEntity createLoaderDisEnchantingBE(BlockPos pos, BlockState state);

    BlockEntityType<?> getLoaderDisEnchantingBE();

    void doLoaderDisEnchantingTick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity);

    <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void registerScreen(MenuType<M> menuType, TriFunction<M, Inventory, Component, S> screenConstructor);

    @FunctionalInterface
    interface TriFunction<P1, P2, P3, R> {
        R apply(P1 var1, P2 var2, P3 var3);
    }
}