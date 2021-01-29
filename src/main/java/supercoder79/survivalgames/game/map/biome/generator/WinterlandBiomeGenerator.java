package supercoder79.survivalgames.game.map.biome.generator;

import com.mojang.serialization.Codec;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.biome.TaigaGen;

public class WinterlandBiomeGenerator implements BiomeGenerator {
	public static final Codec<WinterlandBiomeGenerator> CODEC = Codec.unit(new WinterlandBiomeGenerator());

	@Override
	public BiomeGen getBiome(double temperature, double rainfall) {
		return TaigaGen.INSTANCE;
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
