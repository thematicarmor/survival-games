package supercoder79.survivalgames.game.map.gen.biome;

import java.util.Random;

import kdotjpg.opensimplex.OpenSimplexNoise;
import supercoder79.survivalgames.game.map.gen.feature.CactusGen;
import supercoder79.survivalgames.game.map.gen.feature.GrassGen;
import supercoder79.survivalgames.game.map.gen.feature.MapGen;
import supercoder79.survivalgames.game.map.gen.feature.NoGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class ScrublandGen implements BiomeGen {
	private OpenSimplexNoise desertNoise;
	private OpenSimplexNoise desertRoughNoise;

	@Override
	public void setupSeed(Random random) {
		desertNoise = new OpenSimplexNoise(random.nextLong());
		desertRoughNoise = new OpenSimplexNoise(random.nextLong());
	}

	@Override
	public double baseHeightFactorHigh() {
		return 22;
	}

	@Override
	public double baseHeightFactorLow() {
		return 2;
	}

	@Override
	public double lowerHeightFactorHigh() {
		return 6;
	}

	@Override
	public double lowerHeightFactorLow() {
		return 2;
	}

	@Override
	public double upperHeightFactorHigh() {
		return 6;
	}

	@Override
	public double upperHeightFactorLow() {
		return 2;
	}

	@Override
	public BlockState underState(int x, int z, Random random) {
		return noiseAt(x, z) > 0.0 ? Blocks.SANDSTONE.getDefaultState() : Blocks.DIRT.getDefaultState();
	}

	@Override
	public BlockState topState(int x, int z, Random random) {
		return noiseAt(x, z) > 0.0 ? Blocks.SAND.getDefaultState() : Blocks.GRASS_BLOCK.getDefaultState();
	}

	@Override
	public BlockState underwaterState(int x, int z, Random random) {
		return noiseAt(x, z) > 0.0 ? Blocks.SAND.getDefaultState() : Blocks.DIRT.getDefaultState();
	}

	@Override
	public MapGen treeAt(BlockPos pos, Random random) {
		return new NoGen();
	}

	@Override
	public MapGen grassAt(BlockPos pos, Random random) {
		return random.nextInt(32) == 0 ? new GrassGen(pos) : random.nextInt(32) == 0 ? new CactusGen(pos) : new NoGen();
	}

	private double noiseAt(int x, int z) {
		return (desertNoise.eval(x / 24.0, z / 24.0) * 0.6) + (desertRoughNoise.eval(x / 16.0, z / 16.0) * 0.4);
	}
}
