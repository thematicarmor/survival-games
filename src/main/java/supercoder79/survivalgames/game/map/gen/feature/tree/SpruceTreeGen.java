package supercoder79.survivalgames.game.map.gen.feature.tree;

import java.util.Random;

import net.gegy1000.plasmid.game.map.GameMapBuilder;
import supercoder79.survivalgames.game.map.gen.GenHelper;
import supercoder79.survivalgames.game.map.gen.feature.MapGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class SpruceTreeGen implements MapGen {
	private static final BlockState LEAVES = Blocks.SPRUCE_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1);
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
			GenHelper.circle(mutable.mutableCopy(), maxRadius * radius(y / 10.f), leafPos -> {
				if (builder.getBlockState(leafPos).isAir()) {
					builder.setBlockState(leafPos, LEAVES, false);
				}
			});
			mutable.move(Direction.UP);
		}
	}

	private double radius(double x) {
		return -0.15 * (x * x) - x + 1.3;
	}

}
