package com.cursee.examplemod.core.registry;

import com.cursee.examplemod.Constants;
import com.cursee.examplemod.ExampleMod;
import com.cursee.examplemod.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.BiConsumer;

public class ModBlocks {

    // must be registered if defined
    // public static final Block EXAMPLE_BLOCK = new Block(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY));

    public static void register(BiConsumer<Block, ResourceLocation> consumer) {
        if (Services.PLATFORM.isDevelopmentEnvironment()) {
            // consumer.accept(EXAMPLE_BLOCK, ExampleMod.identifier("example_block"));
        }
    }
}
