package untamedwilds.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class OwnershipDeedItem extends Item {
    
    public OwnershipDeedItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag nbt = getDeedData(stack);
        if (nbt != null) {
            tooltip.accept(Component.translatable("item.untamedwilds.ownership_deed_desc_4", nbt.getStringOr("entityname", "")).withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.translatable("item.untamedwilds.ownership_deed_desc_5").withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.translatable("item.untamedwilds.ownership_deed_desc_6", nbt.getStringOr("ownername", "")).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }
        else {
            tooltip.accept(Component.translatable("item.untamedwilds.ownership_deed_desc_1").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return getDeedData(stack) != null;
    }

    @Override
    @Nonnull
    public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        CompoundTag nbt = getDeedData(itemstack);

        if (nbt != null) {
            String entityId = nbt.getStringOr("entityid", "");
            if (!entityId.isEmpty()) {
                List<LivingEntity> list = worldIn.getEntitiesOfClass(LivingEntity.class, playerIn.getBoundingBox().inflate(8.0D));
                for(LivingEntity entity : list) {
                    if (entity.getUUID().equals(UUID.fromString(entityId))) {
                        entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80, 0, false, false));
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof TamableAnimal tamable) || !tamable.isTame()) {
            return InteractionResult.PASS;
        }

        CompoundTag deed = getDeedData(stack);
        if (deed == null) {
            if (tamable.getOwnerReference() == null || !Objects.equals(tamable.getOwnerReference().getUUID(), playerIn.getUUID())) {
                return InteractionResult.FAIL;
            }

            if (!playerIn.level().isClientSide()) {
                deed = new CompoundTag();
                deed.putString("ownername", playerIn.getName().getString());
                deed.putString("entityname", tamable.getName().getString());
                deed.putString("ownerid", playerIn.getUUID().toString());
                deed.putString("entityid", tamable.getUUID().toString());
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(deed));
            }
            return InteractionResult.SUCCESS;
        }

        if (tamable.getOwnerReference() == null
            || !Objects.equals(tamable.getOwnerReference().getUUID(), parseUuid(deed.getStringOr("ownerid", "")))
                || !tamable.getUUID().equals(parseUuid(deed.getStringOr("entityid", "")))) {
            return InteractionResult.FAIL;
        }

        if (!playerIn.level().isClientSide()) {
            tamable.setOwner(playerIn);
            if (!playerIn.isCreative()) {
                stack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private static CompoundTag getDeedData(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null || data.isEmpty() ? null : data.copyTag();
    }

    private static UUID parseUuid(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }


    /*@Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, Hand hand) {
        UntamedWilds.LOGGER.log(Level.INFO, "Trying to get entity");

        ItemStack itemstack = playerIn.getItemInHand(hand);
        if (target instanceof TameableEntity) {
            TameableEntity entity_target = (TameableEntity) target;

            if (entity_target.isTame()) {
                if (entity_target.getOwnerId().equals(playerIn.getUUID()) && !itemstack.hasTag()) {
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("ownername", playerIn.getName().getString());
                    nbt.putString("entityname", entity_target.getName().getString());
                    nbt.putString("ownerid", playerIn.getUUID().toString());
                    nbt.putString("entityid", entity_target.getUUID().toString());
                    itemstack.setTag(nbt);
                    if (UntamedWilds.DEBUG) {
                        UntamedWilds.LOGGER.log(Level.INFO, "Pet owner signed a deed for a " + entity_target.getName().getString());
                    }
                    return InteractionResult.SUCCESS;
                }

                else if (itemstack.hasTag()) {
                    if (entity_target.getOwnerId().toString().equals(itemstack.getTag().getString("ownerid")) && entity_target.getUUID().toString().equals(itemstack.getTag().getString("entityid"))) {
                        entity_target.setOwnerId(playerIn.getUUID());
                        if (!playerIn.isCreative()) {
                            itemstack.shrink(1);
                        }
                        // playerIn.addStat(Stats.getObjectUseStats(this));
                        if (UntamedWilds.DEBUG) {
                            UntamedWilds.LOGGER.log(Level.INFO, "Pet ownership transferred to " + playerIn.getName().getString());
                        }
                        return InteractionResult.CONSUME;
                    }
                }
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.FAIL;
    }*/
}