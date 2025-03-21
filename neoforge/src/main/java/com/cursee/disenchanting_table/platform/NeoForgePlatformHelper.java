package com.cursee.disenchanting_table.platform;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.core.registry.ForgeBlockEntities;
import com.cursee.disenchanting_table.core.util.NeoForgeMenuScreenRegistry;
import com.cursee.disenchanting_table.core.world.block.entity.ForgeDisEnchantingBE;
import com.cursee.disenchanting_table.platform.services.IPlatformHelper;
import net.minecraft.client.gui.screens.MenuScreens;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
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
    public boolean isClientSide() {
        return false;
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> registerMenu(MenuType.MenuSupplier<T> menuConstructor, FeatureFlagSet flagSet) {
        return new MenuType<>(menuConstructor, flagSet);
    }

    @Override
    public @Nullable BlockEntity createLoaderDisEnchantingBE(BlockPos pos, BlockState state) {
        return new ForgeDisEnchantingBE(pos, state);
    }

    @Override
    public BlockEntityType<?> getLoaderDisEnchantingBE() {
        return ForgeBlockEntities.DISENCHANTING_TABLE;
    }

    @Override
    public void doLoaderDisEnchantingTick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof ForgeDisEnchantingBE disenchantingTable) disenchantingTable.doTick(level, pos, state, blockEntity);
    }

    @Override
    public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void registerScreen(MenuType<M> menuType, TriFunction<M, Inventory, Component, S> screenConstructor) {
        // MenuScreens.register(menuType, screenConstructor::apply); // no-op on purpose, registered in loader client entrypoint
    }
}