package supercoder79.survivalgames.game;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.gegy1000.plasmid.game.config.GameConfig;
import net.gegy1000.plasmid.game.config.PlayerConfig;

import net.minecraft.item.ItemStack;

public class SurvivalGamesConfig implements GameConfig {
	public static final Codec<SurvivalGamesConfig> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				PlayerConfig.CODEC.fieldOf("players").forGetter(config -> config.playerConfig),
				ItemStack.CODEC.listOf().fieldOf("kit").forGetter(config -> config.kit)
		).apply(instance, SurvivalGamesConfig::new);
	});
	public final PlayerConfig playerConfig;
	public final List<ItemStack> kit;

	public SurvivalGamesConfig(PlayerConfig playerConfig, List<ItemStack> kit) {
		this.playerConfig = playerConfig;
		this.kit = kit;
	}
}
