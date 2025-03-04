package com.cursee.disenchanting_table.mixin;

import com.cursee.disenchanting_table.Constants;
import com.cursee.disenchanting_table.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class ForgeTitleScreenMixin {

    @Inject(at = @At("HEAD"), method = "init")
    private void init(CallbackInfo info) {

        if (Services.PLATFORM.isDevelopmentEnvironment()) {
            Constants.LOG.info("This line is printed by an example mod mixin from Forge!");
            Constants.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
        }
    }
}