package supercoder79.survivalgames.game.map.gen.structure;

import net.minecraft.util.collection.WeightedList;

public class Structures {
	public static final WeightedList<StructureGen> POOL = new WeightedList<StructureGen>()
			.add(HouseGen.INSTANCE, 1)
			.add(TowerGen.INSTANCE, 1)
			.add(OrePileGen.INSTANCE, 1)
			.add(FarmlandStructure.INSTANCE, 1)
			.add(EnchantingTableGen.INSTANCE, 1);
}
