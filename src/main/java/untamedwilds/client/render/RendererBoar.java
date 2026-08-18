package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelBoar;
import untamedwilds.client.model.ModelBoarPiglet;
import untamedwilds.client.model.ModelWarthog;
import untamedwilds.entity.mammal.EntityBoar;

public class RendererBoar extends UntamedMobRenderer<EntityBoar> {

    private static final ModelBoar BOAR_MODEL = new ModelBoar();
    private static final ModelWarthog WARTHOG_MODEL = new ModelWarthog();
    private static final ModelBoarPiglet BOAR_MODEL_PIGLET = new ModelBoarPiglet();

    public RendererBoar(EntityRendererProvider.Context renderManager) {
        super(renderManager, BOAR_MODEL, 0.4F);
    }

    protected void scale(EntityBoar entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.isBaby() ? 0.8F : 1.0F;
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.6F;
    }

    public @NotNull Identifier getTextureLocation(EntityBoar entity) {
        return entity.getTexture();
    }
}
