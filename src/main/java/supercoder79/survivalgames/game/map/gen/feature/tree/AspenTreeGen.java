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

public class AspenTreeGen implements MapGen {
	private static final BlockState LEAVES = Blocks.BIRCH_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1);
	private final BlockPos origin;

	public AspenTreeGen(BlockPos origin) {
		this.origin = origin;
	}

	@Override
	public void generate(GameMapBuilder builder) {
		if (builder.getBlockState(origin.down()) != Blocks.GRASS_BLOCK.getDefaultState() || !builder.getBlockState(origin).isAir()) {
			return;
		}

		Random random = new Random();

		double maxRadius = 2 + ((random.nextDouble() - 0.5) * 0.2);
		int leafDistance = random.nextInt(4) + 3;

		BlockPos.Mutable mutable = origin.mutableCopy();
		for (int y = 0; y < 8; y++) {
			builder.setBlockState(mutable, Blocks.BIRCH_LOG.getDefaultState(), false);
			//add branch blocks
			if (maxRadius * radius(y / 7.f) > 2.3) {
				Direction.Axis axis = getAxis(random);
				builder.setBlockState(mutable.offset(getDirection(axis, random)).up(leafDistance), Blocks.BIRCH_LOG.getDefaultState().with(Properties.AXIS, axis), false);
			}

			mutable.move(Direction.UP);
		}

		mutable = origin.mutableCopy();
		mutable.move(Direction.UP, leafDistance);

		for (int y = 0; y < 8; y++) {
			GenHelper.circle(mutable.mutableCopy(), maxRadius * radius(y / 7.f), leafPos -> {
				if (builder.getBlockState(leafPos).isAir()) {
					builder.setBlockState(leafPos, LEAVES, false);
				}
			});
			mutable.move(Direction.UP);
		}
	}

	private double radius(double x) {
		return -Math.pow(((1.4 * x) - 0.3), 2) + 1.2;
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
