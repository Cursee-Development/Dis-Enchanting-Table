package com.cursee.disenchanting_table.client;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.platform.Services;
import com.cursee.monolib.util.toml.Toml;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ForgeClientConfigHandler {

    public static void onLoad() {
        new File(Services.PLATFORM.getGameDirectory() + File.separator + "config").mkdirs();
        File CONFIG_FILE = new File(Services.PLATFORM.getGameDirectory() + File.separator + "config" + File.separator + Constants.MOD_ID + "-client.toml");
        handleConfig(CONFIG_FILE);
    }

    private static final String[] DEFAULT_CONFIG = new String[] {
            "render_ender_particles = true",
            "# setting this to \"false\" will stop particles from rendering around the disenchanting table",

            "experience_indicator = true",
            "# setting this to \"false\" will cause the \"Insufficient Experience\" text to not display",

            "render_table_item = true",
            "# setting this to \" false\" will stop input/output items from displaying on the block"
    };

    private static void handleConfig(File file) {
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
            ClientConfigValues.render_ender_particles = toml.getBoolean("render_ender_particles");
            ClientConfigValues.experience_indicator = toml.getBoolean("experience_indicator");
            ClientConfigValues.render_table_item = toml.getBoolean("render_table_item");
        }
        catch (Exception ignored) {}
    }
}
