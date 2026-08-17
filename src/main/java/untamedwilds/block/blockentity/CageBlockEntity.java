package untamedwilds.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.Objects;

public class CageBlockEntity extends BlockEntity {

    private CompoundTag data;
    private boolean locked;

    public CageBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlock.TILE_ENTITY_CAGE.get(), pos, state);
    }

    public static boolean isBlacklisted(Entity entity) {
        return entity.getType().builtInRegistryHolder().is(ModTags.EntityTags.CAGE_BLACKLIST);
    }

    public boolean cageEntity(Mob entity) {
        if (!this.isLocked()) {
            if (!isBlacklisted(entity) && (ConfigGamerules.easyMobCapturing.get() || entity.getTarget() == null)) {
                this.setTagCompound(EntityUtils.writeEntityToNBT(entity));
                this.setLocked(true);
                entity.discard();
                setChanged();
                return true;
            }
        }
        return false;
    }

    public boolean spawnCagedCreature(ServerLevel worldIn, BlockPos pos, boolean offsetHitbox) {
        return false;
    }

    @Nullable
    public CompoundTag getTagCompound() { return this.data; }

    private void setTagCompound(@Nullable CompoundTag nbt) { this.data = nbt; }

    public boolean hasTagCompound() { return this.data != null; }

    public boolean isLocked() { return this.locked; }

    private void setLocked(boolean locked) { this.locked = locked; }

}