package supercoder79.survivalgames.game.map.biome.alpine;

import java.util.Random;

import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.gen.TaigaTreeGen;
import xyz.nucleoid.substrate.gen.MapGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class AlpsGen implements BiomeGen {
	public static final AlpsGen INSTANCE = new AlpsGen();

	@Override
	public BlockState topState(Random random, int x, int z) {
		return Blocks.STONE.getDefaultState();
	}

	@Override
	public BlockState underState(Random random, int x, int z) {
		return Blocks.STONE.getDefaultState();
	}

	@Override
	public BlockState underWaterState(Random random, int x, int z) {
		return Blocks.STONE.getDefaultState();
	}

	@Override
	public double upperNoiseFactor() {
		return 32;
	}

	@Override
	public double lowerNoiseFactor() {
		return -32;
	}

	@Override
	public double upperLerpHigh() {
		return 24;
	}

	@Override
	public double upperLerpLow() {
		return 18;
	}

	@Override
	public double lowerLerpHigh() {
		return 20;
	}

	@Override
	public double lowerLerpLow() {
		return 16;
	}

	@Override
	public double detailFactor() {
		return 5;
	}

	@Override
	public MapGen tree(int x, int z, Random random) {
		return TaigaTreeGen.INSTANCE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return 512;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.SNOWY_TUNDRA;
	}
}
