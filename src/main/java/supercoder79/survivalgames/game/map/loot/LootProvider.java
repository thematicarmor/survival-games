package supercoder79.survivalgames.game.map.loot;

import net.minecraft.item.ItemStack;

public class LootProvider {
	public final WeightedList<ItemStack> stacks;
	public final int minCount;
	public final int maxCount;

	public LootProvider(WeightedList<ItemStack> stacks, int minCount, int maxCount) {
		this.stacks = stacks;
		this.minCount = minCount;
		this.maxCount = maxCount;
	}
}
