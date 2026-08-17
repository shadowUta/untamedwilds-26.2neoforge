package untamedwilds.entity.ai.unique;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import untamedwilds.entity.mammal.EntityBear;

import java.util.EnumSet;

public class BearRaidChestsGoal extends Goal {
    private Container targetInventory;
    private BlockPos targetPos;
    private final EntityBear taskOwner;
    private final int executionChance;
    private int searchCooldown;
    private boolean continueTask;
    private boolean containerOpen;

    public BearRaidChestsGoal(EntityBear entityIn, int chance) {
        this.taskOwner = entityIn;
        this.executionChance = chance;
        this.searchCooldown = 100;
        this.continueTask = true;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.taskOwner.isTame() || !this.taskOwner.onGround() || this.taskOwner.getHunger() > 60
                || this.taskOwner.getRandom().nextInt(this.executionChance) != 0 || this.taskOwner.getTarget() != null) {
            return false;
        }

        this.targetPos = this.getNearbyInventories(this.taskOwner.blockPosition());
        return this.targetPos != null;
    }

    @Override
    public void start() {
        this.searchCooldown = 100;
        this.continueTask = true;
        this.containerOpen = false;
        this.taskOwner.getNavigation().moveTo(
                this.targetPos.getX() + 0.5D, this.targetPos.getY() + 1.0D, this.targetPos.getZ() + 0.5D, 1.0D);
    }

    @Override
    public void stop() {
        this.closeTargetContainer();
        this.taskOwner.setSitting(false);
        this.taskOwner.getNavigation().stop();
        this.targetInventory = null;
        this.targetPos = null;
    }

    @Override
    public void tick() {
        if (this.targetPos != null && this.taskOwner.distanceToSqr(
                this.targetPos.getX() + 0.5D, this.targetPos.getY() + 0.5D, this.targetPos.getZ() + 0.5D) < 4.0D) {
            this.taskOwner.getLookControl().setLookAt(
                    this.targetPos.getX() + 0.5D, this.targetPos.getY() + 1.5D, this.targetPos.getZ() + 0.5D,
                    10.0F, this.taskOwner.getMaxHeadXRot());
            this.taskOwner.getNavigation().stop();
            this.taskOwner.setSitting(true);
            this.openTargetContainer();

            if (--this.searchCooldown <= 0) {
                this.searchCooldown = 100;
                this.continueTask = this.stealItem();
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.continueTask || this.taskOwner.getHunger() >= 60 || this.targetPos == null) {
            return false;
        }

        this.targetInventory = getInventoryAtPosition(this.taskOwner.level(), this.targetPos);
        return this.targetInventory != null && !isInventoryEmpty(this.targetInventory, Direction.UP);
    }

    private boolean stealItem() {
        if (this.targetPos == null) {
            return false;
        }

        this.targetInventory = getInventoryAtPosition(this.taskOwner.level(), this.targetPos);
        if (this.targetInventory == null || isInventoryEmpty(this.targetInventory, Direction.DOWN)) {
            return false;
        }

        Direction extractionSide = Direction.DOWN;
        if (this.targetInventory instanceof WorldlyContainer sidedInventory) {
            for (int slot : sidedInventory.getSlotsForFace(extractionSide)) {
                if (this.stealFromSlot(slot, extractionSide)) {
                    return true;
                }
            }
        } else {
            for (int slot = 0; slot < this.targetInventory.getContainerSize(); ++slot) {
                if (this.stealFromSlot(slot, extractionSide)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean stealFromSlot(int slot, Direction extractionSide) {
        ItemStack stack = this.targetInventory.getItem(slot);
        if (stack.isEmpty() || !canExtractItemFromSlot(this.targetInventory, stack, slot, extractionSide)) {
            return false;
        }

        ItemStack stolenStack = stack.copy();
        this.targetInventory.setItem(slot, ItemStack.EMPTY);
        this.targetInventory.setChanged();
        this.taskOwner.setAnimation(EntityBear.ATTACK_SWIPE);

        FoodProperties food = stolenStack.get(DataComponents.FOOD);
        if (food != null) {
            this.taskOwner.playSound(SoundEvents.PLAYER_BURP, 1.0F, 1.0F);
            this.taskOwner.addHunger(food.nutrition() * 10 * stolenStack.getCount());
            Consumable consumable = stolenStack.get(DataComponents.CONSUMABLE);
            if (consumable != null) {
                for (ConsumeEffect effect : consumable.onConsumeEffects()) {
                    effect.apply(this.taskOwner.level(), stolenStack, this.taskOwner);
                }
            }
            return true;
        }

        PotionContents potion = stolenStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        if (potion.hasEffects()) {
            this.taskOwner.playSound(SoundEvents.GENERIC_DRINK.value(), 1.0F, 1.0F);
            this.taskOwner.addHunger(10);
            this.applyPotionEffects(potion);
            return true;
        }

        if (this.taskOwner.level() instanceof ServerLevel serverLevel) {
            this.taskOwner.spawnAtLocation(serverLevel, stolenStack, 0.2F);
        }
        return true;
    }

    private void applyPotionEffects(PotionContents potion) {
        for (MobEffectInstance effect : potion.getAllEffects()) {
            if (effect.getEffect().value().isInstantaneous() && this.taskOwner.level() instanceof ServerLevel serverLevel) {
                effect.getEffect().value().applyInstantaneousEffect(
                        serverLevel, this.taskOwner, this.taskOwner, this.taskOwner, effect.getAmplifier(), 1.0D);
            } else {
                this.taskOwner.addEffect(new MobEffectInstance(effect));
            }
        }
    }

    private void openTargetContainer() {
        if (!this.containerOpen && this.targetPos != null) {
            this.setChestOpen(true);
            this.taskOwner.level().gameEvent(this.taskOwner, GameEvent.CONTAINER_OPEN, this.targetPos);
            this.containerOpen = true;
        }
    }

    private void closeTargetContainer() {
        if (this.containerOpen && this.targetPos != null) {
            this.setChestOpen(false);
            this.taskOwner.level().gameEvent(this.taskOwner, GameEvent.CONTAINER_CLOSE, this.targetPos);
            this.containerOpen = false;
        }
    }

    private void setChestOpen(boolean open) {
        Level level = this.taskOwner.level();
        BlockState state = level.getBlockState(this.targetPos);
        if (state.getBlock() instanceof ChestBlock chestBlock
                && level.getBlockEntity(this.targetPos) instanceof ChestBlockEntity) {
            level.blockEvent(this.targetPos, state.getBlock(), 1, open ? 1 : 0);
            SoundEvent sound = open ? chestBlock.getOpenChestSound() : chestBlock.getCloseChestSound();
            level.playSound(null, this.targetPos, sound, SoundSource.BLOCKS, 0.5F,
                    level.getRandom().nextFloat() * 0.1F + 0.9F);
        }
    }

    private static Container getInventoryAtPosition(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RandomizableContainer lootContainer) {
            lootContainer.unpackLootTable(null);
        }

        if (state.getBlock() instanceof ChestBlock chestBlock) {
            Container chest = ChestBlock.getContainer(chestBlock, state, level, pos, false);
            if (chest != null) {
                return chest;
            }
        }

        return blockEntity instanceof Container container ? container : null;
    }

    private static boolean isInventoryEmpty(Container inventory, Direction side) {
        if (inventory instanceof WorldlyContainer sidedInventory) {
            for (int slot : sidedInventory.getSlotsForFace(side)) {
                if (!sidedInventory.getItem(slot).isEmpty()) {
                    return false;
                }
            }
        } else {
            for (int slot = 0; slot < inventory.getContainerSize(); ++slot) {
                if (!inventory.getItem(slot).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean canExtractItemFromSlot(Container inventory, ItemStack stack, int slot, Direction side) {
        return !(inventory instanceof WorldlyContainer sidedInventory)
                || sidedInventory.canTakeItemThroughFace(slot, stack, side);
    }

    private BlockPos getNearbyInventories(BlockPos center) {
        int horizontalRange = 15;
        int verticalRange = 3;
        Level level = this.taskOwner.level();
        for (BlockPos mutablePos : BlockPos.betweenClosed(
                center.offset(-horizontalRange, -verticalRange, -horizontalRange),
                center.offset(horizontalRange, verticalRange, horizontalRange))) {
            Container inventory = getInventoryAtPosition(level, mutablePos);
            if (inventory != null && !isInventoryEmpty(inventory, Direction.UP)) {
                this.targetInventory = inventory;
                return mutablePos.immutable();
            }
        }

        this.targetInventory = null;
        return null;
    }
}