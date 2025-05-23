package com.cursee.disenchanting_table.core;

import com.cursee.monolib.Constants;
import com.cursee.monolib.platform.Services;
import com.cursee.monolib.util.toml.Toml;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedList;

public class CommonConfig {

    private static final String FILE_SUFFIX = Constants.MOD_ID + "-common";
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
//            CommonConfigValues.automatic_disenchanting = toml.getBoolean("automatic_disenchanting");
        }
    }

    private static void loadDefaults() {
//        DEFAULTS.add("# automatic_disenchanting enables hoppers interactions with the block, default = false");
//        DEFAULTS.add("automatic_disenchanting = false");
    }
}
