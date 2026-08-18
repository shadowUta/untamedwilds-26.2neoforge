package untamedwilds.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.config.ConfigMobControl;

import java.util.List;

public record UntamedWildsBiomeModifier(TagKey<Biome> dimension, List<HolderSet<Biome>> biomes,
                                        List<HolderSet<Biome>> blacklist, GenerationStep.Decoration decoration,
                                        Holder<PlacedFeature> feature, String configOption) implements BiomeModifier {

    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, UntamedWilds.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<UntamedWildsBiomeModifier>> BIOME_MODIFIER_SERIALIZER = BIOME_MODIFIER_SERIALIZERS.register("biome_modifier_serializer",
        () -> RecordCodecBuilder.mapCodec(builder -> builder.group(
            TagKey.codec(Registries.BIOME).fieldOf("dimension").forGetter(UntamedWildsBiomeModifier::dimension),
            Biome.LIST_CODEC.listOf().fieldOf("biomes").forGetter(UntamedWildsBiomeModifier::biomes),
            Biome.LIST_CODEC.listOf().fieldOf("blacklist").forGetter(UntamedWildsBiomeModifier::blacklist),
            GenerationStep.Decoration.CODEC.fieldOf("decoration").forGetter(UntamedWildsBiomeModifier::decoration),
            PlacedFeature.CODEC.fieldOf("feature").forGetter(UntamedWildsBiomeModifier::feature),
            com.mojang.serialization.Codec.STRING.fieldOf("configOption").forGetter(UntamedWildsBiomeModifier::configOption)
        ).apply(builder, UntamedWildsBiomeModifier::new)));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        ModConfigSpec.BooleanValue option = ConfigFeatureControl.options.get(configOption);
        if (configOption.isEmpty() || (option != null && option.get() || option == null &&
            // TODO just a quick and dirty check for underground features
            ConfigFeatureControl.probUnderground.get() != 0 && ConfigMobControl.masterSpawner.get())) {
            if (phase != Phase.ADD)
                return;
            if (!biome.is(dimension))
                return;
            if (blacklist.stream().anyMatch(set -> set.contains(biome)))
                return;
            if (biomes.isEmpty() || biomes.stream().anyMatch(set -> set.contains(biome)))
                builder.getGenerationSettings().addFeature(decoration, feature);
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return BIOME_MODIFIER_SERIALIZER.get();
    }
}