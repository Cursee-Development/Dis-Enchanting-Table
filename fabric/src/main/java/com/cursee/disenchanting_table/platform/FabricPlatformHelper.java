package com.cursee.disenchanting_table.platform;

import com.cursee.disenchanting_table.platform.services.IPlatformHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.BiFunction;

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

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> registerMenu(BiFunction<Integer, Inventory, T> menuConstructor, FeatureFlagSet flagSet) {
        return new MenuType<>(menuConstructor::apply, flagSet);
    }
}
