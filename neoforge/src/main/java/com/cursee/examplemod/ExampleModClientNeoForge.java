package com.cursee.examplemod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Consumer;

public class ExampleModClientNeoForge {

    public ExampleModClientNeoForge(final IEventBus modEventBus) {

        modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> {
            event.enqueueWork(() -> {
                ExampleModClient.init();
            });
        });
    }
}
