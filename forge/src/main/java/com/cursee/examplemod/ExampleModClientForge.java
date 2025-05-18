package com.cursee.examplemod;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Consumer;

public class ExampleModClientForge {

    public ExampleModClientForge(final IEventBus modEventBus) {

        modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> {
            event.enqueueWork(() -> {
                ExampleModClient.init();
            });
        });
    }
}
