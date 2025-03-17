package com.cursee.disenchanting_table.core.world.block.entity;

import com.cursee.disenchanting_table.core.util.LazyOptional;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryDirectionWrapper {
    public Map<Direction, LazyOptional<WrappedHandler>> directionsMap;

    public InventoryDirectionWrapper(IItemHandlerModifiable handler, InventoryDirectionEntry... entries) {
        directionsMap = new HashMap<>();
        for (var x : entries) {
            directionsMap.put(x.direction,
                    LazyOptional.of(() -> new WrappedHandler(handler, (i) -> Objects.equals(i, x.slotIndex), (i, s) -> x.canInsert)));
        }
    }
}
