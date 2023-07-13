package supercoder79.survivalgames.game.map.loot;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

public final class LootHelper {

	public static void placeProviderChest(WorldAccess world, BlockPos pos, LootProvider provider) {
		Random random = Random.create();

		world.setBlockState(pos, Blocks.CHEST.getDefaultState(), 3);
		ChestBlockEntity chest = (ChestBlockEntity) world.getBlockEntity(pos);

		if (chest != null) chest.setLootTable(provider.identifier, random.nextLong());
	}
}
