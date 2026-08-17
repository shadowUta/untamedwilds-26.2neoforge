package untamedwilds.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import untamedwilds.UntamedWilds;
import untamedwilds.client.particle.ChumParticle;

@EventBusSubscriber(modid = UntamedWilds.MOD_ID, value = Dist.CLIENT)
public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, UntamedWilds.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CHUM_DISPERSE = PARTICLES.register("chum", () -> new SimpleParticleType(false));

    @OnlyIn(Dist.CLIENT)
    @EventBusSubscriber(modid = UntamedWilds.MOD_ID, value = Dist.CLIENT)
    public static class RegisterParticleFactories {

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void registerParticleTypes(RegisterParticleProvidersEvent event) {
            ParticleEngine particleManager = Minecraft.getInstance().particleEngine;
            particleManager.register(CHUM_DISPERSE.get(), ChumParticle.Provider::new);
        }
    }
}