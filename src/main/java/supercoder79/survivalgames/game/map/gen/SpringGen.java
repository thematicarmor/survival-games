package supercoder79.survivalgames.game.map.gen;

import net.minecraft.block.Blocks;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public class SpringGen implements MapGen {
    public static MapGen INSTANCE = new SpringGen();

    @Override
    public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        for (int i = 0; i < 12; i++) {
            int dx = random.nextInt(8) - random.nextInt(8) + pos.getX();
            int dz = random.nextInt(8) - random.nextInt(8) + pos.getZ();
            int y = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, dx, dz) - 1;

            mutable.set(dx, y, dz);

            // ensure there is a block under the water
            if (!world.getBlockState(mutable.down()).isOpaque()) {
                continue;
            }

            boolean canSpawn = true;

            // check surrounding for non-opaque blocks
            BlockPos origin = mutable.toImmutable();
            for (Direction direction : Direction.Type.HORIZONTAL) {
                mutable.set(origin, direction);

                if (!world.getBlockState(mutable).isOpaque()) {
                    if (!world.getFluidState(mutable).isIn(FluidTags.WATER)) {
                        canSpawn = false;

                        break;
                    }

                }
            }

            if (canSpawn) {
                world.setBlockState(mutable.set(origin), Blocks.WATER.getDefaultState(), 3);
                world.getFluidTickScheduler().schedule(mutable, world.getFluidState(mutable).getFluid(), 0);
            }
        }
    }
}
