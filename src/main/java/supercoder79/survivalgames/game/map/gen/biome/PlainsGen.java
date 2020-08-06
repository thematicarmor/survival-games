package supercoder79.survivalgames.game.map.gen.biome;

import java.util.Random;

import supercoder79.survivalgames.game.map.gen.feature.GrassGen;
import supercoder79.survivalgames.game.map.gen.feature.MapGen;
import supercoder79.survivalgames.game.map.gen.feature.NoGen;
import supercoder79.survivalgames.game.map.gen.feature.PoplarTreeGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class PlainsGen implements BiomeGen {
	@Override
	public void setupSeed(Random random) {

	}

	@Override
	public double baseHeightFactor() {
		return 18;
	}

	@Override
	public double lowerHeightFactor() {
		return 10;
	}

	@Override
	public double upperHeightFactor() {
		return 16;
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
		return random.nextInt(1000) == 0 ? new PoplarTreeGen(pos) : new NoGen();
	}

	@Override
	public MapGen grassAt(BlockPos pos, Random random) {
		return random.nextInt(8) == 0 ? new GrassGen(pos) : new NoGen();
	}
}
