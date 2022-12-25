package supercoder79.survivalgames.game.map.gen.structure;


import net.minecraft.util.math.random.Random;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import xyz.nucleoid.substrate.gen.MapGen;

public interface StructureGen extends MapGen {
	int nearbyChestCount(Random random);

	LootProvider getLootProvider();
}
