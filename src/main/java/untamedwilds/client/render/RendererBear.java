package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelBear;
import untamedwilds.client.model.ModelBearCub;
import untamedwilds.entity.mammal.EntityBear;

public class RendererBear extends UntamedMobRenderer<EntityBear> {

    private static final ModelBear BEAR_MODEL = new ModelBear();
    private static final ModelBearCub BEAR_MODEL_CUB = new ModelBearCub();

    public RendererBear(EntityRendererProvider.Context renderManager) {
        super(renderManager, BEAR_MODEL, 1F);
    }

    @Override
    protected void scale(EntityBear entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        //f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f;
    }

    public @NotNull Identifier getTextureLocation(EntityBear entity) {
        return entity.getTexture();
    }
}
