package com.github.alexthe666.citadel.client.model.basic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;

/**
 * Legacy Citadel entity-model contract retained for entity-driven models.
 * Minecraft 26.2's EntityModel is render-state based and can no longer be a
 * compatible superclass, so renderers bridge this model explicitly.
 */
public abstract class BasicEntityModel<T extends Entity> {
    public int textureWidth = 64;
    public int textureHeight = 32;

    protected BasicEntityModel() {
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, int color) {
        this.parts().forEach((part) -> part.render(poseStack, vertexConsumer, packedLightIn, packedOverlayIn, color));
    }

    public abstract Iterable<BasicModelPart> parts();

    public abstract void setupAnim(T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_);

    public void prepareMobModel(T p_102614_, float p_102615_, float p_102616_, float p_102617_) {
    }
}