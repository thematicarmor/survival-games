package supercoder79.survivalgames.game.map.gen.structure;

import supercoder79.survivalgames.game.map.gen.feature.MapGen;
import supercoder79.survivalgames.game.map.loot.LootProvider;

public interface StructureGen extends MapGen {
	LootProvider getLootProvider();
}
