package untamedwilds.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigModCompat {

    public static ModConfigSpec.BooleanValue sereneSeasonsCompat;

    ConfigModCompat(final ModConfigSpec.Builder builder) {
        builder.comment("Inter-mod compatibility");

        sereneSeasonsCompat = builder.comment("Controls whether to check for Serene Seasons for compatibility (Mobs will only breed during specific seasons).").define("modcompat.serene_seasons", true);

    }
}
