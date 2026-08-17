package untamedwilds.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ProjectileSpit;
import untamedwilds.entity.amphibian.EntityGiantSalamander;
import untamedwilds.entity.amphibian.EntityNewt;
import untamedwilds.entity.arthropod.EntityKingCrab;
import untamedwilds.entity.arthropod.EntityTarantula;
import untamedwilds.entity.fish.*;
import untamedwilds.entity.mammal.*;
import untamedwilds.entity.mollusk.EntityGiantClam;
import untamedwilds.entity.relict.EntitySpitter;
import untamedwilds.entity.reptile.*;
import untamedwilds.world.FaunaHandler;

import java.util.List;

@EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class ModEntity {
    public final static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, UntamedWilds.MOD_ID);
    //public static Map<RegistryObject<EntityType<? extends Mob>>, EntityRendererProvider<?>> map = Collections.emptyMap(); 
    
    // Arthropods
    public static DeferredHolder<EntityType<?>, EntityType<EntityTarantula>> TARANTULA = createEntity(EntityTarantula::new, "tarantula", 0.4f, 0.3f, 0xB5B095, 0x26292B);
    public static DeferredHolder<EntityType<?>, EntityType<EntityKingCrab>> KING_CRAB = createEntity(EntityKingCrab::new, "king_crab", 0.6f, 0.5f, 0x715236, 0xAD9050);

    // Reptiles
    public static DeferredHolder<EntityType<?>, EntityType<EntitySnake>> SNAKE = createEntity(EntitySnake::new, "snake", 0.95f, 0.3f, 0xD8A552, 0x5C3525);
    public static DeferredHolder<EntityType<?>, EntityType<EntitySoftshellTurtle>> SOFTSHELL_TURTLE = createEntity(EntitySoftshellTurtle::new, "softshell_turtle", 0.6f, 0.3f, 0x828444, 0x26292B);
    public static DeferredHolder<EntityType<?>, EntityType<EntityTortoise>> TORTOISE = createEntity(EntityTortoise::new, "tortoise", 0.6f, 0.6f, 0xAF9F74, 0x775232);
    public static DeferredHolder<EntityType<?>, EntityType<EntityAnaconda>> ANACONDA = createEntity(EntityAnaconda::new, "large_snake", 1.5f, 0.6f, 0x65704C, 0x42291A);
    public static DeferredHolder<EntityType<?>, EntityType<EntityMonitor>> MONITOR = createEntity(EntityMonitor::new, "monitor", 1.3f, 0.6f, 0x423C2C, 0x958C66);

    // Mollusks
    public static DeferredHolder<EntityType<?>, EntityType<EntityGiantClam>> GIANT_CLAM = createEntity(EntityGiantClam::new, "giant_clam", 1.0F, 1.0F, 0x346B70, 0xAD713C);
    //public static RegistryObject<EntityType<EntityGiantClam>> GIANT_CLAM = createEntity(EntityGiantClam::new, MobCategory.WATER_CREATURE, "giant_clam", 32, 10, true, 1.0F, 1.0F, 0x346B70, 0xAD713C, animalType.SESSILE, 1);

    // Mammals
    public static DeferredHolder<EntityType<?>, EntityType<EntityBear>> BEAR = createEntity(EntityBear::new, "bear", 1.3F, 1.3F, 0x20130B, 0x564C45);
    public static DeferredHolder<EntityType<?>, EntityType<EntityBigCat>> BIG_CAT = createEntity(EntityBigCat::new, "big_cat", 1.2F, 1.0F, 0xC59F45,0x383121);
    public static DeferredHolder<EntityType<?>, EntityType<EntityHippo>> HIPPO = createEntity(EntityHippo::new, "hippo", 1.8F, 1.8F, 0x463A31, 0x956761);
    public static DeferredHolder<EntityType<?>, EntityType<EntityAardvark>> AARDVARK = createEntity(EntityAardvark::new, "aardvark", 0.9F, 0.9F, 0x463A31, 0x956761);
    public static DeferredHolder<EntityType<?>, EntityType<EntityRhino>> RHINO = createEntity(EntityRhino::new, "rhino", 2.0F, 1.8F, 0x787676, 0x665956);
    public static DeferredHolder<EntityType<?>, EntityType<EntityHyena>> HYENA = createEntity(EntityHyena::new, "hyena", 0.9F, 1.1F, 0x6C6857, 0x978966);
    public static DeferredHolder<EntityType<?>, EntityType<EntityBoar>> BOAR = createEntity(EntityBoar::new, "boar", 1.2F, 1.2F, 0x503C2A, 0x605449);
    public static DeferredHolder<EntityType<?>, EntityType<EntityBison>> BISON = createEntity(EntityBison::new, "bison", 1.7F, 1.6F, 0x845B2B, 0x49342A);
    public static DeferredHolder<EntityType<?>, EntityType<EntityCamel>> CAMEL = createEntity(EntityCamel::new, "camel", 1.8F, 2F, 0xE0B989, 0x976B3D);
    public static DeferredHolder<EntityType<?>, EntityType<EntityManatee>> MANATEE = createEntity(EntityManatee::new, "manatee", 1.8F, 2F, 0x4A4040, 0x787676);
    public static DeferredHolder<EntityType<?>, EntityType<EntityBaleenWhale>> BALEEN_WHALE = createEntity(EntityBaleenWhale::new, "baleen_whale", 2.6F, 1.6F, 0x12141E, 0x5B6168);
    public static DeferredHolder<EntityType<?>, EntityType<EntityOpossum>> OPOSSUM = createEntity(EntityOpossum::new, "opossum", 0.9F, 0.9F, 0xABA29B, 0x383735);

    // Fish
    public static DeferredHolder<EntityType<?>, EntityType<EntitySunfish>> SUNFISH = createEntity(EntitySunfish::new, "sunfish", 1.6F, 1.6F, 0x2C545B, 0xB6D0D3);
    public static DeferredHolder<EntityType<?>, EntityType<EntityTrevally>> TREVALLY = createEntity(EntityTrevally::new, "trevally", 0.8F, 0.8F, 0xA5B4AF, 0xC89D17);
    public static DeferredHolder<EntityType<?>, EntityType<EntityArowana>> AROWANA = createEntity(EntityArowana::new, "arowana", 0.6F, 0.6F, 0x645C45, 0xB29F52);
    public static DeferredHolder<EntityType<?>, EntityType<EntityShark>> SHARK = createEntity(EntityShark::new, "shark", 1.8F, 1.3F, 0x6B5142, 0xB0B0A3);
    public static DeferredHolder<EntityType<?>, EntityType<EntityFootballFish>> FOOTBALL_FISH = createEntity(EntityFootballFish::new, "football_fish", 0.8F, 0.8F, 0x53556C, 0x2F3037);
    public static DeferredHolder<EntityType<?>, EntityType<EntityWhaleShark>> WHALE_SHARK = createEntity(EntityWhaleShark::new, "whale_shark", 2.6F, 1.6F, 0x222426, 0x7E7D84);
    public static DeferredHolder<EntityType<?>, EntityType<EntityTriggerfish>> TRIGGERFISH = createEntity(EntityTriggerfish::new, "triggerfish", 0.8F, 0.8F, 0x1F0A19, 0xFCBD00);
    public static DeferredHolder<EntityType<?>, EntityType<EntityCatfish>> CATFISH = createEntity(EntityCatfish::new, "catfish", 0.8F, 0.8F, 0x545963, 0x3A2C23);
    public static DeferredHolder<EntityType<?>, EntityType<EntitySpadefish>> SPADEFISH = createEntity(EntitySpadefish::new, "spadefish", 0.8F, 0.8F, 0x545963, 0x3A2C23);

    // Amphibians
    public static DeferredHolder<EntityType<?>, EntityType<EntityGiantSalamander>> GIANT_SALAMANDER = createEntity(EntityGiantSalamander::new, "giant_salamander", 1F, 0.6f, 0x3A2C23, 0x6B5142);
    public static DeferredHolder<EntityType<?>, EntityType<EntityNewt>> NEWT = createEntity(EntityNewt::new, "newt", 0.6F, 0.3f, 0x232323, 0xFF8D00);

    // Relicts
    public static DeferredHolder<EntityType<?>, EntityType<EntitySpitter>> SPITTER = createEntity(EntitySpitter::new, "spitter", 1.3F, 1.3f, 0x3A345E, 0xB364E0);

    // Projectiles
    public static DeferredHolder<EntityType<?>, EntityType<ProjectileSpit>> SPIT = createProjectile(ProjectileSpit::new, "spit", 64, 1, true,0.6F, 0.3f);

    private static <T extends Projectile> DeferredHolder<EntityType<?>, EntityType<T>> createProjectile(EntityType.EntityFactory<T> factory, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, float sizeX, float sizeY) {
        DeferredHolder<EntityType<?>, EntityType<T>> type = ENTITIES.register(name, () -> EntityType.Builder.of(factory, MobCategory.MISC)
                .sized(sizeX, sizeY)
                .clientTrackingRange(trackingRange)
                .setShouldReceiveVelocityUpdates(sendsVelocityUpdates)
                .build(name));

        return type;
    }

    private static <T extends Mob> DeferredHolder<EntityType<?>, EntityType<T>> createEntity(EntityType.EntityFactory<T> factory, String name, float sizeX, float sizeY, int baseColor, int overlayColor) {
        return createEntity(factory, MobCategory.CREATURE, name, 64, 1, true, sizeX, sizeY, baseColor, overlayColor);
    }

    private static <T extends Mob> DeferredHolder<EntityType<?>, EntityType<T>> createEntity(EntityType.EntityFactory<T> factory, MobCategory classification, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, float sizeX, float sizeY, int maincolor, int backcolor) {
        DeferredHolder<EntityType<?>, EntityType<T>> type = ENTITIES.register(name, () -> EntityType.Builder.of(factory, classification)
                .sized(sizeX, sizeY)
                .clientTrackingRange(trackingRange)
                .setShouldReceiveVelocityUpdates(sendsVelocityUpdates)
                .build(name));

        ModItems.ITEMS.register(name + "_spawn_egg", () -> new UntamedSpawnEggItem(type, maincolor, backcolor, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        return type;
    }

    @SubscribeEvent
    public static void bakeAttributes(EntityAttributeCreationEvent event) {
        event.put(TARANTULA.get(), EntityTarantula.registerAttributes().build());
        event.put(KING_CRAB.get(), EntityKingCrab.registerAttributes().build());

        event.put(SNAKE.get(), EntitySnake.registerAttributes().build());
        event.put(SOFTSHELL_TURTLE.get(), EntitySoftshellTurtle.registerAttributes().build());
        event.put(TORTOISE.get(), EntityTortoise.registerAttributes().build());
        event.put(ANACONDA.get(), EntityAnaconda.registerAttributes().build());
        event.put(MONITOR.get(), EntityMonitor.registerAttributes().build());

        event.put(GIANT_CLAM.get(), EntityGiantClam.registerAttributes().build());

        event.put(BEAR.get(), EntityBear.registerAttributes().build());
        event.put(BIG_CAT.get(), EntityBigCat.registerAttributes().build());
        event.put(HIPPO.get(), EntityHippo.registerAttributes().build());
        event.put(AARDVARK.get(), EntityAardvark.registerAttributes().build());
        event.put(RHINO.get(), EntityRhino.registerAttributes().build());
        event.put(HYENA.get(), EntityHyena.registerAttributes().build());
        event.put(BOAR.get(), EntityBoar.registerAttributes().build());
        event.put(BISON.get(), EntityBison.registerAttributes().build());
        event.put(CAMEL.get(), EntityCamel.registerAttributes().build());
        event.put(MANATEE.get(), EntityManatee.registerAttributes().build());
        event.put(BALEEN_WHALE.get(), EntityBaleenWhale.registerAttributes().build());
        event.put(OPOSSUM.get(), EntityOpossum.registerAttributes().build());

        event.put(SUNFISH.get(), EntitySunfish.registerAttributes().build());
        event.put(TREVALLY.get(), EntityTrevally.registerAttributes().build());
        event.put(AROWANA.get(), EntityArowana.registerAttributes().build());
        event.put(SHARK.get(), EntityShark.registerAttributes().build());
        event.put(FOOTBALL_FISH.get(), EntityFootballFish.registerAttributes().build());
        event.put(WHALE_SHARK.get(), EntityWhaleShark.registerAttributes().build());
        event.put(TRIGGERFISH.get(), EntityTriggerfish.registerAttributes().build());
        event.put(CATFISH.get(), EntityCatfish.registerAttributes().build());
        event.put(SPADEFISH.get(), EntitySpadefish.registerAttributes().build());

        event.put(GIANT_SALAMANDER.get(), EntityGiantSalamander.registerAttributes().build());
        event.put(NEWT.get(), EntityNewt.registerAttributes().build());

        event.put(SPITTER.get(), EntitySpitter.registerAttributes().build());
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        ENTITIES.getEntries().forEach(holder -> event.registerEntityRenderer(holder.get(), NoopRenderer::new));
    }

    public static void addWorldSpawn(EntityType<?> entityClass, int weightedProb, FaunaHandler.animalType type, int groupCount) {
        List<FaunaHandler.SpawnListEntry> spawns = FaunaHandler.getSpawnableList(type);
        boolean found = false;
        for (FaunaHandler.SpawnListEntry entry : spawns) {
            // Adjusting an existing spawn entry
            if (entry.entityType == entityClass) {
                entry.itemWeight = weightedProb;
                //entry.groupCount = groupCount;
                found = true;
                break;
            }
        }

        //if (!found)
            //spawns.add(new FaunaHandler.SpawnListEntry(entityClass, weightedProb, groupCount));
    }
}