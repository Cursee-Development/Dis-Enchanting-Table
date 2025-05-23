package com.cursee.disenchanting_table.core.world.inventory;

import com.cursee.disenchanting_table.core.ServerConfig;
import com.cursee.disenchanting_table.core.registry.ModBlocks;
import com.cursee.disenchanting_table.core.registry.ModMenus;
import com.cursee.disenchanting_table.core.util.DisenchantmentHelper;
import com.cursee.disenchanting_table.core.util.ExperienceHelper;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;

import java.util.Map;

public class DisenchantingMenu extends ItemCombinerMenu {

    public int cost = 0;
    public final DataSlot mayPickup;

    private final Player playerReference;

    private @Nullable Enchantment keptEnchantment;
    private @Nullable Integer keptEnchantmentLevel;
    private @Nullable Map<Enchantment, Integer> stolenEnchantments;

    public DisenchantingMenu(int containerIndex, Inventory inventory) {
        this(containerIndex, inventory, ContainerLevelAccess.NULL);
    }

    public DisenchantingMenu(int containerIndex, Inventory inventory, ContainerLevelAccess access) {
        super(ModMenus.DISENCHANTING_MENU, containerIndex, inventory, access);

        this.mayPickup = DataSlot.standalone();
        this.addDataSlot(mayPickup);
        this.mayPickup.set(this.cost);

        this.playerReference = inventory.player;
    }

    @Override
    protected boolean isValidBlock(BlockState blockState) {
        return blockState.is(ModBlocks.DISENCHANTING_TABLE);
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 27, 47, DisenchantmentHelper::canDisenchant)
                .withSlot(1, 76, 47, (stack) -> stack.is(Items.BOOK))
                .withResultSlot(2, 134, 47)
                .build();
    }

