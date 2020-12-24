package supercoder79.survivalgames.game.map.gen.structure;

import java.util.Random;

import supercoder79.survivalgames.game.map.loot.LootHelper;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import supercoder79.survivalgames.game.map.loot.LootProviders;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;

public class TowerGen implements StructureGen {
	public static final StructureGen INSTANCE = new TowerGen();

	private static final WeightedList<BlockState> STATES = new WeightedList<BlockState>()
			.add(Blocks.OAK_PLANKS.getDefaultState(), 24)
			.add(Blocks.COBWEB.getDefaultState(), 2)
			.add(Blocks.AIR.getDefaultState(), 1);

	@Override
	public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
		int height = random.nextInt(9) + 12;
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				world.setBlockState(pos.add(x, 0, z), STATES.pickRandom(random), 3);

				for (int y = 1; y <= height; y++) {
					if (Math.abs(x) == 1 || Math.abs(z) == 1) {
						if (z == 1) {
							// Make sure all the ladders have supporting blocks
							world.setBlockState(pos.add(x, y, z), Blocks.OAK_PLANKS.getDefaultState(), 3);
						} else {
							world.setBlockState(pos.add(x, y, z), STATES.pickRandom(random), 3);
						}
					} else {
						world.setBlockState(pos.add(x, y, z), Blocks.LADDER.getDefaultState(), 3);
					}
				}
			}
		}

		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				if (x == 0 && z == 0) {
					continue;
				}

				world.setBlockState(pos.add(x, height, z), STATES.pickRandom(random), 3);
				if (Math.abs(x) == 2 && Math.abs(z) == 2) {
					world.setBlockState(pos.add(x, height + 1, z), Blocks.OAK_FENCE.getDefaultState(), 3);
				}
			}
		}

		world.setBlockState(pos.add(0, 1, -1), Blocks.AIR.getDefaultState(), 3);
		world.setBlockState(pos.add(0, 2, -1), Blocks.AIR.getDefaultState(), 3);

		LootHelper.placeProviderChest(world, pos.add(0, height + 1, 2), LootProviders.TOWER);
	}

	@Override
	public int nearbyChestCount(Random random) {
		return random.nextInt(3);
	}

	@Override
	public LootProvider getLootProvider() {
		return LootProviders.TOWER;
	}
}
