package supercoder79.survivalgames.game.map.loot;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class LootHelper {
	public static List<ItemStack> get(List<LootProviderEntry> entries) {
		Random random = new Random();
		WeightedList<WeightedList<ItemStack>> weights = new WeightedList<>();
		int maxCount = 0;
		int minCount = 0;
		int count = 0;

		// Add the stacks and values
		for (LootProviderEntry entry : entries) {
			weights.add(entry.provider.stacks, entry.count);
			maxCount += entry.provider.maxCount * entry.count;
			minCount += entry.provider.minCount * entry.count;
			count += entry.count;
		}

		// Normalize and get final count
		maxCount /= count;
		minCount /= count;
		int finalCount = random.nextInt(Math.max(maxCount - minCount, 1)) + minCount;

		// Get final stacks
		List<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < finalCount; i++) {
			stacks.add(weights.pickRandom(random).pickRandom(random).copy());
		}

		return stacks;
	}

	public static void placeProviderChest(WorldAccess world, BlockPos pos, LootProvider provider) {
		Random random = new Random();

		List<ItemStack> stacks = LootHelper.get(ImmutableList.of(new LootProviderEntry(provider, 96 * 96)));
		world.setBlockState(pos, Blocks.CHEST.getDefaultState(), 3);
		ChestBlockEntity chest = (ChestBlockEntity) world.getBlockEntity(pos);

		for (ItemStack stack : stacks) {
			chest.setStack(random.nextInt(27), stack);
		}
	}
}
