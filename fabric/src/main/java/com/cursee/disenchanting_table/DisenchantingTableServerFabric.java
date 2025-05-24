package com.cursee.disenchanting_table;

import net.minecraft.server.MinecraftServer;

public class DisenchantingTableServerFabric {

    public static MinecraftServer SERVER;

    public DisenchantingTableServerFabric(MinecraftServer server) {
        SERVER = server;
        DisenchantingTableServer.init();
    }

    public MinecraftServer getServer() {
        return SERVER;
    }
}
