package com.cursee.disenchanting_table.core.world.inventory;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.core.registry.ModBlocks;
import com.cursee.disenchanting_table.core.registry.ModMenus;
import com.cursee.disenchanting_table.core.util.DisenchantmentHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ManualDisenchantingMenu extends ItemCombinerMenu {

    private @Nullable Enchantment keptEnchantment;
    private @Nullable Integer keptEnchantmentLevel;
    private @Nullable Map<Enchantment, Integer> stolenEnchantments;

    // private final Player player;

    public ManualDisenchantingMenu(int containerIndex, Inventory playerInventory) {
        this(containerIndex, playerInventory, ContainerLevelAccess.NULL);
    }

    public ManualDisenchantingMenu(int containerIndex, Inventory playerInventory, ContainerLevelAccess containerLevelAccess) {
        super(ModMenus.MANUAL_DISENCHANTING_TABLE, containerIndex, playerInventory, containerLevelAccess);
        // player = playerInventory.player;
    }

    @Override
    protected boolean mayPickup(Player player, boolean b) {
        return true; // todo configurable experience amount
    }

    @Override
    protected boolean isValidBlock(BlockState blockState) {
        return blockState.is(ModBlocks.DISENCHANTING_TABLE);
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 27, 47, (stack) -> DisenchantmentHelper.canRemoveEnchantments(stack))
                .withSlot(1, 76, 47, (stack) -> stack.is(Items.BOOK))
                .withResultSlot(2, 134, 47)
                .build();
    }

    @Override
    public void createResult() {
        ItemStack input = inputSlots.getItem(0);
        ItemStack extra = inputSlots.getItem(1);
        if (!DisenchantmentHelper.canRemoveEnchantments(input) || !extra.is(Items.BOOK)) return;

        if (!input.is(Items.ENCHANTED_BOOK)) {
            this.keptEnchantment = null;
            this.keptEnchantmentLevel = null;
            this.stolenEnchantments = EnchantmentHelper.getEnchantments(input);

            ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
            resultSlots.setItem(0, result);
        }
        else {
            this.stolenEnchantments = EnchantmentHelper.getEnchantments(input);
            this.keptEnchantment = stolenEnchantments.keySet().iterator().next();
            this.keptEnchantmentLevel = stolenEnchantments.get(this.keptEnchantment);
            this.stolenEnchantments.remove(this.keptEnchantment);

            ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
            this.resultSlots.setItem(0, result);
        }
    }

    @Override
    protected void onTake(Player player, ItemStack itemStack) {
        // todo needs config cost

        ItemStack input = inputSlots.getItem(0);

        if (!input.is(Items.ENCHANTED_BOOK)) {
            input.setRepairCost(0); // todo add config option
            EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(ItemStack.EMPTY), input);
            inputSlots.setItem(0, input);
        }
        else {
            inputSlots.setItem(0, EnchantedBookItem.createForEnchantment(new EnchantmentInstance(this.keptEnchantment, this.keptEnchantmentLevel)));
        }

        ItemStack extra = inputSlots.getItem(1);
        extra.shrink(1);
        inputSlots.setItem(1, extra);

        this.keptEnchantment = null;
        this.keptEnchantmentLevel = null;
        this.stolenEnchantments = null;
    }
}
