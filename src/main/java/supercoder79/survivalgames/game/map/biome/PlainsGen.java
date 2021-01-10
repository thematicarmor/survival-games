package supercoder79.survivalgames.game.map.biome;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class PlainsGen implements BiomeGen {
	public static final PlainsGen INSTANCE = new PlainsGen();

	@Override
	public double upperNoiseFactor() {
		return 8;
	}

	@Override
	public double lowerNoiseFactor() {
		return 6;
	}

	@Override
	public double upperLerpHigh() {
		return 3;
	}

	@Override
	public double upperLerpLow() {
		return 2;
	}

	@Override
	public double lowerLerpHigh() {
		return 3;
	}

	@Override
	public double lowerLerpLow() {
		return 2;
	}

	@Override
	public double detailFactor() {
		return 1.25;
	}

	@Override
	public double modifyTreeCount(double original) {
		return 720;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.PLAINS;
	}
}
