package com.cursee.disenchanting_table;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerAboutToStartEvent;

public class DisenchantingTableServerForge {

    public static MinecraftServer SERVER;

    public DisenchantingTableServerForge(ServerAboutToStartEvent event) {
        SERVER = event.getServer();
        DisenchantingTableServer.init();
    }

    public static MinecraftServer getServer() {
        return SERVER;
    }
}
