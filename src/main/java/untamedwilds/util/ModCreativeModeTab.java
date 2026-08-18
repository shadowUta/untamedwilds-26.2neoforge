package untamedwilds.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import untamedwilds.UntamedWilds;
import untamedwilds.init.ModItems;

public final class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, UntamedWilds.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> untamedwilds_items = TABS.register("untamedwilds_items", () ->
        CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.untamedwilds_items"))
                    .icon(() -> new ItemStack(ModItems.LOGO.get()))
                    .displayItems((parameters, output) -> BuiltInRegistries.ITEM.forEach(item -> {
                        Identifier key = BuiltInRegistries.ITEM.getKey(item);
                        if (UntamedWilds.MOD_ID.equals(key.getNamespace())) {
                            output.accept(item);
                        }
                    }))
                    .build());
}
