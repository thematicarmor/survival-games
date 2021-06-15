package supercoder79.survivalgames.game.map.biome.generator;

import com.mojang.serialization.Codec;
import supercoder79.survivalgames.game.map.biome.alpine.AlpineCliffsGen;
import supercoder79.survivalgames.game.map.biome.alpine.AlpineSlopedForestGen;
import supercoder79.survivalgames.game.map.biome.alpine.AlpsGen;
import supercoder79.survivalgames.game.map.biome.BiomeGen;

public class AlpsBiomeGenerator implements BiomeGenerator {
	public static final Codec<AlpsBiomeGenerator> CODEC = Codec.unit(new AlpsBiomeGenerator());

	@Override
	public BiomeGen getBiome(double temperature, double rainfall) {
		if (temperature < 0.65) {
			return temperature < 0.24 ? AlpsGen.INSTANCE : AlpineCliffsGen.INSTANCE;
		}
		return AlpineSlopedForestGen.INSTANCE;
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
