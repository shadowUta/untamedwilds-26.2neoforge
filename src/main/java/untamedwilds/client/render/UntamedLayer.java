package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Mob;

public interface UntamedLayer<T extends Mob> {
    void submit(UntamedMobRenderer<T> renderer, T entity, EntityRenderState state,
                PoseStack pose, SubmitNodeCollector collector);
}