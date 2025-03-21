package com.cursee.disenchanting_table.core.util;

import com.cursee.disenchanting_table.platform.services.IPlatformHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.HashMap;
import java.util.Map;

public class NeoForgeMenuScreenRegistry<M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> {

    public static NeoForgeMenuScreenRegistry<? extends AbstractContainerMenu, ? extends AbstractContainerScreen<? extends AbstractContainerMenu>> INSTANCE;

    public final Map<MenuType, IPlatformHelper.TriFunction<M, Inventory, Component, S>> MENU_SCREEN_MAP = new HashMap<>();

    public NeoForgeMenuScreenRegistry() {
        if (INSTANCE == null) INSTANCE = new NeoForgeMenuScreenRegistry<>();
    }
}
