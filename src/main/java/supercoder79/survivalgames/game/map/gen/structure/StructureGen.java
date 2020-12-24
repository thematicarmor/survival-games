package supercoder79.survivalgames.game.map.gen.structure;

import java.util.Random;

import supercoder79.survivalgames.game.map.loot.LootProvider;
import xyz.nucleoid.substrate.gen.MapGen;

public interface StructureGen extends MapGen {
	int nearbyChestCount(Random random);

	LootProvider getLootProvider();
}
