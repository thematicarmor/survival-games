package supercoder79.survivalgames.game.map.gen.biome;

import java.util.Random;

import kdotjpg.opensimplex.OpenSimplexNoise;
import supercoder79.survivalgames.game.map.gen.feature.CactusGen;
import supercoder79.survivalgames.game.map.gen.feature.GrassGen;
import supercoder79.survivalgames.game.map.gen.feature.MapGen;
import supercoder79.survivalgames.game.map.gen.feature.NoGen;
import supercoder79.survivalgames.game.map.gen.feature.PoplarTreeGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class ScrublandGen implements BiomeGen {
	private OpenSimplexNoise desertNoise;

	@Override
	public void setupSeed(Random random) {
		desertNoise = new OpenSimplexNoise(random.nextLong());
	}

	@Override
	public double baseHeightFactor() {
		return 22;
	}

	@Override
	public double lowerHeightFactor() {
		return 6;
	}

	@Override
	public double upperHeightFactor() {
		return 6;
	}

	@Override
	public BlockState underState(int x, int z, Random random) {
		return desertNoise.eval(x / 32.0, z / 32.0) > 0.0 ? Blocks.SANDSTONE.getDefaultState() : Blocks.DIRT.getDefaultState();
	}

	@Override
	public BlockState topState(int x, int z, Random random) {
		return desertNoise.eval(x / 32.0, z / 32.0) > 0.0 ? Blocks.SAND.getDefaultState() : Blocks.GRASS_BLOCK.getDefaultState();
	}

	@Override
	public BlockState underwaterState(int x, int z, Random random) {
		return desertNoise.eval(x / 32.0, z / 32.0) > 0.0 ? Blocks.SAND.getDefaultState() : Blocks.DIRT.getDefaultState();
	}

	@Override
	public MapGen treeAt(BlockPos pos, Random random) {
		return new NoGen();
	}

	@Override
	public MapGen grassAt(BlockPos pos, Random random) {
		return random.nextInt(32) == 0 ? new GrassGen(pos) : random.nextInt(18) == 0 ? new CactusGen(pos) : new NoGen();
	}
}
