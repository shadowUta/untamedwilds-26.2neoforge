package untamedwilds.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import untamedwilds.UntamedWilds;
import untamedwilds.block.blockentity.ReptileNestBlockEntity;
import untamedwilds.config.ConfigMobControl;

import java.util.Random;

public class NestReptileBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {

    protected static final VoxelShape SHAPE = Block.box(1D, 0.0D, 1D, 15D, 3.5D, 15D);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public NestReptileBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = this.defaultBlockState();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return blockstate.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collision) {
        return SHAPE;
    }

    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    protected boolean isValidGround(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return !state.getCollisionShape(worldIn, pos).getFaceShape(Direction.UP).isEmpty();
    }

    protected BlockState updateShape(BlockState stateIn, LevelReader worldIn, ScheduledTickAccess ticks, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
        if (stateIn.getValue(WATERLOGGED)) {
            ticks.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        if (!canSurvive(stateIn, worldIn, currentPos)) {
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(stateIn, worldIn, ticks, currentPos, facing, facingPos, facingState, random);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReptileNestBlockEntity(pos, state);
    }

    public void fallOn(Level levelIn, BlockState stateIn, BlockPos posIn, Entity entityIn, float p_154849_) {
        if (levelIn.getBlockEntity(posIn) instanceof ReptileNestBlockEntity te
                && !entityIn.getType().equals(te.getEntityType())) {
            te.trampleOnNest(levelIn, posIn, stateIn);
        }

        super.fallOn(levelIn, stateIn, posIn, entityIn, p_154849_);
    }

    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    protected void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (ConfigMobControl.tickingNests.get() && worldIn.getBlockEntity(pos) instanceof ReptileNestBlockEntity burrow) {
            burrow.createMobs(worldIn);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hit) {
        if (worldIn.isClientSide()) {
            return InteractionResult.FAIL;
        }
        else {
            if (worldIn.getBlockEntity(pos) instanceof ReptileNestBlockEntity te) {
                if (playerIn.isCreative() && playerIn.isSteppingCarefully()) {
                    UntamedWilds.LOGGER.info(te.getEggCount()); // TODO: DEBUG
                }
                else {
                    te.removeEggs(worldIn, 1);
                    CompoundTag baseTag = new CompoundTag();
                    ItemStack item = new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(UntamedWilds.MOD_ID + ":egg_" + te.getEntityType().builtInRegistryHolder().key().identifier().getPath())));
                    baseTag.putInt("variant", te.getVariant());
                    baseTag.putInt("custom_model_data", te.getVariant());
                    item.set(DataComponents.CUSTOM_DATA, CustomData.of(baseTag));
                    item.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(java.util.List.of((float)te.getVariant()), java.util.List.of(), java.util.List.of(), java.util.List.of()));
                    playerIn.getInventory().add(item);
                }
            }
            return InteractionResult.SUCCESS;
        }
    }
}