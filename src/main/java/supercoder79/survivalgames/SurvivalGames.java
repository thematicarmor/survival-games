package supercoder79.survivalgames;

import xyz.nucleoid.plasmid.game.GameType;
import supercoder79.survivalgames.game.SurvivalGamesConfig;
import supercoder79.survivalgames.game.SurvivalGamesWaiting;

import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;

public class SurvivalGames implements ModInitializer {
	public static final GameType<SurvivalGamesConfig> TYPE = GameType.register(
			new Identifier("survivalgames", "survivalgames"),
			SurvivalGamesWaiting::open,
			SurvivalGamesConfig.CODEC
	);

	@Override
	public void onInitialize() {

	}
}
