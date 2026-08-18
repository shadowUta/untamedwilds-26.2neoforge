package untamedwilds.client.render;

import com.github.alexthe666.citadel.client.model.basic.BasicEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;
import java.lang.reflect.Method;
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

    private void invokeScale(T entity, PoseStack pose, float partialTick) {
        try {
            Method method = getClass().getDeclaredMethod("scale", entity.getClass(), PoseStack.class, float.class);
            method.setAccessible(true);
            method.invoke(this, entity, pose, partialTick);
        } catch (ReflectiveOperationException ignored) {
            // Renderers without a custom scale retain the vanilla transform.
        }
    }

    @Override
    public void submit(UntamedRenderState state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        T entity = entity(state);
        BasicEntityModel<T> model = getUntamedModel(entity);
        pose.pushPose();
        pose.scale(state.scale, state.scale, state.scale);
        pose.scale(-1.0F, -1.0F, 1.0F);
        invokeScale(entity, pose, state.partialTick);
        pose.translate(0.0F, -1.501F, 0.0F);
        model.setupAnim(entity, state.walkAnimationPos, state.walkAnimationSpeed, state.ageInTicks,
            state.yRot, state.xRot);
        Identifier texture = texture(entity);
        collector.submitCustomGeometry(pose, RenderTypes.entityCutout(texture), (currentPose, vertexConsumer) ->
            model.renderToBuffer(pose, vertexConsumer, state.lightCoords,  Overlay.NO_OVERLAY, -1));
        for (UntamedLayer<T> layer : untamedLayers) {
            layer.submit(this, entity, state, pose, collector);
        }
        pose.popPose();
        super.submit(state, pose, collector, camera);
    }

    @SuppressWarnings("unchecked")
    private T entity(UntamedRenderState state) { return (T) state.entity; }

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

    private static final class Overlay {
        private static final int NO_OVERLAY = 0;
    }
}