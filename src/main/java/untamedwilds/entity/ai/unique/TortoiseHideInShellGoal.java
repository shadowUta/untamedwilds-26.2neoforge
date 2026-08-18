package untamedwilds.entity.ai.unique;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.server.level.ServerLevel;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class TortoiseHideInShellGoal<T extends LivingEntity> extends Goal {

    protected final Class<T> classToAvoid;
    protected T avoidTarget;
    protected ComplexMob taskOwner;
    protected final float avoidDistance;
    private final TargetingConditions builtTargetSelector;

    public TortoiseHideInShellGoal(ComplexMob entityIn, Class<T> classToAvoidIn, float avoidDistanceIn, final Predicate<LivingEntity> targetSelector) {
        this.taskOwner = entityIn;
        this.classToAvoid = classToAvoidIn;
        this.avoidDistance = avoidDistanceIn;
        this.builtTargetSelector = TargetingConditions.forCombat().range(avoidDistanceIn)
                .selector((candidate, level) -> targetSelector == null || targetSelector.test(candidate));
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    private List<T> findNearbyThreats() {
        if (!(this.taskOwner.level() instanceof ServerLevel serverLevel)) {
            return List.of();
        }
        return serverLevel.getEntitiesOfClass(this.classToAvoid,
                this.taskOwner.getBoundingBox().inflate(this.avoidDistance, 4.0D, this.avoidDistance),
                candidate -> candidate != this.taskOwner
                        && this.builtTargetSelector.test(serverLevel, this.taskOwner, candidate));
    }

    @Override
    public boolean canUse() {
        if (this.taskOwner.tickCount % 40 != 0) {
            return false;
        }
        if (this.taskOwner.getTarget() != null || this.taskOwner.getCommandInt() != 0 || this.taskOwner.isTame()) {
            return false;
        }

        List<T> list = this.findNearbyThreats();
        if (list.isEmpty()) {
            this.taskOwner.setSitting(false);
            return false;
        }
        this.avoidTarget = list.get(0);
        return true;
    }

    public void start() {
        super.start();
        this.taskOwner.getNavigation().stop();
        this.taskOwner.setSitting(true);
    }

    public void stop() {
        super.stop();
    }

    public void tick() {
        if (this.taskOwner.getRandom().nextInt(40) == 0) {
            List<T> list = this.findNearbyThreats();
            if (list.isEmpty()) {
                this.taskOwner.setSitting(false);
            }
        }
        if (this.taskOwner.distanceTo(this.avoidTarget) > 10) {
            this.taskOwner.setSitting(false);
        }
        super.tick();
    }

    public boolean canContinueToUse() {
        return !this.taskOwner.isSitting();
    }
}