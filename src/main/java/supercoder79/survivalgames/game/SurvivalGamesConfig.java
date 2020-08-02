package supercoder79.survivalgames.game;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.gegy1000.plasmid.game.config.GameConfig;
import net.gegy1000.plasmid.game.config.GameMapConfig;
import net.gegy1000.plasmid.game.config.PlayerConfig;

public class SurvivalGamesConfig implements GameConfig {
	public static final Codec<SurvivalGamesConfig> CODEC = RecordCodecBuilder.create(instance -> {
		Codec<GameMapConfig<SurvivalGamesConfig>> mapCodec = GameMapConfig.codec();

		return instance.group(
				mapCodec.fieldOf("map").forGetter(config -> config.mapConfig),
				PlayerConfig.CODEC.fieldOf("players").forGetter(config -> config.playerConfig)
		).apply(instance, SurvivalGamesConfig::new);
	});

	private final GameMapConfig<SurvivalGamesConfig> mapConfig;
	private final PlayerConfig playerConfig;

	public SurvivalGamesConfig(GameMapConfig<SurvivalGamesConfig> mapConfig, PlayerConfig playerConfig) {
		this.mapConfig = mapConfig;
		this.playerConfig = playerConfig;
	}

	public GameMapConfig<SurvivalGamesConfig> getMapConfig() {
		return mapConfig;
	}

	public PlayerConfig getPlayerConfig() {
		return playerConfig;
	}
}
