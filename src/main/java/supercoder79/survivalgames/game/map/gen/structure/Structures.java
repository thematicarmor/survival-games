package supercoder79.survivalgames.game.map.gen.structure;

import xyz.nucleoid.substrate.gen.MapGen;

import net.minecraft.util.collection.WeightedList;

public class Structures {
	public static final WeightedList<MapGen> POOL = new WeightedList<MapGen>()
			.add(HouseStructure.INSTANCE, 1)
			.add(TowerStructure.INSTANCE, 1)
			.add(OrePileStructure.INSTANCE, 1)
			.add(FarmlandStructure.INSTANCE, 1)
			.add(EnchantingTableStructure.INSTANCE, 1);
}
