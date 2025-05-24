package com.cursee.disenchanting_table.core.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Map;

public class DisenchantmentHelper {


    /**
     * Check if an ItemStack instance has one or more enchantments. If it does,
     * also check that it is either not an Enchanted Book or that it has more than one
     * enchantment.
     *
     * @param stack The ItemStack instance to check.
     * @return `true` if the stack can be disenchanted, otherwise false
     */
    public static boolean canDisenchant(ItemStack stack) {

        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);

        if (enchantments.isEmpty()) return false;

        return !stack.is(Items.ENCHANTED_BOOK) || enchantments.size() >= 2;
    }
}
