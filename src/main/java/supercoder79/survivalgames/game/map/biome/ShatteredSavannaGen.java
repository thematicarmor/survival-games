package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import kdotjpg.opensimplex.OpenSimplexNoise;
import supercoder79.survivalgames.game.map.gen.BranchingTreeGen;
import xyz.nucleoid.substrate.gen.MapGen;
import xyz.nucleoid.substrate.gen.ShrubGen;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class ShatteredSavannaGen implements BiomeGen {
    public static final ShatteredSavannaGen INSTANCE = new ShatteredSavannaGen();
	private static final OpenSimplexNoise STONE_NOISE = new OpenSimplexNoise(88);

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
		return 32;
	}

	@Override
	public double upperLerpLow() {
		return 4;
	}

	@Override
	public double lowerLerpHigh() {
		return 24;
	}

	@Override
	public double lowerLerpLow() {
		return 4;
	}

	@Override
	public double detailFactor() {
		return 4.5;
	}

	@Override
	public MapGen tree(int x, int z, Random random) {
        if (random.nextInt(2) == 0) {
			return BranchingTreeGen.ACACIA;
        }

		return ShrubGen.INSTANCE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return original * 2.0;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.SHATTERED_SAVANNA;
	}
}
