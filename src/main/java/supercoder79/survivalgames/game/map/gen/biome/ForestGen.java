package supercoder79.survivalgames.game.map.gen.biome;

import java.util.Random;

import kdotjpg.opensimplex.OpenSimplexNoise;
import supercoder79.survivalgames.game.map.gen.feature.tree.AspenTreeGen;
import supercoder79.survivalgames.game.map.gen.feature.GrassGen;
import supercoder79.survivalgames.game.map.gen.feature.MapGen;
import supercoder79.survivalgames.game.map.gen.feature.NoGen;
import supercoder79.survivalgames.game.map.gen.feature.tree.PoplarTreeGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class ForestGen implements BiomeGen {
	private OpenSimplexNoise treeDensity;
	private OpenSimplexNoise treeType;

	@Override
	public void setupSeed(Random random) {
		treeDensity = new OpenSimplexNoise(random.nextLong());
		treeType = new OpenSimplexNoise(random.nextLong());
	}

	@Override
	public double baseHeightFactorHigh() {
		return 18;
	}

	@Override
	public double baseHeightFactorLow() {
		return 12;
	}

	@Override
	public double lowerHeightFactorHigh() {
		return 10;
	}

	@Override
	public double lowerHeightFactorLow() {
		return 10;
	}

	@Override
	public double upperHeightFactorHigh() {
		return 10;
	}

	@Override
	public double upperHeightFactorLow() {
		return 10;
	}

	@Override
	public BlockState underState(int x, int z, Random random) {
		return Blocks.DIRT.getDefaultState();
	}

	@Override
	public BlockState topState(int x, int z, Random random) {
		return Blocks.GRASS_BLOCK.getDefaultState();
	}

	@Override
	public BlockState underwaterState(int x, int z, Random random) {
		return Blocks.DIRT.getDefaultState();
	}

	@Override
	public MapGen treeAt(BlockPos pos, Random random) {
		int x = pos.getX();
		int z = pos.getZ();
		if (random.nextInt(80 + (int) (treeDensity.eval(x / 45.0, z / 45.0) * 30)) == 0) {
			double typeNoise = treeType.eval(x / 120.0, z / 120.0) * 2.5;
			if (typeNoise > 1) {
				return new PoplarTreeGen(pos);
			} else if (typeNoise < 0) {
				return new AspenTreeGen(pos);
			} else {
				// Create tree gradient
				if (random.nextDouble() < typeNoise) {
					return new PoplarTreeGen(pos);
				} else {
					return new AspenTreeGen(pos);
				}
			}
		}

		return new NoGen();
	}

	@Override
	public MapGen grassAt(BlockPos pos, Random random) {
		return random.nextInt(14) == 0 ? new GrassGen(pos) : new NoGen();
	}
}
