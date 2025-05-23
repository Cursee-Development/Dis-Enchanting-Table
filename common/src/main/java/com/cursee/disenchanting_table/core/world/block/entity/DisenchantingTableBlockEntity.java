package com.cursee.disenchanting_table.core.world.block.entity;

import com.cursee.disenchanting_table.core.ServerConfig;
import com.cursee.disenchanting_table.core.registry.ModBlockEntities;
import com.cursee.disenchanting_table.core.util.DisenchantmentHelper;
import com.cursee.disenchanting_table.core.util.ExperienceHelper;
import com.cursee.disenchanting_table.core.util.S2CBlockEntityUpdatePacket;
import com.cursee.disenchanting_table.core.world.block.entity.util.ExposedSimpleInventoryBlockEntity;
import com.cursee.disenchanting_table.core.world.block.entity.util.SimpleInventoryBlockEntity;
import com.cursee.disenchanting_table.core.world.inventory.DisenchantingMenu;
import com.cursee.disenchanting_table.core.world.inventory.DisenchantingTableMenu;
import com.cursee.disenchanting_table.platform.Services;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Map;

public class DisenchantingTableBlockEntity extends ExposedSimpleInventoryBlockEntity implements MenuProvider {

    public static final int INPUT_SLOT = 0;
    public static final int EXTRA_SLOT = 1;
    public static final int OUTER_SLOT = 2; // lol not output bc it doesn't line up all pretty lolololol

    public static final int SLOT_COUNT = 3;

    private int signal = 0;
    private boolean dirty = false;

    private static final int MAX_PROGRESS = 10;
    private int progress = 0;

    protected final ContainerData dataAccess;

