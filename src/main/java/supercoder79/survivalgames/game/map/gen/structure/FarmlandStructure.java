package supercoder79.survivalgames.game.map.gen.structure;

import net.minecraft.util.math.random.Random;

import supercoder79.survivalgames.game.map.loot.LootHelper;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import supercoder79.survivalgames.game.map.loot.LootProviders;
import xyz.nucleoid.substrate.gen.GenHelper;

import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;

public class FarmlandStructure implements StructureGen {
	public static StructureGen INSTANCE = new FarmlandStructure();

	@Override
	public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
		boolean chestPlaced = false;
		for (int i = 0; i < 196; i++) {
			int aX = random.nextInt(16) - random.nextInt(16);
			int aY = random.nextInt(4) - random.nextInt(4);
			int aZ = random.nextInt(16) - random.nextInt(16);

			BlockPos local = pos.add(aX, aY, aZ);
			if (world.getBlockState(local) == Blocks.GRASS_BLOCK.getDefaultState()) {
				boolean canSpawn = true;

				for (Direction direction : GenHelper.HORIZONTALS) {
					BlockPos dLocal = local.offset(direction);
					if (!world.getBlockState(dLocal).isOpaque()) {
						if (!world.getBlockState(dLocal).isOf(Blocks.WATER)) {
							canSpawn = false;
						}

						break;
					}
				}

				if (canSpawn) {
					if (!chestPlaced) {
						chestPlaced = true;
						LootHelper.placeProviderChest(world, local.up(), LootProviders.FARMLAND);
					} else {
						if (random.nextInt(3) == 0) {
							world.setBlockState(local, Blocks.WATER.getDefaultState(), 3);
						} else {
							world.setBlockState(local, Blocks.FARMLAND.getDefaultState().with(Properties.MOISTURE, 7), 3);
							world.setBlockState(local.up(), Blocks.WHEAT.getDefaultState().with(Properties.AGE_7, random.nextInt(8)), 3);
						}
					}
				}
			}

		}
	}

	@Override
	public int nearbyChestCount(Random random) {
		return 1 + random.nextInt(2);
	}

	@Override
	public LootProvider getLootProvider() {
		return LootProviders.FARMLAND;
	}
}
