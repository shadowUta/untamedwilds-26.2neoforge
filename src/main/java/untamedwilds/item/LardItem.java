package untamedwilds.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;

public class LardItem extends Item {
    public LardItem(Item.Properties builder) {
        super(builder.component(DataComponents.CONSUMABLE, Consumable.builder()
                .consumeSeconds(2.0F)
                .animation(ItemUseAnimation.EAT)
                .sound(SoundEvents.HONEY_DRINK)
                .build()));
    }
}