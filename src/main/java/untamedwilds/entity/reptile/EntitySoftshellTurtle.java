package untamedwilds.entity.reptile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import oshi.util.tuples.Pair;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.control.look.SmartSwimmerLookControl;
import untamedwilds.entity.ai.control.movement.SmartSwimmingMoveControl;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModItems;
import untamedwilds.init.ModTags;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;

public class EntitySoftshellTurtle extends ComplexMobAmphibious implements ISpecies, INewSkins, INestingMob {

    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(EntitySoftshellTurtle.class, EntityDataSerializers.BOOLEAN);

    public boolean hasExtendedNeck;
    public int extendNeckProgress;
    public Pair<Float, Float> head_movement;
    private float neck_val = 0;
    private float head_val = 0;

    public EntitySoftshellTurtle(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        this.moveControl = new SmartSwimmingMoveControl(this, 60, 10, 0.6F, 0.25F, true);
        this.lookControl = new SmartSwimmerLookControl(this, 20);
        this.head_movement = new Pair<>(0F, 0F);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_EGG, false);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0D)
                .add(Attributes.MOVEMENT_SPEED, 0.7D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .add(Attributes.ARMOR, 2D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 0.7D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1D, 1.1D, input -> getEcoLevel(input) > getEcoLevel(this)));
        this.goalSelector.addGoal(3, new LayEggsOnNestGoal(this));
        this.goalSelector.addGoal(3, new AmphibiousTransition(this, 1D));
        this.goalSelector.addGoal(4, new AmphibiousRandomSwimGoal(this, 0.7, 40));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, input -> getEcoLevel(input) < getEcoLevel(this)));
    }

    public boolean wantsToBeOnLand() {
        long timeOfDay = this.level().getDefaultClockTime() % 24000L;
        return timeOfDay > 4500L && timeOfDay < 7500L;
    }

    public boolean wantsToBeInWater() { return !this.wantsToBeOnLand(); }

    public boolean isPushedByFluid() {
        return false;
    }

    public void die(DamageSource cause) {
        if (cause.is(DamageTypes.FALLING_ANVIL) && !this.isBaby() && !this.level().isClientSide()) {
            // Advancement Trigger: "Unethical Soup"
            ItemEntity entityitem = this.spawnAtLocation((ServerLevel) this.level(), new ItemStack(ModItems.FOOD_TURTLE_SOUP.get()), 0.2F);
            if (entityitem != null) {
                entityitem.getItem().setCount(1);
            }
        }
        super.die(cause);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide() && this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.03D) {
            Vec3 vec3 = this.getViewVector(0.0F);
            float f = Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
            float f1 = Mth.sin(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
            float f2 = 1.2F - this.random.nextFloat() * 0.7F;

            for(int i = 0; i < 2; ++i) {
                this.level().addParticle(ParticleTypes.DOLPHIN, this.getX() - vec3.x * (double)f2 + (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 + (double)f1, 0.0D, 0.0D, 0.0D);
                this.level().addParticle(ParticleTypes.DOLPHIN, this.getX() - vec3.x * (double)f2 - (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 - (double)f1, 0.0D, 0.0D, 0.0D);
            }
        }
        if (this.tickCount % 1000 == 0) {
            this.hasExtendedNeck = this.random.nextBoolean();
        }
        if (this.tickCount % 120 < 11) {
            if (this.tickCount % 120 == 1) {
                neck_val = (float) (0.8F - this.random.nextDouble() * 1.6F);
                head_val = (float) (0.4F - this.random.nextDouble() * 0.8F);
            }
            this.head_movement = new Pair<>(Mth.lerp(0.1F, head_movement.getA(), neck_val), Mth.lerp(0.1F, head_movement.getB(), head_val));
        }
    }

    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            if (this.level().getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
            if (this.isInWater() && this.getNavigation().isDone()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.003D, 0.0D));
            }
        }
        else {
            if ((!this.isInWater() || this.hasExtendedNeck) && this.extendNeckProgress < 100) {
                this.extendNeckProgress++;
            } else if ((this.isInWater() || !this.hasExtendedNeck) && this.extendNeckProgress > 0) {
                this.extendNeckProgress--;
            }
        }
    }

    /* Breeding conditions for the softshell_turtle are:
     * A nearby softshell_turtle of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntitySoftshellTurtle> list = this.level().getEntitiesOfClass(EntitySoftshellTurtle.class, this.getBoundingBox().inflate(6.0D, 4.0D, 6.0D));
                list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
                return list.size() >= 1;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityUtils.dropEggs(this, "egg_softshell_turtle", this.getOffspring());
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (itemstack.isEmpty() && this.isAlive()) {
            EntityUtils.turnEntityIntoItem(this, "spawn_softshell_turtle");
            return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        SoundEvent soundevent = this.isBaby() ? SoundEvents.TURTLE_SHAMBLE_BABY : SoundEvents.TURTLE_SHAMBLE;
        this.playSound(soundevent, 0.15F, 1.0F);
    }

    @Override
    public boolean wantsToLayEggs() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setEggStatus(boolean status) {
        this.entityData.set(HAS_EGG, status);
    }

    @Override
    public Block getNestType() {
        return ModBlock.NEST_REPTILE.get();
    }

    @Override
    public boolean isValidNestBlock(BlockPos pos) {
        return this.level().isEmptyBlock(pos) && this.level().getBlockState(pos.below()).is(ModTags.ModBlockTags.VALID_REPTILE_NEST) && this.getNestType().defaultBlockState().canSurvive(this.level(), pos);
    }

    public void addAdditionalSaveData(ValueOutput output){
        super.addAdditionalSaveData(output);
        output.putBoolean("has_egg", this.wantsToLayEggs());
    }

    public void readAdditionalSaveData(ValueInput input){
        super.readAdditionalSaveData(input);
        this.setEggStatus(input.getBooleanOr("has_egg", false));
    }

    private static final class LayEggsOnNestGoal extends Goal {
        private final EntitySoftshellTurtle turtle;
        private BlockPos target;
        private boolean buildNest;
        private int buildTicks;

        private LayEggsOnNestGoal(EntitySoftshellTurtle turtle) {
            this.turtle = turtle;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            if (!this.turtle.wantsToLayEggs() || this.turtle.level().isClientSide()) {
                return false;
            }
            this.target = this.findNestSite();
            return this.target != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.turtle.wantsToLayEggs();
        }

        @Override
        public void start() {
            this.buildTicks = this.buildNest ? 400 + this.turtle.getRandom().nextInt(300) : 0;
            this.moveToTarget();
        }

        @Override
        public void tick() {
            if (this.target == null) {
                return;
            }
            if (this.turtle.distanceToSqr(Vec3.atCenterOf(this.target)) > 2.25D) {
                if (this.turtle.tickCount % 20 == 0) {
                    this.moveToTarget();
                }
                return;
            }
            if (this.buildNest && --this.buildTicks > 0) {
                if (this.buildTicks % 30 == 0) {
                    this.turtle.playSound(SoundEvents.SHOVEL_FLATTEN, 0.8F, 0.6F);
                }
                return;
            }
            ServerLevel level = (ServerLevel)this.turtle.level();
            if (this.buildNest) {
                level.setBlockAndUpdate(this.target, this.turtle.getNestType().defaultBlockState());
            }
            BlockEntity nest = level.getBlockEntity(this.target);
            if (nest != null && this.writeNestData(nest)) {
                level.updateNeighbourForOutputSignal(this.target, level.getBlockState(this.target).getBlock());
                this.turtle.setEggStatus(false);
            }
            this.stop();
        }

        @Override
        public void stop() {
            this.turtle.getNavigation().stop();
            this.target = null;
            this.buildNest = false;
        }

        private void moveToTarget() {
            this.turtle.getNavigation().moveTo(this.target.getX() + 0.5D, this.target.getY(), this.target.getZ() + 0.5D, 1.0D);
        }

        private BlockPos findNestSite() {
            BlockPos origin = this.turtle.blockPosition();
            for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-16, -4, -16), origin.offset(16, 4, 16))) {
                if (this.turtle.level().getBlockState(pos).is(this.turtle.getNestType()) && this.turtle.level().getBlockEntity(pos) != null) {
                    this.buildNest = false;
                    return pos.immutable();
                }
            }
            for (int attempt = 0; attempt < 20; ++attempt) {
                BlockPos pos = origin.offset(this.turtle.getRandom().nextInt(17) - 8, this.turtle.getRandom().nextInt(5) - 2, this.turtle.getRandom().nextInt(17) - 8);
                if (this.turtle.isValidNestBlock(pos)) {
                    this.buildNest = true;
                    return pos.immutable();
                }
            }
            return null;
        }

        private boolean writeNestData(BlockEntity nest) {
            try {
                Method setEntityType = nest.getClass().getMethod("setEntityType", EntityType.class);
                Method setVariant = nest.getClass().getMethod("setVariant", int.class);
                Method getEggCount = nest.getClass().getMethod("getEggCount");
                Method setEggCount = nest.getClass().getMethod("setEggCount", int.class);
                setEntityType.invoke(nest, this.turtle.getType());
                setVariant.invoke(nest, this.turtle.getVariant());
                int eggs = (int)getEggCount.invoke(nest);
                setEggCount.invoke(nest, eggs + this.turtle.getOffspring());
                nest.setChanged();
                return true;
            } catch (ReflectiveOperationException exception) {
                return false;
            }
        }
    }
}
