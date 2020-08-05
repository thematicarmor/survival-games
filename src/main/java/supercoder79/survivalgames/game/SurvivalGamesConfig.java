package supercoder79.survivalgames.game;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.gegy1000.plasmid.game.config.GameConfig;
import net.gegy1000.plasmid.game.config.GameMapConfig;
import net.gegy1000.plasmid.game.config.PlayerConfig;

import net.minecraft.item.ItemStack;

public class SurvivalGamesConfig implements GameConfig {
	public static final Codec<SurvivalGamesConfig> CODEC = RecordCodecBuilder.create(instance -> {
		Codec<GameMapConfig<SurvivalGamesConfig>> mapCodec = GameMapConfig.codec();

		return instance.group(
				mapCodec.fieldOf("map").forGetter(config -> config.mapConfig),
				PlayerConfig.CODEC.fieldOf("players").forGetter(config -> config.playerConfig),
				ItemStack.CODEC.listOf().fieldOf("kit").forGetter(config -> config.kit)
		).apply(instance, SurvivalGamesConfig::new);
	});

	private final GameMapConfig<SurvivalGamesConfig> mapConfig;
	private final PlayerConfig playerConfig;
	private final List<ItemStack> kit;

	public SurvivalGamesConfig(GameMapConfig<SurvivalGamesConfig> mapConfig, PlayerConfig playerConfig, List<ItemStack> kit) {
		this.mapConfig = mapConfig;
		this.playerConfig = playerConfig;
		this.kit = kit;
	}

	public GameMapConfig<SurvivalGamesConfig> getMapConfig() {
		return mapConfig;
	}

	public PlayerConfig getPlayerConfig() {
		return playerConfig;
	}

	public List<ItemStack> getKit() {
		return kit;
	}
}
