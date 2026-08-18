package untamedwilds.client.render;

import com.github.alexthe666.citadel.client.model.basic.BasicEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import java.util.ArrayList;
import java.util.List;

/**
 * Minimal 26.2 adapter.  The model remains the real Citadel model; only the
 * vanilla renderer's entity argument is bridged to a RenderState.
 */
public abstract class UntamedMobRenderer<T extends Mob> extends EntityRenderer<T, UntamedRenderState> {
    private final BasicEntityModel<T> defaultModel;
    protected BasicEntityModel<T> model;
    private final List<UntamedLayer<T>> untamedLayers = new ArrayList<>();

    @SuppressWarnings("unchecked")
    protected UntamedMobRenderer(EntityRendererProvider.Context context, BasicEntityModel<?> model, float shadowRadius) {
        super(context);
        this.defaultModel = (BasicEntityModel<T>) model;
        this.model = this.defaultModel;
        this.shadowRadius = shadowRadius;
    }

    @Override
    public UntamedRenderState createRenderState() {
        return new UntamedRenderState();
    }

    @Override
    public void extractRenderState(T entity, UntamedRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.entity = entity;
        state.partialTick = partialTick;
        state.hasRedOverlay = entity.hurtTime > 0 || entity.deathTime > 0;
        state.bodyRot = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
        float headRot = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
        state.yRot = Mth.wrapDegrees(headRot - state.bodyRot);
        state.xRot = entity.getXRot(partialTick);
        if (!entity.isPassenger() && entity.isAlive()) {
            state.walkAnimationPos = entity.walkAnimation.position(partialTick);
            state.walkAnimationSpeed = entity.walkAnimation.speed(partialTick);
        } else {
            state.walkAnimationPos = 0.0F;
            state.walkAnimationSpeed = 0.0F;
        }
    }

    @SuppressWarnings("unchecked")
    public BasicEntityModel<T> getUntamedModel(T entity) {
        return model;
    }

    protected void scale(T entity, PoseStack pose, float partialTick) {
    }

    protected final void addLayer(UntamedLayer<T> layer) {
        untamedLayers.add(layer);
    }

    @Override
    public void submit(UntamedRenderState state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        T entity = entity(state);
        BasicEntityModel<T> model = getUntamedModel(entity);
        pose.pushPose();
        pose.scale(state.scale, state.scale, state.scale);
        pose.mulPose(Axis.YP.rotationDegrees(180.0F - state.bodyRot));
        pose.scale(-1.0F, -1.0F, 1.0F);
        scale(entity, pose, state.partialTick);
        pose.translate(0.0F, -1.501F, 0.0F);
        model.setupAnim(entity, state.walkAnimationPos, state.walkAnimationSpeed, state.ageInTicks,
            state.yRot, state.xRot);
        Identifier texture = texture(entity);
        int overlayCoords = OverlayTexture.pack(OverlayTexture.u(0.0F),
            OverlayTexture.v(state.hasRedOverlay));
        collector.submitCustomGeometry(pose, RenderTypes.entityCutout(texture), (currentPose, vertexConsumer) ->
            model.renderToBuffer(poseStack(currentPose), vertexConsumer, state.lightCoords,
                overlayCoords, -1));
        for (UntamedLayer<T> layer : untamedLayers) {
            layer.submit(this, entity, state, pose, collector);
        }
        pose.popPose();
        super.submit(state, pose, collector, camera);
    }

    @SuppressWarnings("unchecked")
    private T entity(UntamedRenderState state) { return (T) state.entity; }

    public static PoseStack poseStack(PoseStack.Pose pose) {
        PoseStack poseStack = new PoseStack();
        poseStack.last().set(pose);
        return poseStack;
    }

    private Identifier texture(T entity) {
        try {
            return (Identifier) entity.getClass().getMethod("getTexture").invoke(entity);
        } catch (ReflectiveOperationException e) {
            return Identifier.withDefaultNamespace("textures/entity/pig/pig.png");
        }
    }

    public Identifier getTextureLocation(UntamedRenderState state) {
        return texture(entity(state));
    }

    public Identifier getTextureLocation(T entity) { return texture(entity); }
}