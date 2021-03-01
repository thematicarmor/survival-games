package supercoder79.survivalgames.game.map.biome.generator;

import com.mojang.serialization.Codec;
import supercoder79.survivalgames.game.map.biome.AlpsGen;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.biome.TaigaGen;

public class AlpsBiomeGenerator implements BiomeGenerator {
	public static final Codec<AlpsBiomeGenerator> CODEC = Codec.unit(new AlpsBiomeGenerator());

	@Override
	public BiomeGen getBiome(double temperature, double rainfall) {
		return AlpsGen.INSTANCE;
	}

	@Override
	public boolean generateSnow() {
		return true;
	}

	@Override
	public Codec<? extends BiomeGenerator> getCodec() {
		return CODEC;
	}
}
