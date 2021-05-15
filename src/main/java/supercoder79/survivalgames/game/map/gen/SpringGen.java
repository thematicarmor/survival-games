package supercoder79.survivalgames.game.map.gen;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import xyz.nucleoid.plasmid.game.gen.MapGen;

import java.util.Random;

public class SpringGen implements MapGen {
    public static MapGen INSTANCE = new SpringGen();
    @Override
    public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
        // Prepare yourself for the monstrosity you are about to see
        // You will likely never recover
        // Please press ALT+F4 now to save yourself


        // You have chosen to stay
        // This code is a rather cursed way to generate small streams and puddles of water
        if (pos.getY() < 56) {
            // Ensure water does not float
            BlockPos.Mutable mutable = pos.mutableCopy();
            mutable.down((random.nextInt(1) + 1));
            while ((!world.getBlockState(mutable.west()).equals(Blocks.AIR.getDefaultState()) && !world.getBlockState(mutable.east()).equals(Blocks.AIR.getDefaultState())
                    && !world.getBlockState(mutable.north()).equals(Blocks.AIR.getDefaultState()) && !world.getBlockState(mutable.south()).equals(Blocks.AIR.getDefaultState()))) {
                mutable.down();
            }
            world.setBlockState(mutable, Blocks.WATER.getDefaultState(), 0);
            world.getFluidTickScheduler().schedule(mutable, world.getFluidState(mutable).getFluid(), 0);
        }
    }
}
