package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import untamedwilds.client.model.ModelProjectileSpit;
import untamedwilds.entity.ProjectileSpit;

@OnlyIn(Dist.CLIENT)
public class RendererProjectileSpit extends EntityRenderer<ProjectileSpit, ProjectileSpitRenderState> {
    private static final Identifier LLAMA_SPIT_LOCATION = Identifier.withDefaultNamespace("textures/entity/llama/spit.png");
    private final ModelProjectileSpit model;

    public RendererProjectileSpit(EntityRendererProvider.Context p_174296_) {
        super(p_174296_);
        this.model = new ModelProjectileSpit(p_174296_.bakeLayer(ModelLayers.LLAMA_SPIT));
    }

    public void submit(ProjectileSpitRenderState state, PoseStack p_115376_, SubmitNodeCollector p_115377_, CameraRenderState camera) {
        p_115376_.pushPose();
        p_115376_.translate(0.0D, (double)0.15F, 0.0D);
        this.model.setupAnim(state);
        p_115377_.submitModel(this.model, state, p_115376_, RenderTypes.entityCutout(LLAMA_SPIT_LOCATION), state.lightCoords, 0, -1, null, state.outlineColor, null);
        p_115376_.popPose();
    }

    @Override
    public ProjectileSpitRenderState createRenderState() {
        return new ProjectileSpitRenderState();
    }

    @Override
    public void extractRenderState(ProjectileSpit entity, ProjectileSpitRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.yRot = Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F;
        state.xRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
    }

    public Identifier getTextureLocation(ProjectileSpitRenderState state) {
        return LLAMA_SPIT_LOCATION;
    }
}