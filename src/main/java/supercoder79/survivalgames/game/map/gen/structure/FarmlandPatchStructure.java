package supercoder79.survivalgames.game.map.gen.structure;

import java.util.Random;

import net.gegy1000.plasmid.game.map.GameMapBuilder;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import supercoder79.survivalgames.game.map.loot.LootProviders;

import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FarmlandPatchStructure implements StructureGen {
	private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

	private final BlockPos origin;

	public FarmlandPatchStructure(BlockPos origin) {
		this.origin = origin;
	}

	@Override
	public void generate(GameMapBuilder builder) {
		Random random = new Random();
		for (int i = 0; i < 196; i++) {
			int aX = random.nextInt(16) - random.nextInt(16);
			int aY = random.nextInt(4) - random.nextInt(4);
			int aZ = random.nextInt(16) - random.nextInt(16);

			BlockPos local = origin.add(aX, aY, aZ);
			if (builder.getBlockState(local) == Blocks.GRASS_BLOCK.getDefaultState()) {
				boolean canSpawn = true;

				for (Direction direction : HORIZONTALS) {
					BlockPos dLocal = local.offset(direction);
					if (!builder.getBlockState(dLocal).isOpaque()) {
						if (!builder.getBlockState(dLocal).isOf(Blocks.WATER)) {
							canSpawn = false;
						}

						break;
					}
				}

				if (canSpawn) {
					if (random.nextInt(3) == 0) {
						builder.setBlockState(local, Blocks.WATER.getDefaultState(), false);
					} else {
						builder.setBlockState(local, Blocks.FARMLAND.getDefaultState().with(Properties.MOISTURE, 7), false);
						builder.setBlockState(local.up(), Blocks.WHEAT.getDefaultState().with(Properties.AGE_7, random.nextInt(8)), false);
					}
				}
			}

		}
	}

	@Override
	public LootProvider getLootProvider() {
		return LootProviders.FARMLAND;
	}
}
