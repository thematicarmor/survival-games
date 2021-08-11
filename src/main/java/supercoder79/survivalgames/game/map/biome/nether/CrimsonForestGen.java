package supercoder79.survivalgames.game.map.biome.nether;

import java.util.Random;

import kdotjpg.opensimplex.OpenSimplexNoise;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.gen.BranchingTreeGen;
import xyz.nucleoid.substrate.gen.MapGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class CrimsonForestGen implements BiomeGen {
    public static final CrimsonForestGen INSTANCE = new CrimsonForestGen();
    public static final OpenSimplexNoise WART_NOISE = new OpenSimplexNoise(13);
    public static final OpenSimplexNoise LIGHT_NOISE = new OpenSimplexNoise(12);

    @Override
    public BlockState topState(Random random, int x, int z) {
        if (random.nextDouble() <= 0.1 + WART_NOISE.eval(x / 30.0, z / 30.0) * 0.1) {
            return Blocks.NETHER_WART_BLOCK.getDefaultState();
        }
        if (random.nextDouble() <= 0.05 + LIGHT_NOISE.eval(x / 40.0, z / 40.0) * 0.1) {
            return Blocks.SHROOMLIGHT.getDefaultState();
        }
        return Blocks.CRIMSON_NYLIUM.getDefaultState();
    }

    @Override
    public BlockState underState(Random random, int x, int z) {
        return Blocks.NETHERRACK.getDefaultState();
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
        return 16;
    }

    @Override
    public double upperLerpLow() {
        return 8;
    }

    @Override
    public double lowerLerpHigh() {
        return 12;
    }

    @Override
    public double lowerLerpLow() {
        return 4;
    }

    @Override
    public double detailFactor() {
        return 4.5;
    }

    @Override
    public MapGen tree(int x, int z, Random random) {
        return BranchingTreeGen.CRIMSON;
    }

    @Override
    public double modifyTreeChance(double original) {
        return 8;
    }

    @Override
    public RegistryKey<Biome> getFakingBiome() {
        return BiomeKeys.CRIMSON_FOREST;
    }

    @Override
    public int grassChance(int x, int z, Random random) {
        return 4;
    }
}
