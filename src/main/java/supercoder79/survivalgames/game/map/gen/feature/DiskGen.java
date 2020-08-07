package supercoder79.survivalgames.game.map.gen.feature;

import java.util.Random;

import net.gegy1000.plasmid.game.map.GameMapBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.math.BlockPos;

public class DiskGen implements MapGen {
	private static final WeightedList<BlockState> STATES = new WeightedList<BlockState>()
			.add(Blocks.SAND.getDefaultState(), 1)
			.add(Blocks.GRAVEL.getDefaultState(), 1);

	private final BlockPos origin;

	public DiskGen(BlockPos origin) {
		this.origin = origin;
	}

	@Override
	public void generate(GameMapBuilder builder) {
		Random random = new Random();

		int radius = random.nextInt(5) + 2;
		int radiusSquared = radius * radius;

		BlockPos.Mutable mutable = new BlockPos.Mutable();
		BlockState state = STATES.pickRandom(random);

		for(int x = origin.getX() - radius; x <= origin.getX() + radius; ++x) {
			for (int z = origin.getZ() - radius; z <= origin.getZ() + radius; ++z) {
				int localX = x - origin.getX();
				int localZ = z - origin.getZ();
				if (localX * localX + localZ * localZ <= radiusSquared) {
					for(int y = origin.getY() - 2; y <= origin.getY() + 2; ++y) {
						mutable.set(x, y, z);

						if (builder.getBlockState(mutable).isOf(Blocks.DIRT) || builder.getBlockState(mutable).isOf(Blocks.GRASS_BLOCK)) {
							builder.setBlockState(mutable, state, false);
						}
					}
				}
			}
		}
	}
}
