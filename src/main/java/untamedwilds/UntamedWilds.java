package untamedwilds;

import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import untamedwilds.compat.CompatBridge;
import untamedwilds.config.ConfigBase;
import untamedwilds.init.*;
import untamedwilds.world.UntamedWildsBiomeModifier;

@Mod(value = UntamedWilds.MOD_ID)
public class UntamedWilds {

    // TODO: Abstract Herd logic to be functional with any LivingEntity (instead of being limited to IPackEntity ComplexMob)
    // TODO: Store the children's UUID in their mother's NBT, to allow checking for Children without constant AABB checking
    // TODO: Have carnivorous mobs gain hunger when attacking
    // TODO: Move Variants from Int to String to prevent shuffling?
    // TODO: Gillie suit bauble, to prevent animals from spotting you

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "untamedwilds";
    public static final boolean DEBUG = false;

    public UntamedWilds(IEventBus eventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, ConfigBase.common_config);
        eventBus.addListener(this::setupCommon);
        eventBus.addListener(this::setupClient);
        ModBlock.BLOCKS.register(eventBus);
        ModBlock.TILE_ENTITIES.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModEntity.ENTITIES.register(eventBus);
        ModItems.registerSpawnItems();
        UntamedWildsBiomeModifier.BIOME_MODIFIER_SERIALIZERS.register(eventBus);
        ModSounds.SOUNDS.register(eventBus);
        ModParticles.PARTICLES.register(eventBus);
        ModAdvancementTriggers.register();
        CompatBridge.RegisterCompat();
    }

    private void setupCommon(final FMLCommonSetupEvent event) {
    }

    private void setupClient(final FMLClientSetupEvent event) {
        ModBlock.registerRendering();
        ModBlock.registerBlockColors();
        //ModParticles.setupParticles();

        //ModBlock.registerBlockColors();
        //ModParticles.registerParticles(); Handled through events
    }
}