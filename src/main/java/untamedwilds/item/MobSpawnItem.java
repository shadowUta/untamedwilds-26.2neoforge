package untamedwilds.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.INeedsPostUpdate;
import untamedwilds.util.EntityUtils;
import untamedwilds.util.ModCreativeModeTab;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.Consumer;

public class MobSpawnItem extends Item {
    private final Supplier<? extends EntityType<?>> entity;

    public MobSpawnItem(Supplier<? extends EntityType<?>> typeIn, Properties properties) {
        super(properties);
        this.entity = typeIn;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        List<Component> lines = new java.util.ArrayList<>();
        EntityUtils.buildTooltipData(stack, lines, this.entity.get(), EntityUtils.getVariantName(this.entity.get(), this.getSpecies(stack)));
        lines.forEach(tooltip);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("entity.untamedwilds." + this.entity.get().builtInRegistryHolder().key().identifier().getPath() + "_" + EntityUtils.getVariantName(this.entity.get(), this.getSpecies(stack)));
        //return new TranslatableComponent("entity.untamedwilds." + this.entity.getRegistryName().getPath() + "_" + ComplexMob.getEntityData(this.entity).getSpeciesData().get(this.getSpecies(stack)).getName()).getString();
    }

    @Override
    public InteractionResult useOn(UseOnContext useContext) {

        Level worldIn = useContext.getLevel();
        if (!(worldIn instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemStack = useContext.getItemInHand();
            BlockPos pos = useContext.getClickedPos();
            Direction facing = useContext.getClickedFace();
            BlockState blockState = worldIn.getBlockState(pos);
            BlockPos spawnPos = blockState.getCollisionShape(worldIn, pos).isEmpty() ? pos : pos.relative(facing);

            EntityType<?> entity = EntityUtils.getEntityTypeFromTag(this.getComponentData(itemStack), this.entity.get());
            boolean doVerticalOffset = !Objects.equals(pos, spawnPos) && facing == Direction.UP;
            EntityUtils.createMobFromItem((ServerLevel) worldIn, itemStack, entity, this.getSpecies(itemStack), spawnPos, useContext.getPlayer(), doVerticalOffset);

            if (useContext.getPlayer() != null) {
                if (!useContext.getPlayer().isCreative()) {
                    itemStack.shrink(1);
                }
            }
        }
        return InteractionResult.CONSUME;
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