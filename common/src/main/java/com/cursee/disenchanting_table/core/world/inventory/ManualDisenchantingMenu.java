package com.cursee.disenchanting_table.core.world.inventory;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.core.CommonConfigValues;
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

    public final DataSlot cost;
    public final DataSlot mayPickup;

    private final Player player;

    public ManualDisenchantingMenu(int containerIndex, Inventory playerInventory) {
        this(containerIndex, playerInventory, ContainerLevelAccess.NULL);
    }

    public ManualDisenchantingMenu(int containerIndex, Inventory playerInventory, ContainerLevelAccess containerLevelAccess) {
        super(ModMenus.MANUAL_DISENCHANTING_TABLE, containerIndex, playerInventory, containerLevelAccess);
        player = playerInventory.player;
        this.cost = DataSlot.standalone();
        this.mayPickup = DataSlot.standalone();
        this.addDataSlot(this.cost);
        this.addDataSlot(this.mayPickup);
    }

    @Override
    protected boolean mayPickup(Player player, boolean b) {

        int level = player.experienceLevel;
        int pointsRequired = 0;
        if (level <= 15) {
            pointsRequired = 2 * (level + 7);
        }
        if (level >= 16 && level <= 30) {
            pointsRequired = 5 * (level - 38);
        }
        if (level >= 31) {
            pointsRequired = 9 * (level - 158);
        }

        final float experienceProgress = player.experienceProgress;
        final int currentExperience = (int) (pointsRequired * experienceProgress);

        boolean creative = player.getAbilities().instabuild;
        boolean experiencePoints = CommonConfigValues.uses_points && (player.experienceLevel >= 1 || currentExperience >= this.cost.get());
        boolean experienceLevels = !CommonConfigValues.uses_points && player.experienceLevel >= this.cost.get();
        boolean experience = experiencePoints || experienceLevels;
        boolean books = this.inputSlots.getItem(1).is(Items.BOOK);

        this.mayPickup.set(((creative || experience) && books) ? 1 : 0);

        return this.mayPickup.get() == 1;
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
        if (!DisenchantmentHelper.canRemoveEnchantments(input) || !extra.is(Items.BOOK)) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            cost.set(0);
            return;
        }

        cost.set(CommonConfigValues.experience_cost);

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

        if (this.player != null) {
            final float experienceProgress = this.player.experienceProgress;

            int level = this.player.experienceLevel;
            int pointsRequired = 0;
            if (level <= 15) {
                pointsRequired = 2 * (level + 7);
            }
            if (level >= 16 && level <= 30) {
                pointsRequired = 5 * (level - 38);
            }
            if (level >= 31) {
                pointsRequired = 9 * (level - 158);
            }

            int currentExperience = (int) (pointsRequired * experienceProgress);

            boolean creative = this.player.getAbilities().instabuild;
            boolean experiencePoints = CommonConfigValues.uses_points && (this.player.experienceLevel >= 1 || currentExperience >= this.cost.get());
            boolean experienceLevels = !CommonConfigValues.uses_points && this.player.experienceLevel >= this.cost.get();
            boolean experience = experiencePoints || experienceLevels;
            boolean books = this.inputSlots.getItem(1).is(Items.BOOK);

            boolean requiresExperience = CommonConfigValues.requires_experience;
            boolean satisfiesCost = (creative || experience) && books;
            this.mayPickup.set((!requiresExperience || satisfiesCost) ? 1 : 0);
        }
    }

    @Override
    protected void onTake(Player player, ItemStack itemStack) {

        ItemStack input = inputSlots.getItem(0);

        if (!input.is(Items.ENCHANTED_BOOK)) {
            if (CommonConfigValues.resets_repair_cost) input.setRepairCost(0);
            EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(ItemStack.EMPTY), input);
            inputSlots.setItem(0, input);
        }
        else {
            if (this.keptEnchantment == null || this.keptEnchantmentLevel == null) return;
            inputSlots.setItem(0, EnchantedBookItem.createForEnchantment(new EnchantmentInstance(this.keptEnchantment, this.keptEnchantmentLevel)));
        }

        ItemStack extra = inputSlots.getItem(1);
        extra.shrink(1);
        inputSlots.setItem(1, extra);

        this.keptEnchantment = null;
        this.keptEnchantmentLevel = null;
        this.stolenEnchantments = null;

        if (CommonConfigValues.uses_points) {
            if (player.totalExperience >= this.cost.get()) player.giveExperiencePoints(-this.cost.get());
            else {
                player.experienceLevel -= 1;
                int newXP = player.getXpNeededForNextLevel();
                newXP -= this.cost.get();
                player.giveExperiencePoints(newXP);
            }
        }
        else {
            player.giveExperienceLevels(-this.cost.get());
        }

        this.cost.set(0);
    }
}
