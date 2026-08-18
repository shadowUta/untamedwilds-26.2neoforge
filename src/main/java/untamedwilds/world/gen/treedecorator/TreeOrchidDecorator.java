package untamedwilds.world.gen.treedecorator;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import untamedwilds.block.EpyphitePlantBlock;
import untamedwilds.init.ModBlock;
import untamedwilds.world.UntamedWildsGenerator;

public class TreeOrchidDecorator extends TreeDecorator {
    public static final TreeOrchidDecorator INSTANCE = new TreeOrchidDecorator();
    public static final MapCodec<TreeOrchidDecorator> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public void place(Context context) {
        if (context.logs().isEmpty() || context.random().nextFloat() >= 0.95F) {
            return;
        }
        int baseY = context.logs().getFirst().getY();
        context.logs().stream()
            .filter(logPos -> logPos.getY() - baseY <= 2)
            .forEach(logPos -> {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (context.random().nextFloat() <= 0.25F) {
                        Direction opposite = direction.getOpposite();
                        BlockPos blockpos = logPos.offset(opposite.getStepX(), 0, opposite.getStepZ());
                        if (context.isAir(blockpos)) {
                            BlockState blockstate = ModBlock.ORCHID_RED.get().defaultBlockState().setValue(EpyphitePlantBlock.FACING, direction);
                            context.setBlock(blockpos, blockstate);
                        }
                    }
                }
            });
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return UntamedWildsGenerator.TREE_ORCHID.get();
    }
}