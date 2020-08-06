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

public class HouseStructure implements StructureGen {
	private static final WeightedList<BlockState> STATES = new WeightedList<BlockState>()
			.add(Blocks.OAK_PLANKS.getDefaultState(), 40)
			.add(Blocks.COBWEB.getDefaultState(), 2)
			.add(Blocks.AIR.getDefaultState(), 1);
	private final BlockPos origin;

	public HouseStructure(BlockPos origin) {
		this.origin = origin;
	}

	@Override
	public void generate(GameMapBuilder builder) {
		Random random = new Random();

		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				builder.setBlockState(origin.add(x, 0, z), STATES.pickRandom(random), false);
				builder.setBlockState(origin.add(x, 4, z), STATES.pickRandom(random), false);

				for (int y = 1; y <= 3; y++) {
					if (Math.abs(x) == 2 || Math.abs(z) == 2) {
						builder.setBlockState(origin.add(x, y, z), STATES.pickRandom(random), false);
					} else {
						builder.setBlockState(origin.add(x, y, z), Blocks.AIR.getDefaultState(), false);
					}
				}
			}
		}

		//TODO: roof

		// Door
		builder.setBlockState(origin.add(2, 1, 0), Blocks.AIR.getDefaultState(), false);
		builder.setBlockState(origin.add(2, 2, 0), Blocks.AIR.getDefaultState(), false);

		LootHelper.placeProviderChest(builder, origin.add(-1, 1, -1), LootProviders.HOUSE);
	}

	@Override
	public LootProvider getLootProvider() {
		return LootProviders.HOUSE;
	}
}
