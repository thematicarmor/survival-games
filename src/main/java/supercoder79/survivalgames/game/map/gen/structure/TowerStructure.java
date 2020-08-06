package supercoder79.survivalgames.game.map.gen.structure;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import net.gegy1000.plasmid.game.map.GameMapBuilder;
import supercoder79.survivalgames.game.map.loot.LootHelper;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import supercoder79.survivalgames.game.map.loot.LootProviderEntry;
import supercoder79.survivalgames.game.map.loot.LootProviders;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.math.BlockPos;

public class TowerStructure implements StructureGen {
	private static final WeightedList<BlockState> STATES = new WeightedList<BlockState>()
			.add(Blocks.OAK_PLANKS.getDefaultState(), 32)
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
				builder.setBlockState(origin.add(x, 0, z), STATES.pickRandom(random), false);

				for (int y = 1; y <= height; y++) {
					if (Math.abs(x) == 1 || Math.abs(z) == 1) {
						if (z == 1) {
							// Make sure all the ladders have supporting blocks
							builder.setBlockState(origin.add(x, y, z), Blocks.OAK_PLANKS.getDefaultState(), false);
						} else {
							builder.setBlockState(origin.add(x, y, z), STATES.pickRandom(random), false);
						}
					} else {
						builder.setBlockState(origin.add(x, y, z), Blocks.LADDER.getDefaultState(), false);
					}
				}
			}
		}

		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				if (x == 0 && z == 0) {
					continue;
				}

				builder.setBlockState(origin.add(x, height, z), STATES.pickRandom(random), false);
				if (Math.abs(x) == 2 && Math.abs(z) == 2) {
					builder.setBlockState(origin.add(x, height + 1, z), Blocks.OAK_FENCE.getDefaultState(), false);
				}
			}
		}

		builder.setBlockState(origin.add(0, 1, -1), Blocks.AIR.getDefaultState(), false);
		builder.setBlockState(origin.add(0, 2, -1), Blocks.AIR.getDefaultState(), false);

		List<ItemStack> stacks = LootHelper.get(ImmutableList.of(new LootProviderEntry(LootProviders.TOWER, 96 * 96)));
		builder.setBlockState(origin.add(0, height + 1, 2), Blocks.CHEST.getDefaultState());
		ChestBlockEntity be = (ChestBlockEntity) builder.getBlockEntity(origin.add(0, height + 1, 2));

		for (ItemStack stack : stacks) {
			be.setStack(random.nextInt(27), stack);
		}
	}

	@Override
	public LootProvider getLootProvider() {
		return LootProviders.TOWER;
	}
}
