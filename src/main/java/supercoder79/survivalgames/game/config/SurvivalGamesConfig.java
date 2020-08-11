package supercoder79.survivalgames.game.config;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import xyz.nucleoid.plasmid.game.config.GameConfig;
import xyz.nucleoid.plasmid.game.config.PlayerConfig;

import net.minecraft.item.ItemStack;

public class SurvivalGamesConfig implements GameConfig {
	public static final Codec<SurvivalGamesConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			WorldBorderConfig.CODEC.fieldOf("border").forGetter(config -> config.borderConfig),
			PlayerConfig.CODEC.fieldOf("players").forGetter(config -> config.playerConfig),
			ItemStack.CODEC.listOf().fieldOf("kit").forGetter(config -> config.kit)
	).apply(instance, SurvivalGamesConfig::new));
	public final WorldBorderConfig borderConfig;
	public final PlayerConfig playerConfig;
	public final List<ItemStack> kit;

	public SurvivalGamesConfig(WorldBorderConfig borderConfig, PlayerConfig playerConfig, List<ItemStack> kit) {
		this.borderConfig = borderConfig;
		this.playerConfig = playerConfig;
		this.kit = kit;
	}
}
