package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.client.tick.ClientTickRateTracker;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @ModifyReturnValue(
            method = "calculatePitch",
            remap = CitadelConstants.REMAPREFS,
            at = @At(value = "RETURN")
    )
    protected float citadel_setupRotations(float original, @Local(argsOnly = true) SoundInstance soundInstance) {
        return original * ClientTickRateTracker.getForClient(Minecraft.getInstance()).modifySoundPitch(soundInstance);
    }
}
