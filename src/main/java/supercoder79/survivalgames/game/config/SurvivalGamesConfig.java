package supercoder79.survivalgames.game.config;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import supercoder79.survivalgames.game.map.biome.generator.BiomeGenerator;
import supercoder79.survivalgames.game.map.noise.NoiseGenerator;
import xyz.nucleoid.plasmid.game.config.PlayerConfig;

import net.minecraft.item.ItemStack;

public class SurvivalGamesConfig {
	public static final Codec<SurvivalGamesConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			WorldBorderConfig.CODEC.fieldOf("border").forGetter(config -> config.borderConfig),
			PlayerConfig.CODEC.fieldOf("players").forGetter(config -> config.playerConfig),
			Codec.INT.fieldOf("town_depth").forGetter(config -> config.townDepth),
			Codec.INT.fieldOf("outskirts_building_count").forGetter(config -> config.outskirtsBuildingCount),
			BiomeGenerator.CODEC.fieldOf("biome_generator").forGetter(config -> config.biomeGenerator),
			NoiseGenerator.CODEC.fieldOf("noise_generator").forGetter(config -> config.noiseGenerator),
			ItemStack.CODEC.listOf().fieldOf("kit").forGetter(config -> config.kit)
	).apply(instance, SurvivalGamesConfig::new));
	public final WorldBorderConfig borderConfig;
	public final PlayerConfig playerConfig;
	public final int townDepth;
	public final int outskirtsBuildingCount;
	public final BiomeGenerator biomeGenerator;
	public final NoiseGenerator noiseGenerator;
	public final List<ItemStack> kit;

	public SurvivalGamesConfig(WorldBorderConfig borderConfig, PlayerConfig playerConfig, int townDepth, int outskirtsBuildingCount, BiomeGenerator biomeGenerator, NoiseGenerator noiseGenerator, List<ItemStack> kit) {
		this.borderConfig = borderConfig;
		this.playerConfig = playerConfig;
		this.townDepth = townDepth;
		this.outskirtsBuildingCount = outskirtsBuildingCount;
		this.biomeGenerator = biomeGenerator;
		this.noiseGenerator = noiseGenerator;
		this.kit = kit;
	}
}
