package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import xyz.nucleoid.plasmid.game.gen.MapGen;
import xyz.nucleoid.plasmid.game.gen.feature.tree.PoplarTreeGen;
import xyz.nucleoid.substrate.biome.BaseBiomeGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public interface BiomeGen extends BaseBiomeGen {
	default double upperNoiseFactor() {
		return 14;
	}

	default double lowerNoiseFactor() {
		return 12;
	}

	default double detailFactor() {
		return 3.25;
	}

	default BlockState topState(Random random) {
		return Blocks.GRASS_BLOCK.getDefaultState();
	}

	default BlockState pathState() {
		return Blocks.GRASS_PATH.getDefaultState();
	}

	default BlockState underState() {
		return Blocks.DIRT.getDefaultState();
	}

	default BlockState underWaterState() {
		return Blocks.DIRT.getDefaultState();
	}

	default MapGen tree(int x, int z, Random random) {
		return PoplarTreeGen.INSTANCE;
	}

	default double modifyTreeCount(double original) {
		return original;
	}
}