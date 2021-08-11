package supercoder79.survivalgames.game.map.gen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public class GroundCoverGen implements MapGen {
    public static MapGen SNOW = new GroundCoverGen(Blocks.SNOW.getDefaultState(), 1);
    private final BlockState state;
    private final int chance;

    public GroundCoverGen(BlockState state, int chance) {
        this.state = state;
        this.chance = chance;
    }
    @Override
    public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
        if (random.nextInt(chance) == 0) {
            world.setBlockState(pos, state, 0);
        }
    }
}
