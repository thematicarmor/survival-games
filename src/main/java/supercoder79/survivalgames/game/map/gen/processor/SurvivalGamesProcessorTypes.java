package supercoder79.survivalgames.game.map.gen.processor;

import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SurvivalGamesProcessorTypes {
	public static StructureProcessorType<LootChestProcessor> LOOT;
	public static void init() {
		LOOT = Registry.register(Registry.STRUCTURE_PROCESSOR, new Identifier("survivalgames", "loot"), () -> LootChestProcessor.CODEC);
	}
}
