package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import kdotjpg.opensimplex.OpenSimplexNoise;
import supercoder79.survivalgames.game.map.gen.TaigaTreeGen;
import xyz.nucleoid.substrate.gen.MapGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class MountainGen implements BiomeGen {
	public static final MountainGen INSTANCE = new MountainGen();
	private static final OpenSimplexNoise STONE_NOISE = new OpenSimplexNoise(80);

	@Override
	public BlockState topState(Random random, int x, int z) {
		return STONE_NOISE.eval(x / 45.0, z / 45.0) > 0 ? Blocks.STONE.getDefaultState() : Blocks.GRASS_BLOCK.getDefaultState();
	}

	@Override
	public BlockState underState(Random random, int x, int z) {
		return STONE_NOISE.eval(x / 45.0, z / 45.0) > 0 ? Blocks.STONE.getDefaultState() : Blocks.DIRT.getDefaultState();
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
		return 24;
	}

	@Override
	public double upperLerpLow() {
		return 12;
	}

	@Override
	public double lowerLerpHigh() {
		return 18;
	}

	@Override
	public double lowerLerpLow() {
		return 9;
	}

	@Override
	public double detailFactor() {
		return 4.5;
	}

	@Override
	public MapGen tree(int x, int z, Random random) {
		return TaigaTreeGen.INSTANCE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return original * 3.0;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.MOUNTAINS;
	}
}
