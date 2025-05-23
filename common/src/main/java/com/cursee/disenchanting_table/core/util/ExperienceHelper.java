package com.cursee.disenchanting_table.core.util;

import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;

/// Adapted from [Collective](https://github.com/serilum/collective)
public class ExperienceHelper {

    /**
     * Checks if the player has enough XP to spend a given amount.
     */
    public static boolean hasEnoughExperiencePoints(final Player player, final int amount) {
        if (player.isCreative()) {
            return true;
        }
        return amount <= 0 || getTotalPlayerExperiencePoints(player) >= amount;
    }

    /**
     * Checks if the player has enough XP to spend a given amount.
     */
    public static boolean hasEnoughExperienceLevels(final Player player, final int amount) {
        if (player.isCreative()) {
            return true;
        }
        return amount <= 0 || getTotalPlayerExperienceLevels(player) >= amount;
    }

    private static int getTotalPlayerExperienceLevels(Player player) {
        return player.experienceLevel;
    }

    /**
     * Deducts a given amount of XP from the player, if possible.
     */
    public static void deductExperiencePoints(final Player player, final int amount) {
        if (amount <= 0) {
            return;
        }

        final int playerXP = getTotalPlayerExperiencePoints(player);
        if (playerXP >= amount) {
            addExperiencePoints(player, -amount);
        }

        // if (player instanceof ServerPlayer) {
        //     ((ServerPlayer) player).connection.send(new ClientboundEntityEventPacket(player, (byte) 9));
        // }
    }

    /**
     * Deducts a given amount of XP from the player, if possible.
     */
    public static void deductExperienceLevels(final Player player, final int amount) {
        if (amount <= 0) {
            return;
        }

        player.experienceLevel -= amount;

        // if (player instanceof ServerPlayer) {
        //     /// {@link ServerPlayer#completeUsingItem()}
        //     ((ServerPlayer) player).connection.send(new ClientboundEntityEventPacket(player, (byte) EntityEvent.USE_ITEM_COMPLETE));
        // }
    }

    /**
     * Calculates the player's total experience points, including progress towards the next level.
     */
    public static int getTotalPlayerExperiencePoints(final Player player) {
        return (int) (getExperiencePointsFromLevel(player.experienceLevel) + player.experienceProgress * player.getXpNeededForNextLevel());
    }

    /**
     * Adds a specified number of experience points to the player.
     */
    public static void addExperiencePoints(final Player player, final int amount) {
        final int experience = getTotalPlayerExperiencePoints(player) + amount;
        player.totalExperience = experience;
        player.experienceLevel = getLevelFromExperiencePoints(experience);
        final int expForLevel = getExperiencePointsFromLevel(player.experienceLevel);
        player.experienceProgress = (float) (experience - expForLevel) / (float) player.getXpNeededForNextLevel();
    }

    /**
     * Adds a specified number of experience levels to the player.
     */
    public static void addExperienceLevels(final Player player, final int amount) {
        player.experienceLevel += amount;
    }

    /** Get the maximum points possible at a specified level. */
    public static int getHighestExperienceAtLevel(final int level) {

        if (level >= 30) {
            return 112 + (level - 30) * 9;
        }
        else if (level >= 15) {
            return 37 + (level - 15) * 5;
        }

        return 7 + level * 2;
    }

    /** Converts XP level to XP points */
    public static int getExperiencePointsFromLevel(final int level) {

        if (level == 0) {
            return 0;
        }
        else if (level <= 15) {
            return arithmeticSeriesSum(level, 7, 2);
        }
        else if (level <= 30) {
            return 315 + arithmeticSeriesSum(level - 15, 37, 5);
        }

        return 1395 + arithmeticSeriesSum(level - 30, 112, 9);
    }

    /** Converts XP points to XP level. */
    public static int getLevelFromExperiencePoints(int amount) {
        int level = 0;
        while (true) {
            final int xpToNextLevel = getHighestExperienceAtLevel(level);
            if (amount < xpToNextLevel) {
                return level;
            }
            level++;
            amount -= xpToNextLevel;
        }
    }

    /**
     * @param n The numbers of terms in the series to sum
     * @param a The first term of the arithmetic series
     * @param d The difference between terms in the series
     */
    private static int arithmeticSeriesSum(final int n, final int a, final int d) {
        return (n/2) * (2 * a + (n - 1) * d);
    }
}
