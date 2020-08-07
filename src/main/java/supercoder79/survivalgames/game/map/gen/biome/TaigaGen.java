package supercoder79.survivalgames.game.map.gen.biome;

import java.util.Random;

import supercoder79.survivalgames.game.map.gen.feature.GrassGen;
import supercoder79.survivalgames.game.map.gen.feature.MapGen;
import supercoder79.survivalgames.game.map.gen.feature.NoGen;
import supercoder79.survivalgames.game.map.gen.feature.tree.SpruceTreeGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class TaigaGen implements BiomeGen {
	@Override
	public void setupSeed(Random random) {

	}

	@Override
	public double baseHeightFactorHigh() {
		return 12;
	}

	@Override
	public double baseHeightFactorLow() {
		return 4;
	}

	@Override
	public double lowerHeightFactorHigh() {
		return 8;
	}

	@Override
	public double lowerHeightFactorLow() {
		return 8;
	}

	@Override
	public double upperHeightFactorHigh() {
		return 8;
	}

	@Override
	public double upperHeightFactorLow() {
		return 8;
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
		return random.nextInt(80) == 0 ? new SpruceTreeGen(pos) : new NoGen();
	}

	@Override
	public MapGen grassAt(BlockPos pos, Random random) {
		return random.nextInt(18) == 0 ? new GrassGen(pos) : new NoGen();
	}
}
