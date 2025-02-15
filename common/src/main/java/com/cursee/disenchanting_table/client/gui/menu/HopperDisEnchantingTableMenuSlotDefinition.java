package com.cursee.disenchanting_table.client.gui.menu;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HopperDisEnchantingTableMenuSlotDefinition {

    private final List<HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition> slots;
    private final HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition resultSlot;

    HopperDisEnchantingTableMenuSlotDefinition(List<HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition> pSlots, HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition pResultSlot) {
        if (!pSlots.isEmpty() && !pResultSlot.equals(HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition.EMPTY)) {
            this.slots = pSlots;
            this.resultSlot = pResultSlot;
        } else {
            throw new IllegalArgumentException("Need to define both inputSlots and resultSlot");
        }
    }

    public static HopperDisEnchantingTableMenuSlotDefinition.Builder create() {
        return new HopperDisEnchantingTableMenuSlotDefinition.Builder();
    }

    public boolean hasSlot(int pSlot) {
        return this.slots.size() >= pSlot;
    }

    public HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition getSlot(int pSlot) {
        return this.slots.get(pSlot);
    }

    public HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition getResultSlot() {
        return this.resultSlot;
    }

    public List<HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition> getSlots() {
        return this.slots;
    }

    public int getNumOfInputSlots() {
        return this.slots.size();
    }

    public int getResultSlotIndex() {
        return this.getNumOfInputSlots();
    }

    public List<Integer> getInputSlotIndexes() {
        return this.slots.stream().map(HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition::slotIndex).collect(Collectors.toList());
    }

    public static class Builder {
        private final List<HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition> slots = new ArrayList<>();
        private HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition resultSlot = HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition.EMPTY;

        public HopperDisEnchantingTableMenuSlotDefinition.Builder withSlot(int pSlotIndex, int pX, int pY, Predicate<ItemStack> pMayPlace) {
            this.slots.add(new HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition(pSlotIndex, pX, pY, pMayPlace));
            return this;
        }

        public HopperDisEnchantingTableMenuSlotDefinition.Builder withResultSlot(int pSlotIndex, int pX, int pY) {
            this.resultSlot = new HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition(pSlotIndex, pX, pY, (p_266825_) -> {
                return false;
            });
            return this;
        }

        public HopperDisEnchantingTableMenuSlotDefinition build() {
            return new HopperDisEnchantingTableMenuSlotDefinition(this.slots, this.resultSlot);
        }
    }

    public static record SlotDefinition(int slotIndex, int x, int y, Predicate<ItemStack> mayPlace) {
        static final HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition EMPTY = new HopperDisEnchantingTableMenuSlotDefinition.SlotDefinition(0, 0, 0, (p_267109_) -> {
            return true;
        });
    }
}
