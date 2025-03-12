package com.cursee.disenchanting_table.core.world.block.entity;

import net.minecraft.core.Direction;

public class InventoryDirectionEntry {
    public Direction direction;
    public int slotIndex;
    public boolean canInsert;

    public InventoryDirectionEntry(Direction direction, int slotIndex, boolean canInsert) {
        this.direction = direction;
        this.slotIndex = slotIndex;
        this.canInsert = canInsert;
    }
}
