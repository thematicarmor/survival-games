package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import supercoder79.survivalgames.game.map.gen.BranchingTreeGen;
import xyz.nucleoid.substrate.gen.MapGen;
import xyz.nucleoid.substrate.gen.ShrubGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class JungleGen implements BiomeGen {
    public static final JungleGen INSTANCE = new JungleGen();

	@Override
	public BlockState topState(Random random, int x, int z) {
		return Blocks.GRASS_BLOCK.getDefaultState();
	}

	@Override
	public BlockState underState(Random random, int x, int z) {
		return Blocks.DIRT.getDefaultState();
	}

	@Override
	public double upperLerpHigh() {
		return 6;
	}

	@Override
	public double upperLerpLow() {
		return 4;
	}

	@Override
	public double lowerLerpHigh() {
		return 6;
	}

	@Override
	public double lowerLerpLow() {
		return 4;
	}

	@Override
	public double detailFactor() {
		return 2.0;
	}

	@Override
	public MapGen tree(int x, int z, Random random) {
		if (random.nextInt(3) == 0) {
			return ShrubGen.INSTANCE;
		}

		return BranchingTreeGen.JUNGLE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return -32; // 96 - 32 = 64
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.JUNGLE;
	}

	@Override
	public int grassChance(int x, int z, Random random) {
		return 4;
	}
}
