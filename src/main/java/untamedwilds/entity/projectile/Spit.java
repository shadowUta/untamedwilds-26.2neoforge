package untamedwilds.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.common.NeoForge;

public class Spit extends Projectile {
    public Spit(EntityType<? extends Spit> p_37224_, Level p_37225_) {
        super(p_37224_, p_37225_);
    }

    public void tick() {
        super.tick();
        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !NeoForge.EVENT_BUS.post(new ProjectileImpactEvent(this, hitresult)).isCanceled())
            this.onHit(hitresult);
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();
        float f = 0.99F;
        float f1 = 0.06F;
        if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.discard();
        } else if (this.isInWater()) {
            this.discard();
        } else {
            this.setDeltaMovement(vec3.scale(0.99F));
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.06F, 0.0D));
            }

            this.setPos(d0, d1, d2);
        }
    }

    protected void onHitEntity(EntityHitResult p_37241_) {
        super.onHitEntity(p_37241_);
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            if (this.level() instanceof ServerLevel serverLevel) {
                p_37241_.getEntity().hurtServer(serverLevel, serverLevel.damageSources().spit(this, (LivingEntity) entity), 1.0F);
            }
        }

    }

    protected void onHitBlock(BlockHitResult p_37239_) {
        super.onHitBlock(p_37239_);
        if (!this.level().isClientSide()) {
            this.discard();
        }

    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }

    public void recreateFromPacket(ClientboundAddEntityPacket p_150162_) {
        super.recreateFromPacket(p_150162_);
        Vec3 movement = p_150162_.getMovement();
        double d0 = movement.x;
        double d1 = movement.y;
        double d2 = movement.z;

        for(int i = 0; i < 7; ++i) {
            double d3 = 0.4D + 0.1D * (double)i;
            this.level().addParticle(ParticleTypes.SPIT, this.getX(), this.getY(), this.getZ(), d0 * d3, d1, d2 * d3);
        }

        this.setDeltaMovement(d0, d1, d2);
    }
}
