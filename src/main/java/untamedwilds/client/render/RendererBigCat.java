package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelBigCat;
import untamedwilds.client.model.ModelBigCatCub;
import untamedwilds.entity.mammal.EntityBigCat;

public class RendererBigCat extends UntamedMobRenderer<EntityBigCat> {

    private static final ModelBigCat BIG_CAT_MODEL = new ModelBigCat();
    private static final ModelBigCatCub BIG_CAT_MODEL_CUB = new ModelBigCatCub();

    public RendererBigCat(EntityRendererProvider.Context renderManager) {
        super(renderManager, BIG_CAT_MODEL, 1F);
    }

    protected void scale(EntityBigCat entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        //f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f;
    }

    @Override
    public @NotNull Identifier getTextureLocation(EntityBigCat entity) {
        return entity.getTexture();
    }
}
