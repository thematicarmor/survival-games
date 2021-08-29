package supercoder79.survivalgames.game.map.gen.structure;

import net.minecraft.util.collection.WeightedList;

public class Structures {
	public static final WeightedList<StructureGen> POOL = new WeightedList<StructureGen>()
			.add(OrePileGen.INSTANCE, 2)
			.add(FarmlandStructure.INSTANCE, 2)
			.add(MeteorStructure.INSTANCE, 1)
			.add(SpawnerStructure.INSTANCE, 1);
}
