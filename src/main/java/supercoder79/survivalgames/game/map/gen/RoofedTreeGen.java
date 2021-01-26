package supercoder79.survivalgames.game.map.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import xyz.nucleoid.plasmid.game.gen.MapGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;

public class RoofedTreeGen implements MapGen {
	public static final MapGen INSTANCE = new RoofedTreeGen(Blocks.DARK_OAK_LOG.getDefaultState(), Blocks.DARK_OAK_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), 8);
	private final BlockState log;
	private final BlockState leaves;
	private final int height;

	public RoofedTreeGen(BlockState log, BlockState leaves, int height) {
		this.log = log;
		this.leaves = leaves;
		this.height = height;
	}

	@Override
	public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
		if (world.getBlockState(pos.down()) != Blocks.GRASS_BLOCK.getDefaultState()) return;

		int height = this.height + random.nextInt(Math.max(1, this.height / 4));
		int branchThreshold = (int) (height * 0.4);
		List<BlockPos> leaves = new ArrayList<>();

		BlockPos.Mutable mutable = pos.mutableCopy();
		for (int y = 0; y <= height; y++) {
			world.setBlockState(mutable, this.log, 3);

			if (y > branchThreshold && random.nextInt(2) == 0) {
				BlockPos local = mutable.toImmutable();
				double theta = random.nextDouble() * Math.PI * 2;
				// TODO: scale with height
				int branchLength = random.nextInt(3) + 1;
				for (int i = 0; i <= branchLength; i++) {
					int dx = (int) (Math.cos(theta) * i);
					int dy = i / 2;
					int dz = (int) (Math.sin(theta) * i);

					world.setBlockState(local.add(dx, dy, dz), this.log, 3);

					if (i == branchLength) {
						leaves.add(local.add(dx, dy, dz).toImmutable());
					}
				}
			}

			if (y == height) {
				BlockPos local = mutable.toImmutable();
				int topCount = 2 + random.nextInt(3);

				for (int i = 0; i < topCount; i++) {

					double theta = (i / (double) topCount) * Math.PI * 2;
					theta += random.nextDouble() * 0.3;

					int branchLength = random.nextInt(4) + 2;
					for (int j = 0; i <= branchLength; i++) {
						int dx = (int) (Math.cos(theta) * j);
						int dz = (int) (Math.sin(theta) * j);

						world.setBlockState(local.add(dx, j, dz), this.log, 3);

						if (i == branchLength) {
							leaves.add(local.add(dx, j, dz).toImmutable());
						}
					}
				}

				break;
			}

			mutable.move(Direction.UP);
		}

		for (BlockPos leaf : leaves) {
			for(int x = -1; x <= 1; x++) {
			    for(int z = -1; z <= 1; z++) {
			        BlockPos local = leaf.add(x, 1, z);
			        if (world.getBlockState(local).isAir()) {
			        	world.setBlockState(local, this.leaves, 3);
					}
			    }
			}

			for(int x = -2; x <= 2; x++) {
				for(int z = -2; z <= 2; z++) {
					if (Math.abs(x) == 2 && Math.abs(z) == 2) continue;

					BlockPos local = leaf.add(x, 0, z);
					if (world.getBlockState(local).isAir()) {
						world.setBlockState(local, this.leaves, 3);
					}
				}
			}
		}
	}
}
