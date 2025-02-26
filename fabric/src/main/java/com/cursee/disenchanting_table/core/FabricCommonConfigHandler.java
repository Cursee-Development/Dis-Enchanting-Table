package com.cursee.disenchanting_table.core;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.platform.Services;
import com.cursee.monolib.util.toml.Toml;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class FabricCommonConfigHandler {

    public static void onLoad() {
        new File(Services.PLATFORM.getGameDirectory() + File.separator + "config").mkdirs();
        File CONFIG_FILE = new File(Services.PLATFORM.getGameDirectory() + File.separator + "config" + File.separator + Constants.MOD_ID + "-common.toml");
        handleConfig(CONFIG_FILE);
    }

    private static final String[] DEFAULT_CONFIG = new String[] {
            "automatic_disenchanting = false",
            "# setting this to \"true\" will cause the disenchanting table to operate automatically, enables hopper behavior, and takes experience from the nearest player"
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
        }
        catch (Exception ignored) {}
    }
}
