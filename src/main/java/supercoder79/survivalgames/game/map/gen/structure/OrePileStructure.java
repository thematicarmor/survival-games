package supercoder79.survivalgames.game.map.gen.structure;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import net.gegy1000.plasmid.game.map.GameMapBuilder;
import supercoder79.survivalgames.game.map.gen.GenHelper;
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
import net.minecraft.util.math.Direction;

public class OrePileStructure implements StructureGen {
	private static final WeightedList<BlockState> STATES = new WeightedList<BlockState>()
			.add(Blocks.IRON_ORE.getDefaultState(), 1)
			.add(Blocks.COAL_ORE.getDefaultState(), 1);
	private final BlockPos origin;

	public OrePileStructure(BlockPos origin) {
		this.origin = origin;
	}

	@Override
	public void generate(GameMapBuilder builder) {
		builder.setBlockState(origin, Blocks.STONE_BRICKS.getDefaultState(), false);
		builder.setBlockState(origin.up(), Blocks.LAVA.getDefaultState(), false);
		for (Direction direction : GenHelper.HORIZONTALS) {
			builder.setBlockState(origin.up().offset(direction), Blocks.STONE_BRICKS.getDefaultState(), false);
		}

		boolean chestPlaced = false;

		Random random = new Random();
		int count = random.nextInt(6) + 3;
		for (int i = 0; i < count; i++) {
			int slX = random.nextInt(12) - random.nextInt(12);
			int slY = random.nextInt(3) - random.nextInt(3);
			int slZ = random.nextInt(12) - random.nextInt(12);
			BlockPos stackLocal = origin.add(slX, slY, slZ);
			BlockState state = STATES.pickRandom(random);
			for (int j = 0; j < 40; j++) {
				int aX = random.nextInt(4) - random.nextInt(4);
				int aY = random.nextInt(6) - random.nextInt(6);
				int aZ = random.nextInt(4) - random.nextInt(4);
				BlockPos local = stackLocal.add(aX, aY, aZ);

				if (builder.getBlockState(local.down()).isOpaque() && builder.getBlockState(local).isAir() && !builder.getBlockState(local.down()).isOf(Blocks.CHEST)) {
					if (!chestPlaced) {
						chestPlaced = true;

						LootHelper.placeProviderChest(builder, local, LootProviders.ORE_PILE);
					} else {
						builder.setBlockState(local, state, false);
					}
				}
			}
		}
	}

	@Override
	public LootProvider getLootProvider() {
		return LootProviders.ORE_PILE;
	}
}
