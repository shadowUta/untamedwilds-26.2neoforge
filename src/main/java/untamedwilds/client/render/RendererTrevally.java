package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelTrevally;
import untamedwilds.entity.fish.EntityTrevally;

import javax.annotation.Nonnull;

public class RendererTrevally extends UntamedMobRenderer<EntityTrevally> {

    private static final ModelTrevally TREVALLY_MODEL = new ModelTrevally();

    public RendererTrevally(EntityRendererProvider.Context rendermanager) {
        super(rendermanager, TREVALLY_MODEL, 0.2F);
    }
    @Override
    protected void scale(EntityTrevally entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize() * 0.8F;
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.5F;
    }

    public @NotNull Identifier getTextureLocation(EntityTrevally entity) {
        return entity.getTexture();
    }
}
