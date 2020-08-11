package supercoder79.survivalgames;

import xyz.nucleoid.plasmid.game.GameType;
import supercoder79.survivalgames.game.config.SurvivalGamesConfig;
import supercoder79.survivalgames.game.SurvivalGamesWaiting;

import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;

public class SurvivalGames implements ModInitializer {

	@Override
	public void onInitialize() {
		GameType.register(
				new Identifier("survivalgames", "survivalgames"),
				SurvivalGamesWaiting::open,
				SurvivalGamesConfig.CODEC
		);
	}
}
