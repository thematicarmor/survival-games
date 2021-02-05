package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import supercoder79.survivalgames.game.map.gen.BranchingTreeGen;
import xyz.nucleoid.plasmid.game.gen.MapGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class JungleHillsGen implements BiomeGen {
	public static final JungleHillsGen INSTANCE = new JungleHillsGen();

	@Override
	public BlockState topState(Random random, int x, int z) {
		return Blocks.GRASS_BLOCK.getDefaultState();
	}

	@Override
	public BlockState underState(Random random, int x, int z) {
		return Blocks.GRASS_BLOCK.getDefaultState();
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
		return 24;
	}

	@Override
	public double upperLerpLow() {
		return 16;
	}

	@Override
	public double lowerLerpHigh() {
		return 16;
	}

	@Override
	public double lowerLerpLow() {
		return 8;
	}

	@Override
	public double detailFactor() {
		return 4.5;
	}

	@Override
	public MapGen tree(int x, int z, Random random) {
		return BranchingTreeGen.JUNGLE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return 8;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.JUNGLE_HILLS;
	}

	@Override
	public int grassChance(int x, int z, Random random) {
		return 4;
	}
}