    public DisenchantingTableBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DISENCHANTING_TABLE, pos, blockState);

        this.dataAccess = new ContainerData() {
            @Override
            public int get(int i) {
                if (i == 0) return DisenchantingTableBlockEntity.this.progress;
                return 0;
            }

            @Override
            public void set(int i, int v) {
                if (i == 0) DisenchantingTableBlockEntity.this.progress = v;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("itemGroup.disenchantingTable");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        if (this.level == null) return null;
        return ServerConfig.automatic_disenchanting ? new DisenchantingTableMenu(i, inventory, this, this.dataAccess) : new DisenchantingMenu(i, inventory, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {

        if (!ServerConfig.automatic_disenchanting) return false;

        if (index == 0 && !DisenchantmentHelper.canDisenchant(stack)) return true;

        return index == 2;
    }

    @Override
    protected SimpleContainer createItemHandler() {
        return new SimpleContainer(SLOT_COUNT) {

            @Override
            public boolean canPlaceItem(int index, ItemStack stack) {
                if (!ServerConfig.automatic_disenchanting) return false;
                return (index == INPUT_SLOT && DisenchantmentHelper.canDisenchant(stack)) || (index == EXTRA_SLOT && stack.is(Items.BOOK));
            }
        };
    }

    public static void tickClient(Level level, BlockPos pos, BlockState state, DisenchantingTableBlockEntity table) {
    }

    public static void tickServer(Level level, BlockPos pos, BlockState state, DisenchantingTableBlockEntity table) {

        if (!ServerConfig.automatic_disenchanting) return;

        Player player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 16.0D, false); // final param as false allows creative players and not spectator
        if (player == null || !hasEnoughExperience(player)) return; // only process if the nearest player is creative or has enough experience

        boolean validInputs = DisenchantmentHelper.canDisenchant(table.getItem(INPUT_SLOT)) && table.getItem(EXTRA_SLOT).is(Items.BOOK);
        if (validInputs && table.getItem(OUTER_SLOT).isEmpty()) {

            if (table.progress >= MAX_PROGRESS) {
                table.disenchant(level, pos, player);
                level.levelEvent(LevelEvent.END_PORTAL_FRAME_FILL, pos, 0); /// {@link LevelRenderer#levelEvent(int, BlockPos, int)}
                table.progress = 0;
            }

            table.progress++;
            table.dirty = true;
        }
        else {
            table.progress = 0;
        }

        /// update redstone signal
        int newSignal = 0;
        Container container = table.getItemHandler();
        if (!container.isEmpty()) {
            if (!container.getItem(0).isEmpty()) {
                newSignal = 3; // if an input item is present

                if (!container.getItem(1).isEmpty()) newSignal = 6; // if an input item and a book are present
            }
            if (!container.getItem(2).isEmpty()) newSignal = 9; // if an output item is present
        }

        if (newSignal != table.signal) {
            table.signal = newSignal;
            level.updateNeighbourForOutputSignal(pos, state.getBlock());
        }

        /// mark as dirty / unsaved
        if (table.dirty) {
            table.dirty = false;
            S2CBlockEntityUpdatePacket.sendToClients(table);
        }
    }

    private static boolean hasEnoughExperience(Player player) {

        if (!ServerConfig.requires_experience) return true;

        if (ServerConfig.uses_points && ExperienceHelper.hasEnoughExperiencePoints(player, ServerConfig.experience_cost)) return true;
        else if (!ServerConfig.uses_points && ExperienceHelper.hasEnoughExperienceLevels(player, ServerConfig.experience_cost)) return true;

        return player.isCreative();
    }

    private @Nullable Enchantment keptEnchantment = null;
    private @Nullable Integer keptEnchantmentLevel = null;
    private @Nullable Map<Enchantment, Integer> stolenEnchantments = null;
    private void disenchant(Level level, BlockPos pos, Player player) {

        /// disenchanting item
        ItemStack input = this.getItem(0);

        if (!input.is(Items.ENCHANTED_BOOK)) {
            this.stolenEnchantments = EnchantmentHelper.getEnchantments(input);

            ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
            if (ServerConfig.resets_repair_cost) result.setRepairCost(0);
            this.setItem(2, result);

            EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(ItemStack.EMPTY), input);
            if (ServerConfig.resets_repair_cost) input.setRepairCost(0);
            this.setItem(0, input);

            this.removeNormalBook();
        }
        else {
            this.stolenEnchantments = EnchantmentHelper.getEnchantments(input);
            this.keptEnchantment = this.stolenEnchantments.keySet().iterator().next();
            this.keptEnchantmentLevel = this.stolenEnchantments.get(this.keptEnchantment);
            this.stolenEnchantments.remove(this.keptEnchantment);

            ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(this.stolenEnchantments, result);
            if (ServerConfig.resets_repair_cost) result.setRepairCost(0);
            this.setItem(2, result);

            if (this.keptEnchantment == null || this.keptEnchantmentLevel == null) return;
            ItemStack keptEnchantedBook = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(this.keptEnchantment, this.keptEnchantmentLevel));
            if (ServerConfig.resets_repair_cost) keptEnchantedBook.setRepairCost(0);
            this.setItem(0, keptEnchantedBook);

            this.removeNormalBook();
        }

        /// taking experience
        if (!player.isCreative()) {
            if (ServerConfig.uses_points) ExperienceHelper.deductExperiencePoints(player, ServerConfig.experience_cost);
            else ExperienceHelper.deductExperienceLevels(player, ServerConfig.experience_cost);
        }
    }

    public void removeNormalBook() {
        ItemStack bookStack = this.getItem(1);
        bookStack.shrink(1);
        this.setItem(1, bookStack);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.progress = tag.getInt("Progress");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putInt("Progress", this.progress);
        super.saveAdditional(tag);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide) {
            this.dirty = true;

            for (Player player : level.players()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    Services.PLATFORM.sendItemSyncToClient(serverPlayer, SimpleInventoryBlockEntity.copyFromInv(this), this.getBlockPos());
                }
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public int getSignal() {
        return signal;
    }

    public ItemStack getRenderStack() {
        return !getItem(2).isEmpty() ? getItem(2) : getItem(0);
    }
}
