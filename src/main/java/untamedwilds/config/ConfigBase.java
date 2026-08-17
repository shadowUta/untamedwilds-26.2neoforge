package untamedwilds.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import untamedwilds.UntamedWilds;

@EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class ConfigBase {
    private static final ModConfigSpec.Builder common_builder = new ModConfigSpec.Builder();
    public static final ModConfigSpec common_config;

    public static final ConfigFeatureControl FEATURES;
    public static final ConfigGamerules GAMERULES;
    public static final ConfigMobControl MOBS;
    public static final ConfigModCompat COMPAT;

    static {
        FEATURES = new ConfigFeatureControl(common_builder);
        GAMERULES = new ConfigGamerules(common_builder);
        MOBS = new ConfigMobControl(common_builder);
        COMPAT = new ConfigModCompat(common_builder);

        common_config = common_builder.build();
    }

    public static void loadConfig(ModConfigSpec config, String path) {
        // NeoForge 26.2 loads ModConfigSpec through the ModConfig event system.
    }
}
