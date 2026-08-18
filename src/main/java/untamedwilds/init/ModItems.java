package untamedwilds.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import untamedwilds.UntamedWilds;
import untamedwilds.item.*;
import untamedwilds.item.debug.*;

import java.util.function.Function;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(UntamedWilds.MOD_ID);
    // Wild Level Item instances

    // Debug Tools
    public static DeferredItem<Item> LOGO = createItem("logo", properties -> new Item(properties.stacksTo(1).rarity(Rarity.EPIC)));
    public static DeferredItem<Item> OWNERSHIP_DEED = createItem("ownership_deed", properties -> new OwnershipDeedItem(properties.stacksTo(1)));
    public static DeferredItem<Item> DEBUG_ERASER = createItem("debug_eraser", properties -> new EraserItem(properties.stacksTo(1).rarity(Rarity.EPIC)));
    public static DeferredItem<Item> DEBUG_ANALYZER = createItem("debug_analyzer", properties -> new AnalyzerItem(properties.stacksTo(1).rarity(Rarity.EPIC)));
    public static DeferredItem<Item> DEBUG_IPECAC = createItem("debug_ipecac", properties -> new IpecacItem(properties.stacksTo(1).rarity(Rarity.EPIC)));
    public static DeferredItem<Item> DEBUG_LOVE_POTION = createItem("debug_love_potion", properties -> new LovePotionItem(properties.stacksTo(1).rarity(Rarity.EPIC)));
    public static DeferredItem<Item> DEBUG_GROWTH_TONIC = createItem("debug_growth_tonic", properties -> new GrowthTonicItem(properties.stacksTo(1).rarity(Rarity.EPIC)));
    public static DeferredItem<Item> DEBUG_HIGHLIGHTER = createItem("debug_highlighter", properties -> new HighlighterItem(properties.stacksTo(1).rarity(Rarity.EPIC)));

    // Materials
    public static DeferredItem<Item> MATERIAL_FAT = createItem("material_fat", properties -> new LardItem(properties.food(food(1, 1F))));
    public static DeferredItem<Item> MATERIAL_BLUBBER = createItem("material_blubber", properties -> new LardItem(properties.food(food(1, 1F))) {
        public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
            return 1200;
        }
    });
    public static DeferredItem<Item> MATERIAL_PEARL = createItem("material_pearl", Item::new);
    public static DeferredItem<Item> RARE_GIANT_PEARL = createItem("material_giant_pearl", properties -> new Item(properties.rarity(Rarity.UNCOMMON)));
    public static DeferredItem<Item> MATERIAL_SNAKE_SKIN = createItem("material_snake_skin", Item::new);
    public static DeferredItem<Item> CHUM = createItem("chum", properties -> new ChumItem(properties.food(food(1, 0.1F), foodWithEffect(new MobEffectInstance(MobEffects.NAUSEA, 1200, 0), 1F))));

    // Food
    public static DeferredItem<Item> MEAT_BEAR_RAW = createItem("food_bear_raw", properties -> new Item(properties.food(food(3, 0.6F))));
    public static DeferredItem<Item> MEAT_BEAR_COOKED = createItem("food_bear_cooked", properties -> new Item(properties.food(food(7, 1F))));
    public static DeferredItem<Item> MEAT_TURTLE_RAW = createItem("food_turtle_raw", properties -> new Item(properties.food(food(2, 0.3F))));
    public static DeferredItem<Item> MEAT_TURTLE_COOKED = createItem("food_turtle_cooked", properties -> new Item(properties.food(food(6, 0.6F))));
    public static DeferredItem<Item> MEAT_HIPPO_RAW = createItem("food_pachyderm_raw", properties -> new Item(properties.food(food(3, 0.7F))));
    public static DeferredItem<Item> MEAT_HIPPO_COOKED = createItem("food_pachyderm_cooked", properties -> new Item(properties.food(food(7, 1.1F))));
    public static DeferredItem<Item> FOOD_TURTLE_SOUP = createItem("food_turtle_soup", properties -> new Item(properties.food(food(8, 0.6F)).usingConvertsTo(Items.BOWL).stacksTo(1)));
    public static DeferredItem<Item> FOOD_PEMMICAN = createItem("food_pemmican", properties -> new Item(properties.food(food(6, 1.0F))));
    public static DeferredItem<Item> VEGETABLE_AARDVARK_CUCUMBER = createItem("food_aardvark_cucumber", properties -> new Item(properties.food(food(3, 0.2F))));
    public static DeferredItem<Item> FOOD_HEMLOCK_STEW = createItem("food_hemlock_stew", properties -> new Item(properties.food(alwaysEdibleFood(6, 0.1F), foodWithEffect(new MobEffectInstance(MobEffects.POISON, 1200, 3), 1F)).usingConvertsTo(Items.BOWL).stacksTo(1)));

    // Hides
    public static DeferredItem<Item> HIDE_BEAR_ASHEN = createItem("hide_bear_ashen", Item::new);
    public static DeferredItem<Item> HIDE_BEAR_BLACK = createItem("hide_bear_black", Item::new);
    public static DeferredItem<Item> HIDE_BEAR_BROWN = createItem("hide_bear_brown", Item::new);
    public static DeferredItem<Item> HIDE_BEAR_WHITE = createItem("hide_bear_white", Item::new);
    public static DeferredItem<Item> HIDE_BIGCAT_JAGUAR = createItem("hide_bigcat_jaguar", Item::new);
    public static DeferredItem<Item> HIDE_BIGCAT_LEOPARD = createItem("hide_bigcat_leopard", Item::new);
    public static DeferredItem<Item> HIDE_BIGCAT_LION = createItem("hide_bigcat_lion", Item::new);
    public static DeferredItem<Item> HIDE_BIGCAT_PANTHER = createItem("hide_bigcat_panther", Item::new);
    public static DeferredItem<Item> HIDE_BIGCAT_PUMA = createItem("hide_bigcat_puma", Item::new);
    public static DeferredItem<Item> HIDE_BIGCAT_SNOW_LEOPARD = createItem("hide_bigcat_snow_leopard", Item::new);
    public static DeferredItem<Item> HIDE_BIGCAT_TIGER = createItem("hide_bigcat_tiger", Item::new);

    // Item Blocks
    public static DeferredItem<Item> SEED_TITAN_ARUM = createItem("flora_titan_arum_corm", properties -> new BlockItem(ModBlock.TITAN_ARUM.get(), properties));
    public static DeferredItem<Item> SEED_ZIMBABWE_ALOE = createItem("flora_zimbabwe_aloe_sapling", properties -> new BlockItem(ModBlock.ZIMBABWE_ALOE.get(), properties));
    public static DeferredItem<Item> WATER_HYACINTH_BLOCK = createItem("flora_water_hyacinth_item", properties -> new PlaceOnWaterBlockItem(ModBlock.WATER_HYACINTH.get(), properties));

    private static FoodProperties food(int nutrition, float saturationModifier) {
        return new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturationModifier).build();
    }

    private static FoodProperties alwaysEdibleFood(int nutrition, float saturationModifier) {
        return new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturationModifier).alwaysEdible().build();
    }

    private static Consumable foodWithEffect(MobEffectInstance effect, float probability) {
        return Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(effect, probability)).build();
    }

    public static <I extends Item> DeferredItem<I> createItem(String name, Function<Item.Properties, ? extends I> factory) {
        return ModItems.ITEMS.registerItem(name, factory);
    }

    public static void registerSpawnItems() {
        // These items have no associated objects, as they are not supposed to be accessed, and I do not want to register each variant
        // Tarantula Items
        ModItems.ITEMS.registerItem("egg_tarantula", properties -> new MobEggItem(ModEntity.TARANTULA, properties));
        ModItems.ITEMS.registerItem("bottle_tarantula", properties -> new MobBottledItem(ModEntity.TARANTULA, properties));

        // Small Snake Items
        ModItems.ITEMS.registerItem("egg_snake", properties -> new MobEggItem(ModEntity.SNAKE, properties));
        ModItems.ITEMS.registerItem("spawn_snake", properties -> new MobSpawnItem(ModEntity.SNAKE, properties));

        // Softshell Turtle Items
        ModItems.ITEMS.registerItem("egg_softshell_turtle", properties -> new MobEggItem(ModEntity.SOFTSHELL_TURTLE, properties));
        ModItems.ITEMS.registerItem("spawn_softshell_turtle", properties -> new MobSpawnItem(ModEntity.SOFTSHELL_TURTLE, properties));

        // Giant Clam Items
        ModItems.ITEMS.registerItem("egg_giant_clam", properties -> new MobEggItem(ModEntity.GIANT_CLAM, properties));
        ModItems.ITEMS.registerItem("spawn_giant_clam", properties -> new MobSpawnItem(ModEntity.GIANT_CLAM, properties));

        // Sunfish Items
        ModItems.ITEMS.registerItem("egg_sunfish", properties -> new MobEggItem(ModEntity.SUNFISH, properties));

        // Trevally Items
        ModItems.ITEMS.registerItem("egg_trevally", properties -> new MobEggItem(ModEntity.TREVALLY, properties));
        ModItems.ITEMS.registerItem("bucket_trevally", properties -> new MobBucketedItem(ModEntity.TREVALLY, Fluids.WATER, properties));

        // Arowana Items
        ModItems.ITEMS.registerItem("egg_arowana", properties -> new MobEggItem(ModEntity.AROWANA, properties));
        ModItems.ITEMS.registerItem("bucket_arowana", properties -> new MobBucketedItem(ModEntity.AROWANA, Fluids.WATER, properties));

        // Football Fish Items
        ModItems.ITEMS.registerItem("egg_football_fish", properties -> new MobEggItem(ModEntity.FOOTBALL_FISH, properties));
        ModItems.ITEMS.registerItem("bucket_football_fish", properties -> new MobBucketedItem(ModEntity.FOOTBALL_FISH, Fluids.WATER, properties));

        // Giant Salamander Items
        ModItems.ITEMS.registerItem("egg_giant_salamander", properties -> new MobEggItem(ModEntity.GIANT_SALAMANDER, properties));
        ModItems.ITEMS.registerItem("bucket_giant_salamander", properties -> new MobBucketedItem(ModEntity.GIANT_SALAMANDER, Fluids.WATER, properties));

        // Newt Items
        ModItems.ITEMS.registerItem("egg_newt", properties -> new MobEggItem(ModEntity.NEWT, properties));
        ModItems.ITEMS.registerItem("bucket_newt", properties -> new MobBucketedItem(ModEntity.NEWT, Fluids.WATER, properties));

        // Tortoise Items
        ModItems.ITEMS.registerItem("egg_tortoise", properties -> new MobEggItem(ModEntity.TORTOISE, properties));
        ModItems.ITEMS.registerItem("spawn_tortoise", properties -> new MobSpawnItem(ModEntity.TORTOISE, properties));

        // Large Snake Items
        ModItems.ITEMS.registerItem("egg_large_snake", properties -> new MobEggItem(ModEntity.ANACONDA, properties));

        // Triggerfish Items
        ModItems.ITEMS.registerItem("egg_triggerfish", properties -> new MobEggItem(ModEntity.TRIGGERFISH, properties));
        ModItems.ITEMS.registerItem("bucket_triggerfish", properties -> new MobBucketedItem(ModEntity.TRIGGERFISH, Fluids.WATER, properties));

        // Catfish Items
        ModItems.ITEMS.registerItem("egg_catfish", properties -> new MobEggItem(ModEntity.CATFISH, properties));
        ModItems.ITEMS.registerItem("bucket_catfish", properties -> new MobBucketedItem(ModEntity.CATFISH, Fluids.WATER, properties));

        // King Crab Items
        ModItems.ITEMS.registerItem("egg_king_crab", properties -> new MobEggItem(ModEntity.KING_CRAB, properties));
        ModItems.ITEMS.registerItem("bucket_king_crab", properties -> new MobBucketedItem(ModEntity.KING_CRAB, Fluids.WATER, properties));

        // Monitor Items
        ModItems.ITEMS.registerItem("egg_monitor", properties -> new MobEggItem(ModEntity.MONITOR, properties));

        // Spadefish Items
        ModItems.ITEMS.registerItem("egg_spadefish", properties -> new MobEggItem(ModEntity.SPADEFISH, properties));
        ModItems.ITEMS.registerItem("bucket_spadefish", properties -> new MobBucketedItem(ModEntity.SPADEFISH, Fluids.WATER, properties));

    }
}