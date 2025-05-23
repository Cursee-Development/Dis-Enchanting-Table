package com.cursee.disenchanting_table.client;

import com.cursee.disenchanting_table.Constants;
import com.cursee.monolib.platform.Services;
import com.cursee.monolib.util.toml.Toml;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedList;

public class ClientConfig {

    public static boolean render_block_particles = true;
    public static boolean render_experience_cost = true;
    public static boolean render_table_item = true;

    private static final String FILE_SUFFIX = Constants.MOD_ID + "-client";
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
            render_block_particles = toml.getBoolean("render_block_particles");
            render_experience_cost = toml.getBoolean("render_experience_cost");
            render_table_item = toml.getBoolean("render_table_item");
        }
    }

    private static void loadDefaults() {
        DEFAULTS.add("# render_block_particles should particles be rendered from the block?, default = true");
        DEFAULTS.add("render_block_particles = true");
        DEFAULTS.add(" ");
        DEFAULTS.add("# render_experience_cost should the menu show Insufficient Experience display?, default = true");
        DEFAULTS.add("render_experience_cost = true");
        DEFAULTS.add(" ");
        DEFAULTS.add("# render_table_item should the current item be displayed for automatic disenchanting?, default = true");
        // DEFAULTS.add("# (if automatic disenchanting is disabled, only an enchanted book will be displayed on the block)");
        // todo menus menus screens menus screens
        DEFAULTS.add("render_table_item = true");
    }
}
