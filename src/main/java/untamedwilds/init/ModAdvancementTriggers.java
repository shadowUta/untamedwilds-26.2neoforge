package untamedwilds.init;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import untamedwilds.UntamedWilds;

public class ModAdvancementTriggers {
    public static UntamedTriggers<?> NO_PATCHOULI_LOADED = new UntamedTriggers<>(Identifier.fromNamespaceAndPath(UntamedWilds.MOD_ID, "guidebook_alt"));
    public static UntamedTriggers<?> BAIT_BASIC = new UntamedTriggers<>(Identifier.fromNamespaceAndPath(UntamedWilds.MOD_ID, "used_bait"));
    public static UntamedTriggers<?> MASTER_BAIT = new UntamedTriggers<>(Identifier.fromNamespaceAndPath(UntamedWilds.MOD_ID, "master_bait"));

    // TODO: Abstract the "Discovered" Trigger to activate for any mob
    public static UntamedTriggers<?> DISCOVERED_SPITTER = new UntamedTriggers<>(Identifier.fromNamespaceAndPath(UntamedWilds.MOD_ID, "discovered_spitter"));

    public static void register() {
    }

    public static class UntamedTriggers<T> {
        private final Identifier id;

        public UntamedTriggers(Identifier resourceLocation) {
            this.id = resourceLocation;
        }

        public void trigger(ServerPlayer entityIn) {
        }
    }
}