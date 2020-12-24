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

public class HouseGen implements StructureGen {
	public static StructureGen INSTANCE = new HouseGen();

	private static final WeightedList<BlockState> STATES = new WeightedList<BlockState>()
			.add(Blocks.OAK_PLANKS.getDefaultState(), 40)
			.add(Blocks.COBWEB.getDefaultState(), 2)
			.add(Blocks.AIR.getDefaultState(), 1);

	@Override
	public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
		pos = pos.down();

		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				world.setBlockState(pos.add(x, 0, z), STATES.pickRandom(random), 3);
				world.setBlockState(pos.add(x, 4, z), STATES.pickRandom(random), 3);

				for (int y = 1; y <= 3; y++) {
					if (Math.abs(x) == 2 || Math.abs(z) == 2) {
						world.setBlockState(pos.add(x, y, z), STATES.pickRandom(random), 3);
					} else {
						world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState(), 3);
					}
				}
			}
		}

		//TODO: roof

		// Door
		world.setBlockState(pos.add(2, 1, 0), Blocks.AIR.getDefaultState(), 3);
		world.setBlockState(pos.add(2, 2, 0), Blocks.AIR.getDefaultState(), 3);

		LootHelper.placeProviderChest(world, pos.add(-1, 1, -1), LootProviders.HOUSE);
	}

	@Override
	public int nearbyChestCount(Random random) {
		return random.nextInt(2);
	}

	@Override
	public LootProvider getLootProvider() {
		return LootProviders.HOUSE;
	}
}
