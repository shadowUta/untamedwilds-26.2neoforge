package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelManatee;
import untamedwilds.entity.mammal.EntityManatee;

public class RendererManatee extends UntamedMobRenderer<EntityManatee> {

    private static final ModelManatee MANATEE_MODEL = new ModelManatee();
    private static final ModelManatee MANATEE_MODEL_CALF = new ModelManatee();

    public RendererManatee(EntityRendererProvider.Context renderManager) {
        super(renderManager, MANATEE_MODEL, 1F);
    }

    protected void scale(EntityManatee entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f;
    }

    public @NotNull Identifier getTextureLocation(EntityManatee entity) {
        return entity.getTexture();
    }
}
