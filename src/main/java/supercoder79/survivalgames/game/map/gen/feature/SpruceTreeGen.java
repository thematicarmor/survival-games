package supercoder79.survivalgames.game.map.gen.feature;

import java.util.Random;

import net.gegy1000.plasmid.game.map.GameMapBuilder;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class SpruceTreeGen implements MapGen {
	private final BlockPos origin;

	public SpruceTreeGen(BlockPos origin) {
		this.origin = origin;
	}

	@Override
	public void generate(GameMapBuilder builder) {
		if (builder.getBlockState(origin.down()) != Blocks.GRASS_BLOCK.getDefaultState()) {
			return;
		}

		Random random = new Random();

		int heightAddition = random.nextInt(4);

		double maxRadius = 1.8 + ((random.nextDouble() - 0.5) * 0.2);

		BlockPos.Mutable mutable = origin.mutableCopy();
		for (int y = 0; y < 8 + heightAddition; y++) {
			builder.setBlockState(mutable, Blocks.SPRUCE_LOG.getDefaultState(), false);
			mutable.move(Direction.UP);
		}

		mutable = origin.mutableCopy();
		mutable.move(Direction.UP, 1 + heightAddition);

		for (int y = 0; y < 9; y++) {
			GenerationHelper.circle(mutable.mutableCopy(), maxRadius * radius(y / 10.f), leafPos -> {
				if (builder.getBlockState(leafPos).isAir()) {
					builder.setBlockState(leafPos, Blocks.SPRUCE_LEAVES.getDefaultState(), false);
				}
			});
			mutable.move(Direction.UP);
		}
	}

	private double radius(double x) {
		return -0.15 * (x * x) - x + 1.3;
	}

}
