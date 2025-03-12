package com.cursee.disenchanting_table.core.world.inventory;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.core.CommonConfigValues;
import com.cursee.disenchanting_table.core.registry.ModBlocks;
import com.cursee.disenchanting_table.core.registry.ModMenus;
import com.cursee.disenchanting_table.core.util.DisenchantmentHelper;
import com.cursee.disenchanting_table.core.util.ExperienceHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ManualDisenchantingMenu extends ItemCombinerMenu {

    private @Nullable Holder<Enchantment> keptEnchantment;
    private int keptEnchantmentLevel = 0;
    private ItemEnchantments stolenEnchantments = ItemEnchantments.EMPTY;

    public int cost = 0;
    public final DataSlot mayPickup;

    private final Player player;

    public ManualDisenchantingMenu(int containerIndex, Inventory playerInventory) {
        this(containerIndex, playerInventory, ContainerLevelAccess.NULL);
    }

    public ManualDisenchantingMenu(int containerIndex, Inventory playerInventory, ContainerLevelAccess containerLevelAccess) {
        super(ModMenus.MANUAL_DISENCHANTING_TABLE, containerIndex, playerInventory, containerLevelAccess);
        player = playerInventory.player;
        this.mayPickup = DataSlot.standalone();
        this.addDataSlot(this.mayPickup);
        this.mayPickup.set(0);
    }

    @Override
    protected boolean mayPickup(Player player, boolean b) {

        final int currentExperience = ExperienceHelper.totalPointsFromLevelAndProgress(player.experienceLevel, player.experienceProgress);

        boolean creative = player.getAbilities().instabuild;
        boolean experiencePoints = CommonConfigValues.uses_points && currentExperience >= this.cost;
        boolean experienceLevels = !CommonConfigValues.uses_points && player.experienceLevel >= this.cost;
        boolean experience = experiencePoints || experienceLevels;
        boolean books = this.inputSlots.getItem(1).is(Items.BOOK);

        this.mayPickup.set((creative || experience) && books ? 1 : 0);

        return this.mayPickup.get() == 1;
    }

    @Override
    protected boolean isValidBlock(BlockState blockState) {
        return blockState.is(ModBlocks.DISENCHANTING_TABLE);
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 27, 47, DisenchantmentHelper::canRemoveEnchantments)
                .withSlot(1, 76, 47, (stack) -> stack.is(Items.BOOK))
                .withResultSlot(2, 134, 47)
                .build();
    }

    @Override
    public void createResult() {
        if (!(this.player instanceof ServerPlayer player)) return;
        ItemStack input = inputSlots.getItem(0);
        ItemStack extra = inputSlots.getItem(1);
        if (!DisenchantmentHelper.canRemoveEnchantments(input) || !extra.is(Items.BOOK)) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost = 0;
            return;
        }

        if (!input.is(Items.ENCHANTED_BOOK)) {
            this.keptEnchantment = null;
            this.keptEnchantmentLevel = 0;

            // this.stolenEnchantments = EnchantmentHelper.getEnchantments(input);
            this.stolenEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(input);

            ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);

            // EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
            EnchantmentHelper.setEnchantments(result, this.stolenEnchantments);
            resultSlots.setItem(0, result);
        }
        else {
            // this.stolenEnchantments = EnchantmentHelper.getEnchantments(input);
            this.stolenEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(input);

            // this.keptEnchantment = stolenEnchantments.keySet().iterator().next();
            this.keptEnchantment = stolenEnchantments.keySet().iterator().next();

            // this.keptEnchantmentLevel = stolenEnchantments.get(this.keptEnchantment);
            this.keptEnchantmentLevel = stolenEnchantments.getLevel(this.keptEnchantment);

            // this.stolenEnchantments.remove(this.keptEnchantment);
            ItemEnchantments.Mutable mutableEnchantments = new ItemEnchantments.Mutable(this.stolenEnchantments);
            mutableEnchantments.removeIf(enchantmentHolder -> enchantmentHolder.value() == this.keptEnchantment.value());
            this.stolenEnchantments = mutableEnchantments.toImmutable();

            ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);

            // EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
            EnchantmentHelper.setEnchantments(result, this.stolenEnchantments);
            this.resultSlots.setItem(0, result);
        }

        if (!this.resultSlots.isEmpty()) {

            int currentExperience = ExperienceHelper.totalPointsFromLevelAndProgress(player.experienceLevel, player.experienceProgress);

            boolean creative = this.player.getAbilities().instabuild;
            boolean experiencePoints = CommonConfigValues.uses_points && currentExperience >= this.cost;
            boolean experienceLevels = !CommonConfigValues.uses_points && player.experienceLevel >= this.cost;
            boolean experience = experiencePoints || experienceLevels;
            boolean books = this.inputSlots.getItem(1).is(Items.BOOK);

            boolean requiresExperience = CommonConfigValues.requires_experience;
            boolean satisfiesCost = (creative || experience) && books;
            this.mayPickup.set(requiresExperience && satisfiesCost ? 1 : 0);

            this.cost = CommonConfigValues.experience_cost;
        }
    }

    @Override
    protected void onTake(Player player, ItemStack itemStack) {
        if (!(player instanceof ServerPlayer)) return;

        ItemStack input = inputSlots.getItem(0);
        ItemStack extra = inputSlots.getItem(1);

        if (!input.is(Items.ENCHANTED_BOOK)) {
            // if (CommonConfigValues.resets_repair_cost) input.setRepairCost(0);
            if (CommonConfigValues.resets_repair_cost) input.set(DataComponents.REPAIR_COST, 0);
            EnchantmentHelper.setEnchantments(input, EnchantmentHelper.getEnchantmentsForCrafting(ItemStack.EMPTY));
            inputSlots.setItem(0, input);
        }
        else {
            if (this.keptEnchantment == null || this.keptEnchantmentLevel == 0) return;
            inputSlots.setItem(0, EnchantedBookItem.createForEnchantment(new EnchantmentInstance(this.keptEnchantment, this.keptEnchantmentLevel)));
        }

        extra.shrink(1);
        inputSlots.setItem(1, extra);

        this.keptEnchantment = null;
        this.keptEnchantmentLevel = 0;
        this.stolenEnchantments = null;

        if (CommonConfigValues.uses_points) player.giveExperiencePoints(-CommonConfigValues.experience_cost);
        else player.giveExperienceLevels(-CommonConfigValues.experience_cost);

        this.cost = 0;
    }

    public boolean hasResult() {
        return !this.resultSlots.isEmpty();
    }
}
