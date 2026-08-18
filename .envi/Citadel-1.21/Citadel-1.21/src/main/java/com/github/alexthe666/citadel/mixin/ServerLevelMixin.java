package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.server.tick.ServerTickRateTracker;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @ModifyExpressionValue(
            method = "tickTime",
            remap = CitadelConstants.REMAPREFS,
            at = @At(value = "CONSTANT", args = "longValue=1"),
            expect = 2)
    private long citadel_clientSetDayTime(long timeIn) {
        return ServerTickRateTracker.getForServer(server).getDayTimeIncrement(timeIn);
    }
}
