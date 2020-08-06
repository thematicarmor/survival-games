package supercoder79.survivalgames.game.map.gen.feature;

import java.util.Random;

import net.gegy1000.plasmid.game.map.GameMapBuilder;

import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PoplarTreeGen implements MapGen {
	private final BlockPos origin;

	public PoplarTreeGen(BlockPos origin) {
		this.origin = origin;
	}

	@Override
	public void generate(GameMapBuilder builder) {
		if (builder.getBlockState(origin.down()) != Blocks.GRASS_BLOCK.getDefaultState() || !builder.getBlockState(origin).isAir()) {
			return;
		}

		Random random = new Random();

		double maxRadius = 2.6 + ((random.nextDouble() - 0.5) * 0.2);
		int leafDistance = random.nextInt(3) + 2;

		BlockPos.Mutable mutable = origin.mutableCopy();
		for (int y = 0; y < 12; y++) {
			builder.setBlockState(mutable, Blocks.OAK_LOG.getDefaultState(), false);
			//add branch blocks
			if (maxRadius * radius(y / 11.f) > 2.3) {
				Direction.Axis axis = getAxis(random);
				builder.setBlockState(mutable.offset(getDirection(axis, random)).up(leafDistance), Blocks.OAK_LOG.getDefaultState().with(Properties.AXIS, axis), false);
			}

			mutable.move(Direction.UP);
		}

		mutable = origin.mutableCopy();
		mutable.move(Direction.UP, leafDistance);

		for (int y = 0; y < 12; y++) {
			GenerationHelper.circle(mutable.mutableCopy(), maxRadius * radius(y / 11.f), leafPos -> {
				if (builder.getBlockState(leafPos).isAir()) {
					builder.setBlockState(leafPos, Blocks.OAK_LEAVES.getDefaultState(), false);
				}
			});
			mutable.move(Direction.UP);
		}
	}

	private double radius(double x) {
		return (-2 * (x * x * x)) + (1.9 * x) + 0.2;
	}

	private Direction.Axis getAxis(Random random) {
		return random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
	}

	private Direction getDirection(Direction.Axis axis, Random random) {
		if (axis == Direction.Axis.X) {
			return random.nextBoolean() ? Direction.EAST : Direction.WEST;
		} else {
			return random.nextBoolean() ? Direction.NORTH : Direction.SOUTH;
		}
	}
}
