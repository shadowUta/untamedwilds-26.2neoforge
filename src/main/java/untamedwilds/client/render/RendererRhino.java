package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelRhino;
import untamedwilds.client.model.ModelRhinoCalf;
import untamedwilds.entity.mammal.EntityRhino;

import javax.annotation.Nonnull;

public class RendererRhino extends UntamedMobRenderer<EntityRhino> {

    private static final ModelRhino RHINO_MODEL = new ModelRhino();
    private static final ModelRhinoCalf RHINO_MODEL_CALF = new ModelRhinoCalf();

    public RendererRhino(EntityRendererProvider.Context renderManager) {
        super(renderManager, RHINO_MODEL, 1F);
    }

    protected void scale(EntityRhino entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        //f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f;
    }

    public @NotNull Identifier getTextureLocation(EntityRhino entity) {
        return entity.getTexture();
    }
}
