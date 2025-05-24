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

    private static final String FILE_SUFFIXED = Constants.MOD_ID + "-client";
    private static final String CONFIG_DIR_FILEPATH = Services.PLATFORM.getGameDirectory() + File.separator + "config";

    public static void onLoad() {

        final File CONFIG_DIR = new File(CONFIG_DIR_FILEPATH);
        if (!CONFIG_DIR.isDirectory() && !CONFIG_DIR.mkdirs()) {
            throw new RuntimeException("Unable to access or create directory: " + CONFIG_DIR_FILEPATH);
        }

        handle(new File(CONFIG_DIR_FILEPATH + File.separator + FILE_SUFFIXED + ".toml"));
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

            // handle deprecated config
            if (toml.getBoolean("render_block_particles") == null) {
                Constants.LOG.info("Failed to read key \"render_block_particles\" from {}", file.getName());
                Constants.LOG.info("Attempting to read deprecated key \"render_ender_particles\" from {}", file.getName());

                if (toml.getBoolean("render_ender_particles") == null) {
                    Constants.LOG.info("Failed to read deprecated key \"render_ender_particles\" from {}", file.getName());
                    Constants.LOG.info("Setting client config value \"render_block_particles\" to default: true");
                }
                else {
                    render_block_particles = true;
                }
            }
            else {
                render_block_particles = toml.getBoolean("render_block_particles");
            }

            // handle deprecated config
            if (toml.getBoolean("render_experience_cost") == null) {
                Constants.LOG.info("Failed to read key \"render_experience_cost\" from {}", file.getName());
                Constants.LOG.info("Attempting to read deprecated key \"experience_indicator\" from {}", file.getName());

                if (toml.getBoolean("experience_indicator") == null) {
                    Constants.LOG.info("Failed to read deprecated key \"experience_indicator\" from {}", file.getName());
                    Constants.LOG.info("Setting client config value \"render_experience_cost\" to default: true");
                }
                else {
                    render_experience_cost = true;
                }
            }
            else {
                render_experience_cost = toml.getBoolean("render_experience_cost");
            }

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
