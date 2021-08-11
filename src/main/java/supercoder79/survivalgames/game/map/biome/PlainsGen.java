package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import xyz.nucleoid.substrate.gen.MapGen;
import xyz.nucleoid.substrate.gen.ShrubGen;
import xyz.nucleoid.substrate.gen.tree.PoplarTreeGen;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class PlainsGen implements BiomeGen {
	public static final PlainsGen INSTANCE = new PlainsGen();
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
	public MapGen tree(int x, int z, Random random) {
		if (random.nextInt(3) == 0) {
			return PoplarTreeGen.INSTANCE;
		}

		return ShrubGen.INSTANCE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return 600;
	}

	@Override
	public int grassChance(int x, int z, Random random) {
		return 12;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.PLAINS;
	}
}
