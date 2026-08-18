package untamedwilds.client.layer;

import com.github.alexthe666.citadel.client.model.basic.BasicEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import untamedwilds.UntamedWilds;
import untamedwilds.client.render.UntamedLayer;
import untamedwilds.client.render.UntamedMobRenderer;
import untamedwilds.entity.fish.EntityFootballFish;

public class LayerFootballFishLure<T extends EntityFootballFish> implements UntamedLayer<T> {

    private final RenderType TEXTURE = net.minecraft.client.renderer.rendertype.RenderTypes.eyes(Identifier.fromNamespaceAndPath(UntamedWilds.MOD_ID, "textures/entity/football_fish/glint.png"));

    public LayerFootballFishLure(UntamedMobRenderer<T> rendererIn) {
    }

    private RenderType renderType() {
        return TEXTURE;
    }

    @Override
    public void submit(UntamedMobRenderer<T> renderer, T entity, EntityRenderState state,
                       PoseStack pose, SubmitNodeCollector collector) {
        BasicEntityModel<T> model = renderer.getUntamedModel(entity);
        collector.submitCustomGeometry(pose, renderType(),
            (currentPose, vertexConsumer) -> model.renderToBuffer(
                UntamedMobRenderer.poseStack(currentPose), vertexConsumer, state.lightCoords,
                OverlayTexture.NO_OVERLAY, -1));
    }
}
