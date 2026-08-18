package untamedwilds.init;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import untamedwilds.UntamedWilds;
import untamedwilds.block.AlgaeBlock;
import untamedwilds.block.AnemoneBlock;
import untamedwilds.block.CageBlock;
import untamedwilds.block.CarpetBlock;
import untamedwilds.block.CritterBurrowBlock;
import untamedwilds.block.CustomGrassBlock;
import untamedwilds.block.EpyphitePlantBlock;
import untamedwilds.block.FloatingPlantBlock;
import untamedwilds.block.LardBlock;
import untamedwilds.block.NestReptileBlock;
import untamedwilds.block.ReedBlock;
import untamedwilds.block.StrangeEggBlock;
import untamedwilds.block.TallGrassBlock;
import untamedwilds.block.TallPlantBlock;
import untamedwilds.block.TitanArumBlock;
import untamedwilds.block.UndergrowthBlock;
import untamedwilds.block.UndergrowthPoisonousBlock;
import untamedwilds.block.blockentity.CageBlockEntity;
import untamedwilds.util.ModCreativeModeTab;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class ModBlock {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(UntamedWilds.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, UntamedWilds.MOD_ID);

    private final static List<Pair<DeferredBlock<Block>, String>> RENDER_TYPE_DATA = Lists.newArrayList();

    // Carpets
    public static DeferredBlock<Block> CARPET_STRAW  = createBlock("carpet_straw", properties -> new CarpetBlock(properties.mapColor(MapColor.SAND).strength(0.1F).sound(SoundType.CROP)), CreativeModeTabs.FUNCTIONAL_BLOCKS);
    public static DeferredBlock<Block> CARPET_BEAR_ASHEN  = createBlock("carpet_bear_ashen", properties -> new CarpetBlock(properties.mapColor(MapColor.COLOR_GRAY).strength(0.1F).sound(SoundType.WOOL)), CreativeModeTabs.FUNCTIONAL_BLOCKS);
        public static DeferredBlock<Block> CARPET_BEAR_BLACK  = createBlock("carpet_bear_black", properties -> new CarpetBlock(properties.mapColor(MapColor.COLOR_BLACK).strength(0.1F).sound(SoundType.WOOL)), CreativeModeTabs.NATURAL_BLOCKS);
        public static DeferredBlock<Block> CARPET_BEAR_BROWN  = createBlock("carpet_bear_brown", properties -> new CarpetBlock(properties.mapColor(MapColor.COLOR_BROWN).strength(0.1F).sound(SoundType.WOOL)), CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> CARPET_BEAR_WHITE  = createBlock("carpet_bear_white", properties -> new CarpetBlock(properties.mapColor(MapColor.SNOW).strength(0.1F).sound(SoundType.WOOL)), CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> CARPET_BIGCAT_JAGUAR  = createBlock("carpet_bigcat_jaguar", properties -> new CarpetBlock(properties.mapColor(MapColor.GOLD).strength(0.1F).sound(SoundType.WOOL)), CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> CARPET_BIGCAT_LEOPARD  = createBlock("carpet_bigcat_leopard", properties -> new CarpetBlock(properties.mapColor(MapColor.GOLD).strength(0.1F).sound(SoundType.WOOL)), CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> CARPET_BIGCAT_LION  = createBlock("carpet_bigcat_lion", properties -> new CarpetBlock(properties.mapColor(MapColor.COLOR_BROWN).strength(0.1F).sound(SoundType.WOOL)), CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> CARPET_BIGCAT_PANTHER  = createBlock("carpet_bigcat_panther", properties -> new CarpetBlock(properties.mapColor(MapColor.SNOW).strength(0.1F).sound(SoundType.WOOL)), CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> CARPET_BIGCAT_PUMA  = createBlock("carpet_bigcat_puma", properties -> new CarpetBlock(properties.mapColor(MapColor.COLOR_GRAY).strength(0.1F).sound(SoundType.WOOL)), CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> CARPET_BIGCAT_SNOW  = createBlock("carpet_bigcat_snow_leopard", properties -> new CarpetBlock(properties.mapColor(MapColor.COLOR_BLACK).strength(0.1F).sound(SoundType.WOOL)), CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> CARPET_BIGCAT_TIGER  = createBlock("carpet_bigcat_tiger", properties -> new CarpetBlock(properties.mapColor(MapColor.COLOR_BROWN).strength(0.1F).sound(SoundType.WOOL)), CreativeModeTabs.NATURAL_BLOCKS);

    // Storage
    public static DeferredBlock<Block> LARD_BLOCK  = createBlock("block_lard", properties -> new LardBlock(properties.mapColor(MapColor.COLOR_YELLOW).strength(0.1F).sound(SoundType.SLIME_BLOCK)), CreativeModeTabs.BUILDING_BLOCKS);
    public static DeferredBlock<Block> PEARL_BLOCK  = createBlock("block_pearl", properties -> new Block(properties.mapColor(MapColor.COLOR_CYAN).strength(5.0F).sound(SoundType.STONE)), CreativeModeTabs.BUILDING_BLOCKS);

    // Machines
    public static DeferredBlock<Block> TRAP_CAGE  = createBlock("trap_cage", properties -> new CageBlock(properties.mapColor(MapColor.WOOD).strength(3.0F).sound(SoundType.WOOD)), CreativeModeTabs.TOOLS_AND_UTILITIES);

    // Fauna
    public static DeferredBlock<Block> ANEMONE_ROSE_BULB  = createBlock("anemone_rose_bulb", properties -> new AnemoneBlock(properties.mapColor(MapColor.COLOR_RED).strength(0.1F).sound(SoundType.SLIME_BLOCK)), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> ANEMONE_SAND  = createBlock("anemone_sand", properties -> new AnemoneBlock(properties.mapColor(MapColor.COLOR_PINK).strength(0.1F).sound(SoundType.SLIME_BLOCK)), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> ANEMONE_SEBAE  = createBlock("anemone_sebae", properties -> new AnemoneBlock(properties.mapColor(MapColor.TERRACOTTA_WHITE).strength(0.1F).sound(SoundType.SLIME_BLOCK)), "cutout", CreativeModeTabs.NATURAL_BLOCKS);

    // Flora - Reeds
    public static DeferredBlock<Block> COMMON_REED = createBlock("flora_common_reed", properties -> new ReedBlock(properties.mapColor(MapColor.COLOR_GREEN).strength(0.1F).sound(SoundType.VINE).noCollision()), "cutout", CreativeModeTabs.NATURAL_BLOCKS, 100);
    // Flora - Bushes
    public static DeferredBlock<Block> BUSH_TEMPERATE = createBlock("flora_bush_temperate", properties -> new UndergrowthBlock(properties.mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.AZALEA_LEAVES).noCollision()), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> BUSH_CREOSOTE = createBlock("flora_bush_creosote", properties -> new UndergrowthBlock(properties.mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.AZALEA_LEAVES).noCollision()), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> ELEPHANT_EAR = createBlock("flora_elephant_ear", properties -> new UndergrowthBlock(properties.mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WET_GRASS).noCollision(), BlockBehaviour.OffsetType.XYZ), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> HEMLOCK = createBlock("flora_hemlock", properties -> new UndergrowthPoisonousBlock(properties.mapColor(MapColor.COLOR_GREEN).strength(0.1F).sound(SoundType.GRASS).noCollision(), BlockBehaviour.OffsetType.XYZ), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> YARROW = createBlock("flora_yarrow", properties -> new CustomGrassBlock(MobEffects.REGENERATION, 4, properties.mapColor(MapColor.COLOR_GREEN).strength(0.0F).sound(SoundType.GRASS).noCollision()), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> JUNEGRASS = createBlock("flora_junegrass", properties -> new CustomGrassBlock(MobEffects.UNLUCK, 4, properties.mapColor(MapColor.COLOR_GREEN).strength(0.0F).sound(SoundType.GRASS).noCollision()), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> CANOLA = createBlock("flora_canola", properties -> new CustomGrassBlock(MobEffects.STRENGTH, 4, properties.mapColor(MapColor.COLOR_GREEN).strength(0.0F).sound(SoundType.GRASS).noCollision()), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    // Flora - Multistage
    public static DeferredBlock<Block> ZIMBABWE_ALOE = createItemlessBlock("flora_zimbabwe_aloe", properties -> new TallPlantBlock(properties.mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.WOOD).dynamicShape()), "cutout");
    public static DeferredBlock<Block> PAMPAS_GRASS = createBlock("flora_pampas_grass", properties -> new TallGrassBlock(properties.mapColor(MapColor.COLOR_GREEN).strength(1.0F).sound(SoundType.GRASS).dynamicShape()), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    // Flora - Floating
    public static DeferredBlock<Block> WATER_HYACINTH = createItemlessBlock("flora_water_hyacinth", properties -> new FloatingPlantBlock(properties.mapColor(MapColor.COLOR_GREEN).strength(0.0F).sound(SoundType.LILY_PAD).noCollision()), "cutout");
    // Flora - Algae
    public static DeferredBlock<Block> AMAZON_SWORD = createBlock("flora_amazon_sword", properties -> new AlgaeBlock(properties.mapColor(MapColor.COLOR_GREEN).strength(0).sound(SoundType.WET_GRASS).noCollision()), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> EELGRASS = createBlock("flora_eelgrass", properties -> new AlgaeBlock(properties.mapColor(MapColor.COLOR_GREEN).strength(0).sound(SoundType.WET_GRASS).noCollision()), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    // Flora - Flowers
    public static DeferredBlock<Block> ORCHID_MAGENTA = createBlock("flora_orchid_magenta", properties -> new EpyphitePlantBlock(properties.mapColor(MapColor.COLOR_MAGENTA).strength(0.0F).sound(SoundType.VINE)), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> ORCHID_PURPLE = createBlock("flora_orchid_purple", properties -> new EpyphitePlantBlock(properties.mapColor(MapColor.COLOR_PURPLE).strength(0.0F).sound(SoundType.VINE)), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> ORCHID_PINK = createBlock("flora_orchid_pink", properties -> new EpyphitePlantBlock(properties.mapColor(MapColor.COLOR_PINK).strength(0.0F).sound(SoundType.VINE)), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> ORCHID_RED = createBlock("flora_orchid_red", properties -> new EpyphitePlantBlock(properties.mapColor(MapColor.COLOR_RED).strength(0.0F).sound(SoundType.VINE)), "cutout", CreativeModeTabs.NATURAL_BLOCKS);
    public static DeferredBlock<Block> TITAN_ARUM = createItemlessBlock("flora_titan_arum", properties -> new TitanArumBlock(properties.mapColor(MapColor.COLOR_GREEN).strength(2.0F).sound(SoundType.WET_GRASS).noCollision().dynamicShape()), "cutout");

    // Nests
    public static DeferredBlock<Block> NEST_REPTILE = createBlock("nest_reptile", properties -> new NestReptileBlock(properties.mapColor(MapColor.DIRT).strength(1.0F).sound(SoundType.GRAVEL)),  "translucent", ModCreativeModeTab.untamedwilds_items);

    // Eggs
    public static DeferredBlock<Block> EGG_SPITTER = createBlock("egg_spitter", properties -> new StrangeEggBlock(properties.mapColor(MapColor.DIRT).strength(1.0F).sound(SoundType.SLIME_BLOCK)), "cutout", ModCreativeModeTab.untamedwilds_items);

    // Technical Blocks
    public static DeferredBlock<Block> BURROW = createBlock("block_burrow", properties -> new CritterBurrowBlock(properties.mapColor(MapColor.DIRT).strength(1.0F).sound(SoundType.GRAVEL).noCollision()),  "translucent", CreativeModeTabs.NATURAL_BLOCKS);

    // Block Entities
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CageBlockEntity>> TILE_ENTITY_CAGE = TILE_ENTITIES.register("trap_cage", () -> new BlockEntityType<>(CageBlockEntity::new, ModBlock.TRAP_CAGE.get()));

    public static <B extends Block> DeferredBlock<Block> createBlock(String name, Function<BlockBehaviour.Properties, ? extends B> factory, Object group) {
        return createBlock(name, factory, null, group, 0);
    }

    public static <B extends Block> DeferredBlock<Block> createBlock(String name, Function<BlockBehaviour.Properties, ? extends B> factory, @Nullable String renderType, Object group) {
        return createBlock(name, factory, renderType, group, 0);
    }

    public static <B extends Block> DeferredBlock<Block> createBlock(String name, Function<BlockBehaviour.Properties, ? extends B> factory, @Nullable String renderType, Object group, int burnTime) {
        DeferredBlock<Block> block = ModBlock.BLOCKS.registerBlock(name, factory);
        if (renderType != null) {
            RENDER_TYPE_DATA.add(new Pair<>(block, renderType));
        }
        ModItems.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties) {
            public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
                return burnTime;
            }
        });
        return block;
    }

    public static <B extends Block> DeferredBlock<Block> createItemlessBlock(String name, Function<BlockBehaviour.Properties, ? extends B> factory) {
        return createItemlessBlock(name, factory, null);
    }

    public static <B extends Block> DeferredBlock<Block> createItemlessBlock(String name, Function<BlockBehaviour.Properties, ? extends B> factory, @Nullable String renderType) {
        DeferredBlock<Block> block = ModBlock.BLOCKS.registerBlock(name, factory);
        if (renderType != null) {
            RENDER_TYPE_DATA.add(new Pair<>(block, renderType));
        }
        return block;
    }

    public static void registerRendering() {
        for (Pair<DeferredBlock<Block>, String> i : RENDER_TYPE_DATA) {
            Objects.requireNonNull(i.getFirst().get());
        }
    }

    public static void registerBlockColors(){
        BlockColors colors = Minecraft.getInstance().getBlockColors();
        final BlockTintSource grassColor = new BlockTintSource() {
            @Override
            public int color(net.minecraft.world.level.block.state.BlockState state) {
                return GrassColor.get(0.5D, 1.0D);
            }

            @Override
            public int colorInWorld(net.minecraft.world.level.block.state.BlockState state, net.minecraft.client.renderer.block.BlockAndTintGetter worldIn, net.minecraft.core.BlockPos pos) {
                return BiomeColors.getAverageGrassColor(worldIn, pos);
            }
        };

        colors.register(List.of(grassColor), ModBlock.YARROW.get());
        colors.register(List.of(grassColor), ModBlock.JUNEGRASS.get());
        colors.register(List.of(grassColor), ModBlock.CANOLA.get());
    }
}
