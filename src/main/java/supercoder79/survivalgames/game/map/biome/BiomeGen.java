package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import xyz.nucleoid.substrate.gen.MapGen;
import xyz.nucleoid.substrate.gen.GrassGen;
import xyz.nucleoid.substrate.biome.BaseBiomeGen;
import xyz.nucleoid.substrate.gen.tree.PoplarTreeGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public interface BiomeGen extends BaseBiomeGen {
	default double upperNoiseFactor() {
		return 14;
	}

	default double lowerNoiseFactor() {
		return 12;
	}

	default double upperLerpHigh() {
		return 12;
	}

	default double upperLerpLow() {
		return 8;
	}

	default double lowerLerpHigh() {
		return 8;
	}

	default double lowerLerpLow() {
		return 6;
	}

	default double detailFactor() {
		return 3.25;
	}

	default BlockState topState(Random random, int x, int z) {
		return Blocks.GRASS_BLOCK.getDefaultState();
	}

	default BlockState underState(Random random, int x, int z) {
		return Blocks.DIRT.getDefaultState();
	}

	default BlockState underWaterState(Random random, int x, int z) {
		return underState(random, x, z);
	}

	default MapGen tree(int x, int z, Random random) {
		return PoplarTreeGen.INSTANCE;
	}

	default double modifyTreeChance(double original) {
		return original;
	}

	default int grassChance(int x, int z, Random random) {
		return 16;
	}

	default MapGen grass(int x, int z, Random random) {
		return GrassGen.INSTANCE;
	}
}