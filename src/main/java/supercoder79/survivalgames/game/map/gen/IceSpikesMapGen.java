package supercoder79.survivalgames.game.map.gen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public class IceSpikesMapGen implements MapGen {

    public static final MapGen INSTANCE = new IceSpikesMapGen();

    @Override
    public void generate(ServerWorldAccess world, BlockPos blockPos, Random random) {
        BlockPos.Mutable mutable = blockPos.mutableCopy().move(0, -6, 0);

        if (world.getBlockState(blockPos.down()).getBlock() != Blocks.SNOW_BLOCK)
            return;

        int spikeHeight = random.nextInt(24) + 8;

        for (int i = 0; i < 8; i++)
            spikeHeight += random.nextInt(4) == 0 ? random.nextInt(8) + 4 : 0;

        BlockState PACKED_ICE = Blocks.PACKED_ICE.getDefaultState();

        for (int y = 0; y < spikeHeight; y++) {
            double radius = random.nextDouble() * 1.2d + 0.5d;
            radius += (y % (random.nextInt(4) + 2) == 0 ? random.nextDouble() / 1.6d : 0) + (y % (random.nextInt(4) + 2) == 0 ? random.nextDouble() / 1.6d : 0) + (y < 8 ? (8 - y) / 4d : 0);
            radius -= y > spikeHeight - 4 ? (y - spikeHeight + 4) : 0;
            radius += spikeHeight > 40 ? 0.2d : 0;
            radius += spikeHeight > 60 ? 0.2d : 0;
            radius += spikeHeight > 80 ? 0.2d : 0;
            radius = Math.max(0.4d, radius);

            for (int x = -(int) radius; x <= (int) radius; x++) {
                for (int z = -(int) radius; z <= (int) radius; z++) {
                    if (x * x + z * z <= radius * radius) {
                        BlockPos icePos = mutable.add(x, y, z);
                        world.setBlockState(icePos, PACKED_ICE, 3);
                    }
                }
            }
        }
    }
}
