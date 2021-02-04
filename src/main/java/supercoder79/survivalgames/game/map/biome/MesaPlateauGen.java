package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import kdotjpg.opensimplex.OpenSimplexNoise;
import supercoder79.survivalgames.game.map.gen.AcaciaTreeGen;
import xyz.nucleoid.plasmid.game.gen.MapGen;
import xyz.nucleoid.plasmid.game.gen.feature.ShrubGen;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class MesaPlateauGen implements BiomeGen {
	public static final MesaPlateauGen INSTANCE = new MesaPlateauGen();
	private static final OpenSimplexNoise RED_NOISE = new OpenSimplexNoise(32);

	@Override
	public BlockState topState(Random random, int x, int z) {
		return RED_NOISE.eval(x / 45.0, z / 45.0) > 0 ? Blocks.RED_TERRACOTTA.getDefaultState() : Blocks.TERRACOTTA.getDefaultState();
	}

	@Override
	public BlockState underState(Random random, int x, int z) {
		return RED_NOISE.eval(x / 45.0, z / 45.0) > 0 ? Blocks.LIGHT_GRAY_TERRACOTTA.getDefaultState() : Blocks.RED_TERRACOTTA.getDefaultState();
	}

	@Override
	public double upperNoiseFactor() {
		return 32;
	}

	@Override
	public double lowerNoiseFactor() {
		return 8;
	}

	@Override
	public double upperLerpHigh() {
		return 32;
	}

	@Override
	public double upperLerpLow() {
		return 24;
	}

	@Override
	public double lowerLerpHigh() {
		return 24;
	}

	@Override
	public double lowerLerpLow() {
		return 16;
	}

	@Override
	public double detailFactor() {
		return 4.5;
	}

	@Override
	public MapGen tree(int x, int z, Random random) {
		if (random.nextInt(2) == 0) {
			return AcaciaTreeGen.INSTANCE;
		}
		return ShrubGen.INSTANCE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return 0;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.DESERT_HILLS;
	}
}