//    @Override
//    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
//
//        ItemStack newStack = ItemStack.EMPTY;
//        Slot slot = this.slots.get(pIndex);
//        if (!slot.hasItem()) return newStack;
//
//        ItemStack originalStack = slot.getItem();
//        newStack = originalStack.copy();
//
//        final boolean itemMovedToPlayer = pIndex < this.inputSlots.getContainerSize();
//        if (itemMovedToPlayer) {
//            if (!this.moveItemStackTo(originalStack, this.inputSlots.getContainerSize(), this.slots.size(), true)) {
//                return ItemStack.EMPTY;
//            }
//        }
//        else if (!this.moveItemStackTo(originalStack, 0, this.inputSlots.getContainerSize(), false)) {
//            return ItemStack.EMPTY;
//        }
//
//        if (originalStack.isEmpty()) {
//            slot.setByPlayer(ItemStack.EMPTY);
//        }
//        else {
//            slot.setChanged();
//        }
//
//        return newStack;
//    }

    public boolean hasResult() {
        return !this.resultSlots.isEmpty();
    }

    private void reset() {
        this.keptEnchantment = null;
        this.keptEnchantmentLevel = null;
        this.stolenEnchantments = null;
        this.resultSlots.setItem(0, ItemStack.EMPTY);
        this.cost = 0;
        this.mayPickup.set(0);
    }

    private void disenchantNormal(ItemStack input) {
        this.keptEnchantment = null;
        this.keptEnchantmentLevel = null;
        this.stolenEnchantments = EnchantmentHelper.getEnchantments(input);

        ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
        this.resultSlots.setItem(0, result);
    }

    private void disenchantBookWithMany(ItemStack input) {
        this.stolenEnchantments = EnchantmentHelper.getEnchantments(input);
        this.keptEnchantment = stolenEnchantments.keySet().iterator().next();
        this.keptEnchantmentLevel = stolenEnchantments.get(this.keptEnchantment);
        this.stolenEnchantments.remove(this.keptEnchantment);

        ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
        this.resultSlots.setItem(0, result);
    }

    boolean satisfiesCost() {
        if (!(this.playerReference instanceof ServerPlayer serverPlayer)) return false;

        int currentExperience = ExperienceHelper.getTotalPlayerExperiencePoints(serverPlayer);

        boolean creative = this.player.getAbilities().instabuild;
        boolean experiencePoints = ServerConfig.uses_points && currentExperience >= this.cost;
        boolean experienceLevels = !ServerConfig.uses_points && serverPlayer.experienceLevel >= this.cost;
        boolean experience = experiencePoints || experienceLevels;
        boolean books = this.inputSlots.getItem(1).is(Items.BOOK);
        return (creative || experience) && books;
    }

    @Override
    public void createResult() {
        if (!(this.playerReference instanceof ServerPlayer player)) return;

        ItemStack input = this.inputSlots.getItem(0);
        ItemStack book = this.inputSlots.getItem(1);

        if (!DisenchantmentHelper.canDisenchant(input) || !book.is(Items.BOOK)) {
            this.reset();
            return;
        }

        if (!input.is(Items.ENCHANTED_BOOK)) this.disenchantNormal(input);
        else if (EnchantmentHelper.getEnchantments(input).size() > 1) this.disenchantBookWithMany(input);
        else if (EnchantmentHelper.getEnchantments(input).size() == 1) {
            ItemStack result = input.copy();
            if (ServerConfig.resets_repair_cost) result.setRepairCost(0);
            this.resultSlots.setItem(0, result);
        }

        if (!ServerConfig.requires_experience && !this.hasResult()) return;

        boolean requiresExperience = ServerConfig.requires_experience;
        boolean satisfiesCost = satisfiesCost();
        this.mayPickup.set(requiresExperience && satisfiesCost ? 1 : 0);
        this.cost = ServerConfig.experience_cost;
    }

    @Override
    protected boolean mayPickup(@Nonnull Player player, boolean b) {
        this.mayPickup.set(this.satisfiesCost() ? 1 : 0);
        return this.mayPickup.get() == 1;
    }

    @Override
    protected void onTake(Player player, ItemStack itemStack) {

        this.access.execute((level, blockPos) -> {
            level.levelEvent(LevelEvent.END_PORTAL_FRAME_FILL, blockPos, 0); /// {@link LevelRenderer#levelEvent(int, BlockPos, int)}
            // level.playLocalSound(blockPos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0f, 1.0F, false);
            // level.playLocalSound(blockPos, SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.BLOCKS, 1.0f, 1.0F, false);
        });

        if (!(player instanceof ServerPlayer)) return;

        ItemStack input = inputSlots.getItem(0);
        ItemStack extra = inputSlots.getItem(1);

        if (!input.is(Items.ENCHANTED_BOOK)) {
            if (ServerConfig.resets_repair_cost) input.setRepairCost(0);
            EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(ItemStack.EMPTY), input);
            inputSlots.setItem(0, input);
        }
        else if (input.is(Items.ENCHANTED_BOOK) && EnchantmentHelper.getEnchantments(input).size() > 1) {
            if (this.keptEnchantment == null || this.keptEnchantmentLevel == null) return;
            inputSlots.setItem(0, EnchantedBookItem.createForEnchantment(new EnchantmentInstance(this.keptEnchantment, this.keptEnchantmentLevel)));
        }
        else if (input.is(Items.ENCHANTED_BOOK) && EnchantmentHelper.getEnchantments(input).size() == 1) {
            inputSlots.setItem(0, ItemStack.EMPTY);
        }

        extra.shrink(1);
        inputSlots.setItem(1, extra);

        if (!ServerConfig.requires_experience) return;

        if (ServerConfig.uses_points) player.giveExperiencePoints(-ServerConfig.experience_cost);
        else player.giveExperienceLevels(-ServerConfig.experience_cost);

        this.reset();

        if (!input.isEmpty() && input.getBaseRepairCost() > 0) this.createResult();
    }
}
