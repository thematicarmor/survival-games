package supercoder79.survivalgames.game.map.gen;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public class FireGen implements MapGen {
    public static final FireGen INSTANCE = new FireGen();

    @Override
    public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        if (world.getBlockState(mutable.down()) == Blocks.SOUL_SOIL.getDefaultState() || world.getBlockState(mutable.down(2)) == Blocks.SOUL_SAND.getDefaultState()) {
            // Spawn soul fire
            world.setBlockState(pos, Blocks.SOUL_FIRE.getDefaultState(), 0);
        } else {
            world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 0);
        }
    }
}
