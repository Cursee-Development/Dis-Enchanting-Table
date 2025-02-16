package com.cursee.disenchanting_table.client;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.platform.Services;
import com.cursee.monolib.core.config.SimpleConfigEntry;
import com.cursee.monolib.util.toml.Toml;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientConfigFabric {

    public static final SimpleConfigEntry<AtomicBoolean> EXPERIENCE_INDICATOR = new SimpleConfigEntry<>("experience_indicator", new AtomicBoolean(true));
    public static final SimpleConfigEntry<AtomicBoolean> RENDER_ENDER_PARTICLES = new SimpleConfigEntry<>("render_ender_particles", new AtomicBoolean(true));

    public static void onClientLoaded() {
        new File(Services.PLATFORM.getGameDirectory() + File.separator + "config").mkdirs();
        File CLIENT_CONFIG_FILE = new File(Services.PLATFORM.getGameDirectory() + File.separator + "config" + File.separator + Constants.MOD_ID + "-client.toml");
        handleClientConfig(CLIENT_CONFIG_FILE);
    }

    private static final String[] DEFAULT_CLIENT_CONFIG = new String[] {
            "experience_indicator = true",
            "render_ender_particles = true"
    };

    public static void handleClientConfig(File file) {

        if (!file.isFile()) {
            try {

                PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
                for (String s : DEFAULT_CLIENT_CONFIG) {
                    writer.println(s);
                }
                writer.close();

                BufferedWriter bufferedWriter = appendClientComments(file);
                bufferedWriter.close();
            }
            catch (Exception ignored) {}

            return;
        }

        try {
            Toml toml = new Toml().read(file);
            EXPERIENCE_INDICATOR.get().set(toml.getBoolean("experience_indicator"));
            RENDER_ENDER_PARTICLES.get().set(toml.getBoolean("render_ender_particles"));

            ClientConfiguredValues.EXPERIENCE_INDICATOR.set(EXPERIENCE_INDICATOR.get().get());
            ClientConfiguredValues.RENDER_ENDER_PARTICLES.set(RENDER_ENDER_PARTICLES.get().get());
        }
        catch (Exception exception) {}
    }

    private static @NotNull BufferedWriter appendClientComments(File file) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        bufferedWriter.newLine();
        bufferedWriter.append(" # experience_indicator displays can tell you if you don't have enough experience\n");
        bufferedWriter.append(" # render_ender_particles determines if the block renders particles surrounding it\n");
        bufferedWriter.flush();
        return bufferedWriter;
    }
}
