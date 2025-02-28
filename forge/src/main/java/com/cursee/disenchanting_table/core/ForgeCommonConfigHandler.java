package com.cursee.disenchanting_table.core;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.platform.Services;
import com.cursee.monolib.util.toml.Toml;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ForgeCommonConfigHandler {

    public static void onLoad() {
        new File(Services.PLATFORM.getGameDirectory() + File.separator + "config").mkdirs();
        File CONFIG_FILE = new File(Services.PLATFORM.getGameDirectory() + File.separator + "config" + File.separator + Constants.MOD_ID + "-common.toml");
        handleConfig(CONFIG_FILE);
    }

    private static final String[] DEFAULT_CONFIG = new String[] {
            "automatic_disenchanting = false",
            "# setting this to \"true\" will cause the disenchanting table to operate automatically, taking experience from the nearest player and enabling",

            "resets_repair_cost = true",
            "# setting this to \"false\" will stop the disenchanting table from resetting the anvil cost",

            "requires_experience = true",
            "# setting this to \"false\" will cause the disenchanting table to operate without taking experience at any point",

            "uses_points = true",
            "# setting this to \"false\" will cause the disenchanting table to require levels instead of points",

            "experience_cost = 25",
            "# this value adjusts how many levels or points are required for the player to use the disenchanting table"
    };

    public static void handleConfig(File file) {
        if (!file.isFile()) {
            try (PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8)) {
                for (String s : DEFAULT_CONFIG) {
                    writer.println(s);
                }
            }
            catch (Exception ignored) {}

            return;
        }

        try {
            Toml toml = new Toml().read(file);
            CommonConfigValues.automatic_disenchanting = toml.getBoolean("automatic_disenchanting");
            CommonConfigValues.resets_repair_cost = toml.getBoolean("resets_repair_cost");
            CommonConfigValues.requires_experience = toml.getBoolean("requires_experience");
            CommonConfigValues.uses_points = toml.getBoolean("uses_points");
            CommonConfigValues.experience_cost = Math.toIntExact(toml.getLong("experience_cost"));
        }
        catch (Exception ignored) {}
    }
}
