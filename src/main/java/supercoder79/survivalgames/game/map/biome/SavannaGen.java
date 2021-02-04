package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import xyz.nucleoid.plasmid.game.gen.MapGen;
import xyz.nucleoid.plasmid.game.gen.feature.ShrubGen;
import xyz.nucleoid.plasmid.game.gen.feature.tree.PoplarTreeGen;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import supercoder79.survivalgames.game.map.gen.AcaciaTreeGen;

public class SavannaGen implements BiomeGen {
	public static final SavannaGen INSTANCE = new SavannaGen();
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
		if (random.nextInt(1) == 0) {
			return AcaciaTreeGen.INSTANCE;
		}

		return ShrubGen.INSTANCE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return 1000;
	}

	@Override
	public int grassChance(int x, int z, Random random) {
		return 12;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.SAVANNA;
	}
}

