package com.cursee.disenchanting_table.platform;

import com.cursee.disenchanting_table.core.network.packet.ForgeItemSyncS2CPacket;
import com.cursee.disenchanting_table.platform.services.IPlatformHelper;
import com.cursee.monolib.core.util.TriFunction;
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
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.function.BiFunction;
import java.util.function.Supplier;

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

        return FMLEnvironment.dist == Dist.CLIENT;
    }

    public void sendItemSyncToClient(ServerPlayer serverPlayer, NonNullList<ItemStack> inventory, BlockPos blockPos) {
        ForgeItemSyncS2CPacket.createAndSend(serverPlayer, inventory, blockPos);
    }

    /// Registration

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> constructor, Block... validBlocks) {
        return BlockEntityType.Builder.of(constructor::apply, validBlocks).build(null);
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
        return CreativeModeTab.builder().withTabsBefore(CreativeModeTabs.SPAWN_EGGS).icon(icon).title(title).displayItems(displayItemsGenerator).build();
    }
}