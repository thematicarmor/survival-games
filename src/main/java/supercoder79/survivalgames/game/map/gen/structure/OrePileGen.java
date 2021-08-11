package supercoder79.survivalgames.game.map.gen.structure;

import java.util.Random;

import supercoder79.survivalgames.game.map.loot.LootHelper;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import supercoder79.survivalgames.game.map.loot.LootProviders;
import xyz.nucleoid.substrate.gen.GenHelper;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;

public class OrePileGen implements StructureGen {
	public static final StructureGen INSTANCE = new OrePileGen();

	private static final WeightedList<BlockState> STATES = new WeightedList<BlockState>()
			.add(Blocks.IRON_ORE.getDefaultState(), 1)
			.add(Blocks.COAL_ORE.getDefaultState(), 1);

	@Override
	public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
		pos = pos.down();

		world.setBlockState(pos, Blocks.STONE_BRICKS.getDefaultState(), 3);
		world.setBlockState(pos.up(), Blocks.LAVA.getDefaultState(), 3);
		for (Direction direction : GenHelper.HORIZONTALS) {
			world.setBlockState(pos.up().offset(direction), Blocks.STONE_BRICKS.getDefaultState(), 3);
		}

		boolean chestPlaced = false;

		int count = random.nextInt(6) + 4;
		for (int i = 0; i < count; i++) {
			int slX = random.nextInt(12) - random.nextInt(12);
			int slY = random.nextInt(3) - random.nextInt(3);
			int slZ = random.nextInt(12) - random.nextInt(12);
			BlockPos stackLocal = pos.add(slX, slY, slZ);
			BlockState state = STATES.shuffle().stream().findFirst().get();
			for (int j = 0; j < 40; j++) {
				int aX = random.nextInt(4) - random.nextInt(4);
				int aY = random.nextInt(6) - random.nextInt(6);
				int aZ = random.nextInt(4) - random.nextInt(4);
				BlockPos local = stackLocal.add(aX, aY, aZ);

				if (world.getBlockState(local.down()).isOpaque() && world.getBlockState(local).isAir() && !world.getBlockState(local.down()).isOf(Blocks.CHEST)) {
					if (!chestPlaced) {
						chestPlaced = true;

						LootHelper.placeProviderChest(world, local, LootProviders.ORE_PILE);
					} else {
						world.setBlockState(local, state, 3);
					}
				}
			}
		}
	}


	@Override
	public int nearbyChestCount(Random random) {
		return random.nextInt(2);
	}

	@Override
	public LootProvider getLootProvider() {
		return LootProviders.ORE_PILE;
	}
}
