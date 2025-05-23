package com.cursee.disenchanting_table.core.world.inventory;

import com.cursee.disenchanting_table.core.registry.ModMenus;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;

public class DisenchantingTableMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData containerData;

    public DisenchantingTableMenu(int containerID, Inventory inventory) {
        this(containerID, inventory, new SimpleContainer(3), new SimpleContainerData(1));
    }

    public DisenchantingTableMenu(int containerID, Inventory inventory, Container container, ContainerData containerData) {
        super(ModMenus.DISENCHANTING_TABLE, containerID);

        checkContainerSize(container, 3);
        checkContainerDataCount(containerData, 1);
        this.container = container;
        this.containerData = containerData;

        this.addSlot(new Slot(container, 0, 27, 47));
        this.addSlot(new Slot(container, 1, 76, 47));
        this.addSlot(new Slot(container, 2, 134, 47) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                Level level = player.level();
                // level.playLocalSound(player.blockPosition(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0f, 1.0F, false);
                level.levelEvent(LevelEvent.END_PORTAL_FRAME_FILL, player.blockPosition(), 0); /// {@link LevelRenderer#levelEvent(int, BlockPos, int)}
            }
        });

        // player internal inventory slots
        for(int row = 0; row < 3; ++row) {
            for(int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        // player hotbar inventory slots
        for(int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(inventory, column, 8 + column * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {

        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (!slot.hasItem()) return newStack;

        ItemStack originalStack = slot.getItem();
        newStack = originalStack.copy();

        final boolean itemMovedToPlayer = pIndex < this.container.getContainerSize();
        if (itemMovedToPlayer) {
            if (!this.moveItemStackTo(originalStack, this.container.getContainerSize(), this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        }
        else if (!this.moveItemStackTo(originalStack, 0, this.container.getContainerSize(), false)) {
            return ItemStack.EMPTY;
        }

        if (originalStack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        }
        else {
            slot.setChanged();
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}
