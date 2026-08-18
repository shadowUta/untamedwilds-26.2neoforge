package untamedwilds.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityUtils;
import untamedwilds.util.ModCreativeModeTab;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.Consumer;

public class MobBucketedItem extends BucketItem {
    private final Supplier<? extends EntityType<?>> entity;

    public MobBucketedItem(Supplier<? extends EntityType<?>> typeIn, Fluid fluid, Item.Properties builder) {
        this(typeIn, BuiltInRegistries.FLUID.wrapAsHolder(fluid), builder);
    }

    public MobBucketedItem(Supplier<? extends EntityType<?>> typeIn, Holder<Fluid> fluid, Item.Properties builder) {
        super(fluid.value(), builder);
        this.entity = typeIn;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        if (ComplexMob.ENTITY_DATA_HASH.containsKey(this.entity.get())) {
            List<Component> lines = new java.util.ArrayList<>();
            EntityUtils.buildTooltipData(stack, lines, this.entity.get(), EntityUtils.getVariantName(this.entity.get(), this.getSpecies(stack)));
            lines.forEach(tooltip);
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        if (ComplexMob.ENTITY_DATA_HASH.containsKey(this.entity.get())) {
            return Component.translatable("item.untamedwilds.bucket_" + this.entity.get().builtInRegistryHolder().key().identifier().getPath() + "_" + EntityUtils.getVariantName(this.entity.get(), this.getSpecies(stack)));
        }
        return super.getName(stack);
    }

    @Override
    public void checkExtraContent(@Nullable LivingEntity playerIn, Level worldIn, ItemStack itemStackIn, BlockPos posIn) {
        if (worldIn instanceof ServerLevel) {
            this.spawn(worldIn, itemStackIn, posIn);
            worldIn.gameEvent(playerIn, GameEvent.ENTITY_PLACE, posIn);
        }
    }

    public void spawn(Level worldIn, ItemStack itemStack, BlockPos pos) {
        if (worldIn instanceof ServerLevel) {
            EntityType<?> entity = EntityUtils.getEntityTypeFromTag(this.getComponentData(itemStack), this.entity.get());
            EntityUtils.createMobFromItem((ServerLevel) worldIn, itemStack, entity, this.getSpecies(itemStack), pos, null, false);
        }
    }

    @Override
    protected void playEmptySound(@Nullable LivingEntity player, LevelAccessor worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, SoundEvents.BUCKET_EMPTY_FISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    private int getSpecies(ItemStack itemIn) {
        CompoundTag data = this.getComponentData(itemIn);
        if (data != null && (data.contains("variant") || data.contains("CustomModelData") || data.contains("custom_model_data"))) {
            return data.getIntOr("variant", data.getIntOr("CustomModelData", data.getIntOr("custom_model_data", 0)));
        }
        CustomModelData modelData = itemIn.get(DataComponents.CUSTOM_MODEL_DATA);
        if (modelData != null && !modelData.floats().isEmpty()) {
            return modelData.floats().getFirst().intValue();
        }
        UntamedWilds.LOGGER.error("No variant found in this itemstack NBT data");
        return 0;
    }

    private CompoundTag getComponentData(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            data = stack.get(DataComponents.BUCKET_ENTITY_DATA);
        }
        return data == null ? null : data.copyTag();
    }

    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (group == ModCreativeModeTab.untamedwilds_items.value()) {
            for(int i = 0; i < EntityUtils.getNumberOfSpecies(this.entity.get()); i++) {
                CompoundTag baseTag = new CompoundTag();
                ItemStack item = new ItemStack(this);
                baseTag.putInt("variant", i);
                baseTag.putInt("custom_model_data", i);
                item.set(DataComponents.CUSTOM_DATA, CustomData.of(baseTag));
                item.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of((float)i), List.of(), List.of(), List.of()));
                items.add(item);
            }
        }
    }
}