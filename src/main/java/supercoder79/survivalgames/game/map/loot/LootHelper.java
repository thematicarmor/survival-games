package supercoder79.survivalgames.game.map.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.WeightedList;

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
			minCount += entry.provider.minCount  * entry.count;
			count += entry.count;
		}

		// Normalize and get final count
		maxCount /= count;
		minCount /= count;
		int finalCount = random.nextInt(Math.max(maxCount - minCount, 0)) + minCount;

		// Get final stacks
		List<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < finalCount; i++) {
			stacks.add(weights.pickRandom(random).pickRandom(random));
		}

		return stacks;
	}
}
