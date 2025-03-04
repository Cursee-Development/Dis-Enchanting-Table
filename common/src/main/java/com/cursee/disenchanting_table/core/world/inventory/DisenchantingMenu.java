package com.cursee.disenchanting_table.core.world.inventory;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.core.registry.ModMenus;
import com.cursee.disenchanting_table.core.util.ExperienceHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class DisenchantingMenu extends ItemCombinerMenu {

    private final AtomicReference<Player> player;
    private final AtomicInteger cost = new AtomicInteger(0);
    private final AtomicBoolean mayPickup = new AtomicBoolean(false);

    private @Nullable Enchantment keptEnchantment;
    private @Nullable Integer keptEnchantmentLevel;
    private @Nullable Map<Enchantment, Integer> stolenEnchantments;

    public DisenchantingMenu(int containerIndex, Inventory playerInventory) {
        this(containerIndex, playerInventory, ContainerLevelAccess.NULL);
    }

    public DisenchantingMenu(int containerIndex, Inventory playerInventory, ContainerLevelAccess containerLevelAccess) {
        super(ModMenus.MANUAL_DISENCHANTING_TABLE, containerIndex, playerInventory, containerLevelAccess);
        player = new AtomicReference<Player>(playerInventory.player);
        Constants.LOG.info(String.valueOf(ExperienceHelper.totalPointsFromLevelAndProgress(player.get().experienceLevel, player.get().experienceProgress)));
    }

    @Override
    protected boolean mayPickup(Player player, boolean hasStack) {
        return false;
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {

    }

    @Override
    protected boolean isValidBlock(BlockState state) {
        return false;
    }

    @Override
    public void createResult() {

    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return null;
    }
}
