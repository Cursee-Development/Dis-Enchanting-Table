package com.cursee.disenchanting_table.core.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class DisenchantmentHelper {

    public static boolean canRemoveEnchantments(ItemStack stack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        return !enchantments.isEmpty() && (!stack.is(Items.ENCHANTED_BOOK) || enchantments.size() > 1);
    }
}
