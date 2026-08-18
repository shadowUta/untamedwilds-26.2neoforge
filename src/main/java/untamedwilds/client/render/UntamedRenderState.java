package untamedwilds.client.render;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Mob;

/** Render-state bridge for the legacy entity-driven Citadel models. */
public final class UntamedRenderState extends LivingEntityRenderState {
    public Mob entity;
    public float partialTick;
}