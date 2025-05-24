package com.cursee.disenchanting_table.core;

import com.cursee.disenchanting_table.Constants;
import com.cursee.monolib.platform.Services;
import com.cursee.monolib.util.toml.Toml;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedList;

public class ServerConfig {

    public static boolean automatic_disenchanting = false;
    public static boolean resets_repair_cost = true;

    public static boolean requires_experience = true;
    public static boolean uses_points = true;
    public static int experience_cost = 25;

    private static final String FILE_SUFFIX = Constants.MOD_ID + "-server";
    private static final String CONFIG_DIR_FILEPATH = Services.PLATFORM.getGameDirectory() + File.separator + "config";

    public static void onLoad() {

        final File CONFIG_DIR = new File(CONFIG_DIR_FILEPATH);
        if (!CONFIG_DIR.isDirectory() && !CONFIG_DIR.mkdirs()) {
            throw new RuntimeException("Unable to access or create directory: " + CONFIG_DIR_FILEPATH);
        }

        handle(new File(CONFIG_DIR_FILEPATH + File.separator + FILE_SUFFIX + ".toml"));
    }

    private static final LinkedList<String> DEFAULTS = new LinkedList<>();
    private static void handle(File file) {

        if (!file.isFile()) {
            loadDefaults();
            try (PrintWriter writer = new PrintWriter(file)) {
                DEFAULTS.forEach(writer::println);
            }
            catch (Exception e) {
                System.out.println("Filed to write " + file.getAbsolutePath());
                System.out.println(e.getMessage());
            }
        }
        else {
            Toml toml = new Toml().read(file);
            automatic_disenchanting = toml.getBoolean("automatic_disenchanting");
            resets_repair_cost = toml.getBoolean("resets_repair_cost");

            requires_experience = toml.getBoolean("requires_experience");
            uses_points = toml.getBoolean("uses_points");
            experience_cost = Math.toIntExact(toml.getLong("experience_cost"));
        }
    }

    private static void loadDefaults() {
        DEFAULTS.add("# automatic_disenchanting enables hoppers interactions with the block, default = false");
        DEFAULTS.add("automatic_disenchanting = false");
        DEFAULTS.add(" ");
        DEFAULTS.add("# resets_repair_cost enables resetting the repair cost of items, default = true");
        DEFAULTS.add("resets_repair_cost = true");
        DEFAULTS.add(" ");
        DEFAULTS.add("# requires_experience enables taking experience when disenchanting, default = true");
        DEFAULTS.add("requires_experience = true");
        DEFAULTS.add(" ");
        DEFAULTS.add("# uses_points can be false to use levels instead, default = true");
        DEFAULTS.add("uses_points = true");
        DEFAULTS.add(" ");
        DEFAULTS.add("# experience_cost how many experience points or levels to use for disenchanting, default = 25");
        DEFAULTS.add("experience_cost = 25");
    }
}
