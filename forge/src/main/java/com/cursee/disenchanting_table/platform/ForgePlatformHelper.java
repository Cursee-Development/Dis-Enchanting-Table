package com.cursee.disenchanting_table.platform;

import com.cursee.disenchanting_table.core.registry.ForgeBlockEntities;
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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;

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
    public boolean isClientSide() {
        return FMLLoader.getDist().isClient();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> registerMenu(BiFunction<Integer, Inventory, T> menuConstructor, FeatureFlagSet flagSet) {
        return new MenuType<>(menuConstructor::apply, flagSet);
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
        if (blockEntity instanceof ForgeDisEnchantingBE disEnchantingBE) disEnchantingBE.doTick(level, pos, state, blockEntity);
    }

    @Override
    public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void registerScreen(MenuType<M> menuType, TriFunction<M, Inventory, Component, S> screenConstructor) {
        MenuScreens.register(menuType, screenConstructor::apply);
    }
}