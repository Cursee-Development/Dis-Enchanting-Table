package com.cursee.disenchanting_table.core.world.inventory;

import com.cursee.disenchanting_table.core.CommonConfiguredValues;
import com.cursee.disenchanting_table.core.registry.ModBlocks;
import com.cursee.disenchanting_table.core.registry.ModMenus;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DisEnchantingTableMenu extends ItemCombinerMenu {

    private @Nullable Enchantment keptEnchantment;
    private @Nullable Integer keptEnchantmentLevel;
    private @Nullable Map<Enchantment, Integer> stolenEnchantments;
    public final DataSlot cost;

    private @Nullable Player player;
    public final DataSlot mayPickup; // 0 is false

    public DisEnchantingTableMenu(int containerID, Inventory inventory) {
        this(containerID, inventory, ContainerLevelAccess.NULL);
    }

    public DisEnchantingTableMenu(int containerID, Inventory inventory, ContainerLevelAccess containerAccess) {
        super(ModMenus.DISENCHANTING_MENU, containerID, inventory, containerAccess);
        this.cost = DataSlot.standalone();
        this.mayPickup = DataSlot.standalone();
        this.addDataSlot(this.cost);
        this.addDataSlot(this.mayPickup);
        this.player = inventory.player;
    }

    @Override
    protected boolean isValidBlock(BlockState state) {
        return state.is(ModBlocks.DISENCHANTING_TABLE);
    }

    @Override
    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 27, 47, stack -> !EnchantmentHelper.getEnchantments(stack).isEmpty())
                .withSlot(1, 76, 47, stack -> stack.is(Items.BOOK))
                .withResultSlot(2, 134, 47)
                .build();
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
        boolean experiencePoints = CommonConfiguredValues.POINTS_OR_LEVELS.get().equals("points") && (player.experienceLevel >= 1 || currentExperience >= this.cost.get());
        boolean experienceLevels = CommonConfiguredValues.POINTS_OR_LEVELS.get().equals("levels") && player.experienceLevel >= this.cost.get();
        boolean experience = experiencePoints || experienceLevels;
        boolean books = this.inputSlots.getItem(1).is(Items.BOOK);

        this.mayPickup.set(((creative || experience) && books) ? 1 : 0);

        return this.mayPickup.get() == 1;
    }

    private void takeEnchantedBookResult(Player player, ItemStack inputStack) {
        if (this.keptEnchantment == null || this.keptEnchantmentLevel == null) return;

        ItemStack keptEnchantedBook = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(this.keptEnchantment, this.keptEnchantmentLevel));
        this.inputSlots.setItem(0, keptEnchantedBook);

        ItemStack bookStack = this.inputSlots.getItem(1);
        bookStack.shrink(1);
        this.inputSlots.setItem(1, bookStack);
    }

    private void takeRegularItemResult(Player player, ItemStack inputStack) {
        EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(ItemStack.EMPTY), inputStack);
        if (CommonConfiguredValues.RESET_REPAIR_COST.get()) inputStack.setRepairCost(0);
        this.inputSlots.setItem(0, inputStack);

        ItemStack bookStack = this.inputSlots.getItem(1);
        bookStack.shrink(1);
        this.inputSlots.setItem(1, bookStack);
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {

        ItemStack inputStack = this.inputSlots.getItem(0);
        if (inputStack.is(Items.ENCHANTED_BOOK)) takeEnchantedBookResult(player, inputStack);
        else takeRegularItemResult(player, inputStack);
        
        if (CommonConfiguredValues.POINTS_OR_LEVELS.get().equals("points")) {
            if (player.totalExperience >= this.cost.get()) player.giveExperiencePoints(-this.cost.get());
            else {
                player.experienceLevel -= 1;
                int newXP = player.getXpNeededForNextLevel();
                newXP -= this.cost.get();
                player.giveExperiencePoints(newXP);
            }
        }
        else if (CommonConfiguredValues.POINTS_OR_LEVELS.get().equals("levels")) {
            player.giveExperienceLevels(-this.cost.get());
        }
        
        this.cost.set(0);
    }

    private void createEnchantedBookResult() {

        ItemStack inputStack = this.inputSlots.getItem(0);
        if (!this.inputSlots.getItem(1).is(Items.BOOK) || EnchantmentHelper.getEnchantments(inputStack).size() <= 1) return;

        this.stolenEnchantments = EnchantmentHelper.getEnchantments(inputStack);
        this.keptEnchantment = this.stolenEnchantments.keySet().iterator().next();
        this.keptEnchantmentLevel = this.stolenEnchantments.get(this.keptEnchantment);
        this.stolenEnchantments.remove(this.keptEnchantment);

        ItemStack resultStack = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(this.stolenEnchantments, resultStack);
        this.resultSlots.setItem(0, resultStack);
    }

    private void createRegularItemResult() {

        ItemStack inputStack = this.inputSlots.getItem(0);
        if (!this.inputSlots.getItem(1).is(Items.BOOK) || EnchantmentHelper.getEnchantments(inputStack).isEmpty()) return;

        this.stolenEnchantments = EnchantmentHelper.getEnchantments(inputStack);
        ItemStack resultStack = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(this.stolenEnchantments, resultStack);
        this.resultSlots.setItem(0, resultStack);
    }

    @Override
    public void createResult() {

        ItemStack inputStack = this.inputSlots.getItem(0);

        if (inputStack.isEmpty() || (inputStack.is(Items.ENCHANTED_BOOK) && EnchantmentHelper.getEnchantments(inputStack).size() == 1)) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
            return;
        }

        this.cost.set(CommonConfiguredValues.EXPERIENCE_COST.get());
        if (inputStack.is(Items.ENCHANTED_BOOK)) createEnchantedBookResult();
        else createRegularItemResult();

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
            boolean experiencePoints = CommonConfiguredValues.POINTS_OR_LEVELS.get().equals("points") && (this.player.experienceLevel >= 1 || currentExperience >= this.cost.get());
            boolean experienceLevels = CommonConfiguredValues.POINTS_OR_LEVELS.get().equals("levels") && this.player.experienceLevel >= this.cost.get();
            boolean experience = experiencePoints || experienceLevels;
            boolean books = this.inputSlots.getItem(1).is(Items.BOOK);
            
            boolean requiresExperience = CommonConfiguredValues.REQUIRES_EXPERIENCE.get();
            boolean satisfiesCost = (creative || experience) && books;
            this.mayPickup.set((!requiresExperience || satisfiesCost) ? 1 : 0);
        }
    }
}
