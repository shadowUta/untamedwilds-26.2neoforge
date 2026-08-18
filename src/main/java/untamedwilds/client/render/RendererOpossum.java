package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelOpossum;
import untamedwilds.entity.mammal.EntityOpossum;

public class RendererOpossum extends UntamedMobRenderer<EntityOpossum> {

    private static final ModelOpossum OPOSSUM_MODEL = new ModelOpossum();

    public RendererOpossum(EntityRendererProvider.Context renderManager) {
        super(renderManager, OPOSSUM_MODEL, 0.4F);
    }

    protected void scale(EntityOpossum entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.4F;
    }

    public @NotNull Identifier getTextureLocation(EntityOpossum entity) {
        return entity.getTexture();
    }
}
