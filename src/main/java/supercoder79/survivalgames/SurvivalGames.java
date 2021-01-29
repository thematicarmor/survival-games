package supercoder79.survivalgames;

import supercoder79.survivalgames.game.map.biome.generator.BiomeGenerators;
import supercoder79.survivalgames.game.map.gen.processor.SurvivalGamesProcessorTypes;
import xyz.nucleoid.plasmid.game.GameType;
import supercoder79.survivalgames.game.config.SurvivalGamesConfig;
import supercoder79.survivalgames.game.SurvivalGamesWaiting;

import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;

public class SurvivalGames implements ModInitializer {

	@Override
	public void onInitialize() {
		SurvivalGamesProcessorTypes.init();
		BiomeGenerators.init();

		GameType.register(
				new Identifier("survivalgames", "survivalgames"),
				SurvivalGamesWaiting::open,
				SurvivalGamesConfig.CODEC
		);
	}
}
