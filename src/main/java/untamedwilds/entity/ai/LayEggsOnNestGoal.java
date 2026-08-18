package untamedwilds.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.INestingMob;
import untamedwilds.init.ModBlock;

import java.lang.reflect.Method;
import java.util.EnumSet;

public class LayEggsOnNestGoal extends MoveToBlockGoal {
    private final ComplexMob taskOwner;
    private final Level world;
    private boolean hasReachedDestination;
    private boolean needsToBuildNest = false;
    private int nestBuildingTicks;

    public LayEggsOnNestGoal(ComplexMob entityIn) {
        super(entityIn, 1, 16, 4);
        this.taskOwner = entityIn;
        this.world = entityIn.level();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.taskOwner.level().isClientSide() || !(this.taskOwner instanceof INestingMob) || !((INestingMob)taskOwner).wantsToLayEggs())
            return false;
        if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        } else {
            needsToBuildNest = false;
            this.nextStartTick = this.nextStartTick(this.mob);
            if (this.findNearestBlock())
                return true;
            needsToBuildNest = true;
            return this.checkForNewNest();
        }
    }

    public boolean canContinueToUse() {
        return ((INestingMob)this.taskOwner).wantsToLayEggs() && super.canContinueToUse();
    }

    @Override
    public double acceptedDistance() {
        return 1.0D;
    }

    public void tick() {
        super.tick();
        BlockPos blockpos = this.getMoveToTarget();
        if (!isWithinXZDist(blockpos, this.mob.position(), this.acceptedDistance())) {
            this.hasReachedDestination = false;
            ++this.tryTicks;
            if (this.shouldRecalculatePath()) {
                this.mob.getNavigation().moveTo((double) ((float) blockpos.getX()) + 0.5D, blockpos.getY(), (double) ((float) blockpos.getZ()) + 0.5D, this.speedModifier);
            }
        } else {
            this.hasReachedDestination = true;
            --this.tryTicks;
        }
        //((ServerLevel)this.taskOwner.level).sendParticles(ParticleTypes.ANGRY_VILLAGER, blockpos.getX(), blockpos.getY(), blockpos.getZ(), 20, 0.0D, 0.0D, 0.0D, 0.15F);

        if (this.isReachedTarget()) {
            if (this.needsToBuildNest) {
                --this.nestBuildingTicks;
                if (this.nestBuildingTicks % 30 == 0) {
                    if (this.taskOwner.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, serverLevel.getBlockState(this.blockPos.below())), this.taskOwner.getX(), this.taskOwner.getY(), this.taskOwner.getZ(), 20, 0.0D, 0.0D, 0.0D, 0.15F);
                    }
                    this.taskOwner.playSound(SoundEvents.SHOVEL_FLATTEN, 0.8F, 0.6F);
                }
                if (this.nestBuildingTicks <= 0) {
                    this.world.setBlockAndUpdate(this.blockPos, ModBlock.NEST_REPTILE.value().defaultBlockState());
                    BlockEntity nest = this.world.getBlockEntity(this.blockPos);
                    if (nest != null) {
                        this.writeNestData(nest, 0);
                    }
                    this.needsToBuildNest = false;
                }
            }
            else {
                this.addEggsToNest();
                stop();
            }
        }
    }

    public boolean checkForNewNest() {
        RandomSource random = this.taskOwner.getRandom();
        BlockPos blockpos = this.taskOwner.blockPosition();
        for(int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(8) - 4, random.nextInt(4) - 2, random.nextInt(8) - 4);
            if (((INestingMob)this.taskOwner).isValidNestBlock(blockpos1) && this.isValidTarget(this.mob.level(), blockpos1)) {
                this.nestBuildingTicks = 400 + random.nextInt(300);
                this.blockPos = blockpos1;
                return true;
            }
        }

        return false;
    }

    private void addEggsToNest() {
        Level level = this.taskOwner.level();
        BlockState blockstate = level.getBlockState(this.blockPos);
        if (blockstate.is(ModBlock.NEST_REPTILE.value())) {
            BlockEntity nest = level.getBlockEntity(this.blockPos);
            if (nest != null && ((INestingMob)this.taskOwner).wantsToLayEggs()) {
                if (this.addEggs(nest, this.taskOwner.getOffspring())) {
                    level.updateNeighbourForOutputSignal(this.blockPos, blockstate.getBlock());
                    nest.setChanged();
                }
                ((INestingMob)taskOwner).setEggStatus(false);
            }
        }
    }

    private boolean writeNestData(BlockEntity nest, int eggCount) {
        try {
            Method setEntityType = nest.getClass().getMethod("setEntityType", EntityType.class);
            Method setVariant = nest.getClass().getMethod("setVariant", int.class);
            Method setEggCount = nest.getClass().getMethod("setEggCount", int.class);
            setEntityType.invoke(nest, this.taskOwner.getType());
            setVariant.invoke(nest, this.taskOwner.getVariant());
            setEggCount.invoke(nest, eggCount);
            nest.setChanged();
            return true;
        } catch (ReflectiveOperationException exception) {
            return false;
        }
    }

    private boolean addEggs(BlockEntity nest, int eggCount) {
        try {
            Method getEggCount = nest.getClass().getMethod("getEggCount");
            Method setEggCount = nest.getClass().getMethod("setEggCount", int.class);
            int currentEggCount = (int)getEggCount.invoke(nest);
            setEggCount.invoke(nest, currentEggCount + eggCount);
            return true;
        } catch (ReflectiveOperationException exception) {
            return false;
        }
    }

    private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
        return blockpos.distSqr(BlockPos.containing(positionVec.x(), blockpos.getY(), positionVec.z())) < distance * distance;
    }

    protected boolean isReachedTarget() {
        return this.hasReachedDestination;
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        if (this.needsToBuildNest) {
            //((ServerLevel)this.taskOwner.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 20, 0.0D, 0.0D, 0.0D, 0.15F);
            return ((INestingMob)this.taskOwner).isValidNestBlock(pos);
        }
        BlockEntity nest = worldIn.getBlockEntity(pos);
        return worldIn.getBlockState(pos).is(ModBlock.NEST_REPTILE.value()) && nest != null && this.hasMatchingVariant(nest);
    }

    private boolean hasMatchingVariant(BlockEntity nest) {
        try {
            Method getVariant = nest.getClass().getMethod("getVariant");
            return (int)getVariant.invoke(nest) == this.taskOwner.getVariant();
        } catch (ReflectiveOperationException exception) {
            return false;
        }
    }
}