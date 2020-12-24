package supercoder79.survivalgames.game.map.gen.structure;

import java.util.Random;

import supercoder79.survivalgames.game.map.loot.LootHelper;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import supercoder79.survivalgames.game.map.loot.LootProviders;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;

public class EnchantingTableGen implements StructureGen {
	public static StructureGen INSTANCE = new EnchantingTableGen();

	@Override
	public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
		world.setBlockState(pos, Blocks.OAK_PLANKS.getDefaultState(), 3);
		world.setBlockState(pos.up(), Blocks.ENCHANTING_TABLE.getDefaultState(), 3);

		for(int x1 = -1; x1 <= 1; x1++) {
			for(int z1 = -1; z1 <= 1; z1++) {
				world.setBlockState(pos.add(x1, -1, z1), Blocks.OAK_PLANKS.getDefaultState(), 3);
			}
		}

		LootHelper.placeProviderChest(world, pos.south(), LootProviders.ENCHANTING_TABLE);
	}

	@Override
	public int nearbyChestCount(Random random) {
		return random.nextInt(2);
	}

	@Override
	public LootProvider getLootProvider() {
		return LootProviders.ENCHANTING_TABLE;
	}
}
