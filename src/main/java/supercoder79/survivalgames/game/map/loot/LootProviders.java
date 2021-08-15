package supercoder79.survivalgames.game.map.loot;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class LootProviders {

	public static final LootProvider GENERIC = new LootProvider(new WeightedList<ItemStack>()
			.add(new ItemStack(Items.WOODEN_SWORD, 1), 4)
			.add(new ItemStack(Items.STONE_SWORD, 1), 16)
			.add(new ItemStack(Items.STONE_AXE, 1), 4)
			.add(new ItemStack(Items.SHIELD, 1), 3)
			.add(new ItemStack(Items.COBBLESTONE, 16), 10)
			.add(new ItemStack(Items.LEATHER_HELMET, 1), 2)
			.add(new ItemStack(Items.LEATHER_CHESTPLATE, 1), 2)
			.add(new ItemStack(Items.LEATHER_LEGGINGS, 1), 2)
			.add(new ItemStack(Items.LEATHER_BOOTS, 1), 2)
			.add(new ItemStack(Items.CHAINMAIL_HELMET, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_LEGGINGS, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_BOOTS, 1), 1)
			.add(new ItemStack(Items.EXPERIENCE_BOTTLE, 2), 2)
			.add(new ItemStack(Items.LAPIS_LAZULI, 1), 2)
			.add(new ItemStack(Items.ENDER_PEARL, 1), 1)
			.add(new ItemStack(Items.CROSSBOW, 1), 1)
			.add(new ItemStack(Items.BOW, 1), 2)
			.add(new ItemStack(Items.IRON_INGOT, 2), 1)
			.add(new ItemStack(Items.IRON_INGOT, 1), 2)
			.add(new ItemStack(Items.ARROW, 6), 4)
			.add(new ItemStack(Items.SNOWBALL, 4), 1),
			4, 6);

	public static final LootProvider HOUSE = new LootProvider(new WeightedList<ItemStack>()
			.add(new ItemStack(Items.STONE_SWORD, 1), 6)
			.add(new ItemStack(Items.IRON_SWORD, 1), 8)
			.add(new ItemStack(Items.SHIELD, 1), 8)
			.add(new ItemStack(Items.STONE_AXE, 1), 2)
			.add(new ItemStack(Items.GOLDEN_APPLE, 1), 1)
			.add(new ItemStack(Items.ENDER_PEARL, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_HELMET, 1), 3)
			.add(new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1), 3)
			.add(new ItemStack(Items.CHAINMAIL_LEGGINGS, 1), 3)
			.add(new ItemStack(Items.CHAINMAIL_BOOTS, 1), 3)
			.add(new ItemStack(Items.LEATHER_HELMET, 1), 2)
			.add(new ItemStack(Items.LEATHER_CHESTPLATE, 1), 2)
			.add(new ItemStack(Items.LEATHER_LEGGINGS, 1), 2)
			.add(new ItemStack(Items.LEATHER_BOOTS, 1), 2)
			.add(new ItemStack(Items.IRON_HELMET, 1), 1)
			.add(new ItemStack(Items.IRON_CHESTPLATE, 1), 1)
			.add(new ItemStack(Items.IRON_LEGGINGS, 1), 1)
			.add(new ItemStack(Items.IRON_BOOTS, 1), 1)
			.add(new ItemStack(Items.IRON_INGOT, 2), 1)
			.add(new ItemStack(Items.IRON_INGOT, 1), 2),
			4, 6);

	public static final LootProvider ENCHANTING_TABLE = new LootProvider(new WeightedList<ItemStack>()
			.add(new ItemStack(Items.STONE_SWORD, 1), 1)
			.add(new ItemStack(Items.IRON_SWORD, 1), 1)
			.add(new ItemStack(Items.SHIELD, 1), 1)
			.add(new ItemStack(Items.ENDER_PEARL, 1), 2)
			.add(new ItemStack(Items.EXPERIENCE_BOTTLE, 4), 4)
			.add(new ItemStack(Items.LAPIS_LAZULI, 2), 4)
			.add(new ItemStack(Items.CHAINMAIL_HELMET, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_LEGGINGS, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_BOOTS, 1), 1)
			.add(new ItemStack(Items.LEATHER_HELMET, 1), 1)
			.add(new ItemStack(Items.LEATHER_CHESTPLATE, 1), 1)
			.add(new ItemStack(Items.LEATHER_LEGGINGS, 1), 1)
			.add(new ItemStack(Items.LEATHER_BOOTS, 1), 1),
			4, 6);

	public static final LootProvider TOWER = new LootProvider(new WeightedList<ItemStack>()
			.add(new ItemStack(Items.STONE_SWORD, 1), 1)
			.add(new ItemStack(Items.IRON_SWORD, 1), 2)
			.add(new ItemStack(Items.STONE_AXE, 1), 2)
			.add(new ItemStack(Items.SHIELD, 1), 1)
			.add(new ItemStack(Items.BOW, 1), 3)
			.add(new ItemStack(Items.ARROW, 16), 4)
			.add(new ItemStack(Items.ENDER_PEARL, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_HELMET, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_LEGGINGS, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_BOOTS, 1), 1),
			4, 6);

	public static final LootProvider ORE_PILE = new LootProvider(new WeightedList<ItemStack>()
			.add(new ItemStack(Items.STONE_SWORD, 1), 6)
			.add(new ItemStack(Items.IRON_SWORD, 1), 12)
			.add(new ItemStack(Items.SHIELD, 1), 8)
			.add(new ItemStack(Items.STONE_AXE, 1), 4)
			.add(new ItemStack(Items.IRON_INGOT, 4), 8)
			.add(new ItemStack(Items.BUCKET, 1), 4)
			.add(new ItemStack(Items.DIAMOND, 1), 1)
			.add(new ItemStack(Items.GOLDEN_APPLE, 1), 1)
			.add(new ItemStack(Items.TNT, 4), 1)
			.add(new ItemStack(Items.FLINT_AND_STEEL, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_HELMET, 1), 2)
			.add(new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1), 2)
			.add(new ItemStack(Items.CHAINMAIL_LEGGINGS, 1), 2)
			.add(new ItemStack(Items.CHAINMAIL_BOOTS, 1), 2)
			.add(new ItemStack(Items.IRON_HELMET, 1), 1)
			.add(new ItemStack(Items.IRON_CHESTPLATE, 1), 1)
			.add(new ItemStack(Items.IRON_LEGGINGS, 1), 1)
			.add(new ItemStack(Items.IRON_BOOTS, 1), 1),
			4, 6);

	public static final LootProvider FARMLAND = new LootProvider(new WeightedList<ItemStack>()
			.add(new ItemStack(Items.STONE_SWORD, 1), 8)
			.add(new ItemStack(Items.IRON_SWORD, 1), 6)
			.add(new ItemStack(Items.SHIELD, 1), 6)
			.add(new ItemStack(Items.GOLDEN_APPLE, 1), 1)
			.add(new ItemStack(Items.BUCKET, 1), 3)
			.add(new ItemStack(Items.WATER_BUCKET, 1), 2)
			.add(new ItemStack(Items.NETHERITE_HOE, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_HELMET, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_LEGGINGS, 1), 1)
			.add(new ItemStack(Items.CHAINMAIL_BOOTS, 1), 1),
			4, 6);
}
