package com.cursee.disenchanting_table.core;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.platform.Services;
import com.cursee.monolib.core.config.SimpleConfigEntry;
import com.cursee.monolib.util.toml.Toml;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CommonConfigForge {

    public static final SimpleConfigEntry<AtomicBoolean> REQUIRES_EXPERIENCE = new SimpleConfigEntry<>("requires_experience", new AtomicBoolean(true));
    public static final SimpleConfigEntry<AtomicBoolean> RESET_REPAIR_COST = new SimpleConfigEntry<>("reset_repair_cost", new AtomicBoolean(true));
    public static final SimpleConfigEntry<AtomicReference<String>> POINTS_OR_LEVELS = new SimpleConfigEntry<>("points_or_levels", new AtomicReference<String>("points"));
    public static final SimpleConfigEntry<AtomicReference<Integer>> EXPERIENCE_COST = new SimpleConfigEntry<>("experience_cost", new AtomicReference<Integer>(Math.toIntExact(25L)));

    public static void onCommonLoaded() {
        new File(Services.PLATFORM.getGameDirectory() + File.separator + "config").mkdirs();
        File COMMON_CONFIG_FILE = new File(Services.PLATFORM.getGameDirectory() + File.separator + "config" + File.separator + Constants.MOD_ID + "-common.toml");
        handleCommonConfig(COMMON_CONFIG_FILE);
    }

    private static final String[] DEFAULT_COMMON_CONFIG = new String[] {
            "requires_experience = true",
            "reset_repair_cost = true",
            "points_or_levels = \"points\"",
            "experience_cost = 25"
    };

    public static void handleCommonConfig(File file) {

        if (!file.isFile()) {
            try {

                PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
                for (String s : DEFAULT_COMMON_CONFIG) {
                    writer.println(s);
                }
                writer.close();

                BufferedWriter bufferedWriter = appendCommonComments(file);
                bufferedWriter.close();
            }
            catch (Exception ignored) {}

            return;
        }

        try {
            Toml toml = new Toml().read(file);
            REQUIRES_EXPERIENCE.get().set(toml.getBoolean("requires_experience"));
            RESET_REPAIR_COST.get().set(toml.getBoolean("reset_repair_cost"));
            POINTS_OR_LEVELS.get().set(toml.getString("points_or_levels"));
            EXPERIENCE_COST.get().set(Math.toIntExact(toml.getLong("experience_cost")));

            // CommonConfiguredValues.REQUIRES_EXPERIENCE.set(REQUIRES_EXPERIENCE.get().get());
            // CommonConfiguredValues.RESET_REPAIR_COST.set(RESET_REPAIR_COST.get().get());
            // CommonConfiguredValues.POINTS_OR_LEVELS.set(POINTS_OR_LEVELS.get().get());
            // CommonConfiguredValues.EXPERIENCE_COST.set(EXPERIENCE_COST.get().get());
        }
        catch (Exception exception) {}
    }

    private static @NotNull BufferedWriter appendCommonComments(File file) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        bufferedWriter.newLine();
        bufferedWriter.append(" # requires_experience determines if disenchanting has any cost\n");
        bufferedWriter.append(" # resets_anvil_cost determines if the anvil should reset an item's added anvil cost\n");
        bufferedWriter.append(" # points_or_levels determines the cost type\n");
        bufferedWriter.append(" # experience_cost determines the amount of the cost type\n");
        bufferedWriter.flush();
        return bufferedWriter;
    }
}
