package supercoder79.survivalgames.game.map.gen.biome;

import java.util.Random;

import supercoder79.survivalgames.game.map.gen.feature.MapGen;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public interface BiomeGen {
	void setupSeed(Random random);

	double baseHeightFactorHigh();

	double baseHeightFactorLow();

	double lowerHeightFactorHigh();

	double lowerHeightFactorLow();

	double upperHeightFactorHigh();

	double upperHeightFactorLow();

	BlockState underState(int x, int z, Random random);

	BlockState topState(int x, int z, Random random);

	BlockState underwaterState(int x, int z, Random random);

	MapGen treeAt(BlockPos pos, Random random);

	MapGen grassAt(BlockPos pos, Random random);
}
