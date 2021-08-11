package supercoder79.survivalgames.game.map.biome;

import kdotjpg.opensimplex.OpenSimplexNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import xyz.nucleoid.substrate.gen.CactusGen;
import xyz.nucleoid.substrate.gen.MapGen;
import xyz.nucleoid.substrate.gen.tree.DeadTreeGen;

import java.util.Random;

public final class DesertGen implements BiomeGen {
	public static final DesertGen INSTANCE = new DesertGen();
	private static final OpenSimplexNoise GRASS_NOISE = new OpenSimplexNoise(60);

	@Override
	public BlockState topState(Random random, int x, int z) {
		if (random.nextDouble() <= 0.1 + GRASS_NOISE.eval(x / 30.0, z / 30.0) * 0.1) {
			return Blocks.GRASS_BLOCK.getDefaultState();
		}

		return Blocks.SAND.getDefaultState();
	}

	@Override
	public BlockState underState(Random random, int x, int z) {
		return Blocks.SANDSTONE.getDefaultState();
	}

	@Override
	public BlockState underWaterState(Random random, int x, int z) {
		return Blocks.SAND.getDefaultState();
	}

	@Override
	public double upperLerpHigh() {
		return 6;
	}

	@Override
	public double upperLerpLow() {
		return 4;
	}

	@Override
	public double lowerLerpHigh() {
		return 6;
	}

	@Override
	public double lowerLerpLow() {
		return 4;
	}

	@Override
	public double detailFactor() {
		return 2.0;
	}

	@Override
	public MapGen tree(int x, int z, Random random) {
		return DeadTreeGen.INSTANCE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return 720;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.DESERT;
	}

	@Override
	public int grassChance(int x, int z, Random random) {
		return 48;
	}

	@Override
	public MapGen grass(int x, int z, Random random) {
		return CactusGen.INSTANCE;
	}
}
