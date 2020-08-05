package supercoder79.survivalgames.game.map.gen.structure;

import java.util.Random;

import net.gegy1000.plasmid.game.map.GameMapBuilder;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import supercoder79.survivalgames.game.map.loot.LootProviders;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.math.BlockPos;

public class TowerStructure implements StructureGen {
	private static final WeightedList<BlockState> STATES = new WeightedList<BlockState>()
			.add(Blocks.OAK_PLANKS.getDefaultState(), 40)
			.add(Blocks.COBWEB.getDefaultState(), 2)
			.add(Blocks.AIR.getDefaultState(), 1);

	private final BlockPos origin;

	public TowerStructure(BlockPos origin) {
		this.origin = origin;
	}

	@Override
	public void generate(GameMapBuilder builder) {
		Random random = new Random();

		int height = random.nextInt(5) + 8;
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				builder.setBlockState(origin.add(x, 0, z), STATES.pickRandom(random));

				for (int y = 1; y <= height; y++) {
					if (Math.abs(x) == 1 || Math.abs(z) == 1) {
						if (z == 1) {
							// Make sure all the ladders have supporting blocks
							builder.setBlockState(origin.add(x, y, z), Blocks.OAK_PLANKS.getDefaultState());
						} else {
							builder.setBlockState(origin.add(x, y, z), STATES.pickRandom(random));
						}
					} else {
						builder.setBlockState(origin.add(x, y, z), Blocks.LADDER.getDefaultState());
					}
				}
			}
		}

		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				if (x == 0 && z == 0) {
					continue;
				}

				builder.setBlockState(origin.add(x, height, z), STATES.pickRandom(random));
				if (Math.abs(x) == 2 && Math.abs(z) == 2) {
					builder.setBlockState(origin.add(x, height + 1, z), Blocks.OAK_FENCE.getDefaultState());
				}
			}
		}

		builder.setBlockState(origin.add(0, 1, -1), Blocks.AIR.getDefaultState());
		builder.setBlockState(origin.add(0, 2, -1), Blocks.AIR.getDefaultState());
	}

	@Override
	public LootProvider getLootProvider() {
		return LootProviders.TOWER;
	}
}
