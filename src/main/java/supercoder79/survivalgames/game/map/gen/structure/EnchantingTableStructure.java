package supercoder79.survivalgames.game.map.gen.structure;

import net.gegy1000.plasmid.game.map.GameMapBuilder;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import supercoder79.survivalgames.game.map.loot.LootProviders;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class EnchantingTableStructure implements StructureGen {
	private final BlockPos origin;

	public EnchantingTableStructure(BlockPos origin) {
		this.origin = origin;
	}

	@Override
	public void generate(GameMapBuilder builder) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				builder.setBlockState(origin.add(x, 0, z), Blocks.OAK_PLANKS.getDefaultState());
			}
		}

		builder.setBlockState(origin.up(), Blocks.ENCHANTING_TABLE.getDefaultState());
	}

	@Override
	public LootProvider getLootProvider() {
		return LootProviders.ENCHANTING_TABLE;
	}
}
