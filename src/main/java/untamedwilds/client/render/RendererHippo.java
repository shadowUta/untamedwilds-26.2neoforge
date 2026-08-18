package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelHippo;
import untamedwilds.client.model.ModelHippoCalf;
import untamedwilds.entity.mammal.EntityHippo;

import javax.annotation.Nonnull;

public class RendererHippo extends UntamedMobRenderer<EntityHippo> {

    private static final ModelHippo HIPPO_MODEL = new ModelHippo();
    private static final ModelHippoCalf HIPPO_MODEL_CALF = new ModelHippoCalf();

    public RendererHippo(EntityRendererProvider.Context renderManager) {
        super(renderManager, HIPPO_MODEL, 1F);
    }

    protected void scale(EntityHippo entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        //f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f;
    }

    public @NotNull Identifier getTextureLocation(EntityHippo entity) {
        return entity.getTexture();
    }
}
