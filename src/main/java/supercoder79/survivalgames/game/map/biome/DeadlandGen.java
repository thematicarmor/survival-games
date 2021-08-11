package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import xyz.nucleoid.substrate.gen.MapGen;
import xyz.nucleoid.substrate.gen.ShrubGen;
import xyz.nucleoid.substrate.gen.tree.DeadTreeGen;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class DeadlandGen implements BiomeGen {
	public static final DeadlandGen INSTANCE = new DeadlandGen();

	@Override
	public double upperLerpHigh() {
		return 4;
	}

	@Override
	public double upperLerpLow() {
		return 3;
	}

	@Override
	public double lowerLerpHigh() {
		return 4;
	}

	@Override
	public double lowerLerpLow() {
		return 3;
	}

	@Override
	public double detailFactor() {
		return 1.75;
	}

	@Override
	public MapGen tree(int x, int z, Random random) {
		if (random.nextInt(4) == 0) {
			return DeadTreeGen.INSTANCE;
		}

		return ShrubGen.INSTANCE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return original * 2.5;
	}

	@Override
	public int grassChance(int x, int z, Random random) {
		return 32;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.SAVANNA;
	}
}
