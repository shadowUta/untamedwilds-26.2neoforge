package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.server.world.ModifiableTickRateServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTickRateManager;

/**
 * In 1.21+, Minecraft uses TickRateManager with setTickRate() instead of hardcoded 50L constants.
 * We use the native API to modify tick rate instead of @ModifyConstant.
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements ModifiableTickRateServer {

    @Shadow @Final private ServerTickRateManager tickRateManager;

    @Unique
    private long citadel$modifiedMsPerTick = -1;
    @Unique
    private long citadel$masterMs;
    @Unique
    private float citadel$originalTickRate = 20.0F;
    @Unique
    private boolean citadel$hasStoredOriginalTickRate = false;

    @Inject(
            method = "runServer",
            remap = CitadelConstants.REMAPREFS,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;startMetricsRecordingTick()V",
                    shift = At.Shift.BEFORE
            )
    )
    protected void citadel_beforeServerTick(CallbackInfo ci) {
        masterTick();
    }

    @Unique
    private void masterTick() {
        citadel$masterMs += 50L;
    }

    @Override
    public void setGlobalTickLengthMs(long msPerTick) {
        this.citadel$modifiedMsPerTick = msPerTick;
        
        if (msPerTick == -1) {
            // Reset to original tick rate (20 TPS = 50ms per tick)
            if (citadel$hasStoredOriginalTickRate) {
                tickRateManager.setTickRate(citadel$originalTickRate);
            } else {
                tickRateManager.setTickRate(20.0F);
            }
        } else {
            // Store original tick rate before modifying
            if (!citadel$hasStoredOriginalTickRate) {
                citadel$originalTickRate = tickRateManager.tickrate();
                citadel$hasStoredOriginalTickRate = true;
            }
            // Convert ms per tick to ticks per second: TPS = 1000 / msPerTick
            float newTickRate = 1000.0F / msPerTick;
            tickRateManager.setTickRate(newTickRate);
        }
    }

    @Override
    public long getMasterMs() {
        return citadel$masterMs;
    }
}
