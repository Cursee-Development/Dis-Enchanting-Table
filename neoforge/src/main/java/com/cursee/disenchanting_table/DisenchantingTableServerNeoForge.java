package com.cursee.disenchanting_table;

import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

public class DisenchantingTableServerNeoForge {

    public static MinecraftServer SERVER;

    public DisenchantingTableServerNeoForge(ServerAboutToStartEvent event) {
        SERVER = event.getServer();
        DisenchantingTableServer.init();
    }

    public static MinecraftServer getServer() {
        return SERVER;
    }
}
