package untamedwilds.item;

import net.minecraft.ChatFormatting;
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
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.INeedsPostUpdate;
import untamedwilds.util.EntityUtils;
import untamedwilds.util.ModCreativeModeTab;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.Consumer;

public class MobEggItem extends Item {
    private final Supplier<? extends EntityType<?>> entity;

    public MobEggItem(Supplier<? extends EntityType<?>> typeIn, Properties properties) {
        super(properties);
        this.entity = typeIn;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("mobspawn.tooltip.unknown").withStyle(ChatFormatting.GRAY));
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
            Entity spawn = entity.create((ServerLevel) worldIn, EntityType.createDefaultStackConfig(worldIn, itemStack, useContext.getPlayer()), spawnPos, EntitySpawnReason.BUCKET, true, !Objects.equals(pos, spawnPos) && facing == Direction.UP);
            if (spawn instanceof ComplexMob) {
                ComplexMob entitySpawn = (ComplexMob) spawn;
                entitySpawn.setVariant(this.getSpecies(itemStack, entitySpawn));
                entitySpawn.chooseSkinForSpecies(entitySpawn, true);
                entitySpawn.setRandomMobSize();
                entitySpawn.setGender(entitySpawn.getRandom().nextInt(2));
                entitySpawn.setAge(entitySpawn.getAdulthoodTime() * -1);

                if (spawn instanceof INeedsPostUpdate) {
                    ((INeedsPostUpdate) spawn).updateAttributes();
                }
            }
            if (spawn != null) {
                ((ServerLevel) worldIn).addFreshEntityWithPassengers(spawn);
            }
            itemStack.shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.untamedwilds.egg_" + this.entity.get().builtInRegistryHolder().key().identifier().getPath());
    }

    private int getSpecies(ItemStack itemIn, ComplexMob entityIn) {
        CompoundTag data = this.getComponentData(itemIn);
        if (data != null && data.contains("variant")) {
            return data.getIntOr("variant", entityIn.getVariant());
        }
        return entityIn.getVariant();
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

    // TODO: Have dropped eggs eventually hatch into baby mobs
    /*@Override
    public void onDestroyed(ItemEntity entityItem) {

        ItemStack itemstack = entityItem.getItem();
        Level worldIn = entityItem.world;

        if (entityItem.world.isClientSide || entityItem.tickCount < this.getHatchingTime(itemstack)) {
            return super.onEntityItemUpdate(entityItem);
        }

        Entity entity = EntityList.createEntityByIDFromName(this.entity, worldIn);;

        if (entity instanceof ComplexMob) {
            ComplexMob entitySpawn = (ComplexMob) entity;
            entitySpawn.setSpecies(this.getSpecies(itemstack));
            entitySpawn.setPosition(entityItem.getPosition().getX() + 0.5, entityItem.getPosition().getY() + 1, entityItem.getPosition().getZ() + 0.5);
            entitySpawn.setRandomMobSize();
            entitySpawn.setGender(worldIn.random.nextInt(2));

            itemstack.shrink(1);
            if (itemstack.isEmpty()) {
                entityItem.setDead();
            }

            worldIn.spawnEntity(entitySpawn);
            entitySpawn.playLivingSound();
        }

        return super.onEntityItemUpdate(entityItem);
    }*/
}