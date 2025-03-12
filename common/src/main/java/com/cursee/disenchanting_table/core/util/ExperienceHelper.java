package com.cursee.disenchanting_table.core.util;

public class ExperienceHelper {

    public static int totalPointsFromLevelAndProgress(int experienceLevel, float experienceProgress) {

        if (experienceLevel < 0 || experienceProgress < 0) return 0;

        return totalPointsFromLevel(experienceLevel) + totalPointsFromProgress(experienceLevel, experienceProgress);
    }

    public static int totalPointsFromLevel(int experienceLevel) {

        if (experienceLevel < 0) return  0;

        if (experienceLevel <= 16) {
            return (int) (Math.pow(experienceLevel, 2) + 6 * experienceLevel);
        }
        else if (experienceLevel <= 31) {
            return (int) (2.5 * Math.pow(experienceLevel, 2) - 40.5 * experienceLevel + 360);
        }
        else {
            return (int) (4.5 * Math.pow(experienceLevel, 2) - 162.5 * experienceLevel + 2220);
        }
    }

    public static int totalPointsFromProgress(int experienceLevel, float experienceProgress) {

        int pointsAtCurrentLevel = totalPointsFromLevel(experienceLevel);
        int pointsAtNextLevel = totalPointsFromLevel(experienceLevel + 1);
        int totalFromCurrentToNext = pointsAtNextLevel - pointsAtCurrentLevel;

        return (int) (totalFromCurrentToNext * experienceProgress);
    }
}
