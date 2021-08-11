package supercoder79.survivalgames.game.map.gen;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public class LavaHoleGen implements MapGen {
    public static final LavaHoleGen INSTANCE = new LavaHoleGen();

    @Override
    public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 0);
        world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), 0);
        world.setBlockState(pos.down(2), Blocks.LAVA.getDefaultState(), 0);
    }
}
