package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelBison;
import untamedwilds.client.model.ModelBisonCalf;
import untamedwilds.entity.mammal.EntityBison;

import javax.annotation.Nonnull;

public class RendererBison extends UntamedMobRenderer<EntityBison> {

    private static final ModelBison BISON_MODEL = new ModelBison();
    private static final ModelBisonCalf BISON_CALF_MODEL = new ModelBisonCalf();

    public RendererBison(EntityRendererProvider.Context renderManager) {
        super(renderManager, BISON_MODEL, 1F);
    }

    protected void scale(EntityBison entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.6F;
    }

    public @NotNull Identifier getTextureLocation(EntityBison entity) {
        return entity.getTexture();
    }
}
