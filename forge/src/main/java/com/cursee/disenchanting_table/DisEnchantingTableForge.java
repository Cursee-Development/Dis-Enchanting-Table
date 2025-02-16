package com.cursee.disenchanting_table;

import com.cursee.disenchanting_table.core.network.ModNetworkForge;
import com.cursee.disenchanting_table.core.registry.RegistryForge;
import com.cursee.disenchanting_table.core.world.block.DisEnchantingTableBlock;
import com.cursee.disenchanting_table.core.world.block.entity.HopperDisEnchantingTableBlockEntity;
import com.cursee.monolib.core.sailing.Sailing;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class DisEnchantingTableForge {

    public static IEventBus EVENT_BUS = null;
    
    public DisEnchantingTableForge() {
        DisEnchantingTable.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        DisEnchantingTableForge.EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        RegistryForge.register(DisEnchantingTableForge.EVENT_BUS);
        DisEnchantingTableForge.EVENT_BUS.addListener((Consumer<FMLCommonSetupEvent>) event -> event.enqueueWork(ModNetworkForge::registerS2CPackets));
    }
}