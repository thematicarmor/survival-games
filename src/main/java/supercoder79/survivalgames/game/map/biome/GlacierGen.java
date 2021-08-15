package supercoder79.survivalgames.game.map.biome;

import kdotjpg.opensimplex.OpenSimplexNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import supercoder79.survivalgames.game.map.gen.IceSpikesMapGen;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public class GlacierGen implements BiomeGen {
    public static final BiomeGen INSTANCE = new GlacierGen();
    private static final OpenSimplexNoise ICE_NOISE = new OpenSimplexNoise();

    @Override
    public RegistryKey<Biome> getFakingBiome() {
        return BiomeKeys.FROZEN_RIVER;
    }

    @Override
    public double upperNoiseFactor() {
        return BiomeGen.super.upperNoiseFactor();
    }

    @Override
    public double lowerNoiseFactor() {
        return BiomeGen.super.lowerNoiseFactor();
    }

    @Override
    public double upperLerpHigh() {
        return BiomeGen.super.upperLerpHigh();
    }

    @Override
    public double upperLerpLow() {
        return BiomeGen.super.upperLerpLow();
    }

    @Override
    public double lowerLerpHigh() {
        return BiomeGen.super.lowerLerpHigh();
    }

    @Override
    public double lowerLerpLow() {
        return BiomeGen.super.lowerLerpLow();
    }

    @Override
    public double detailFactor() {
        return BiomeGen.super.detailFactor();
    }

    @Override
    public BlockState topState(Random random, int x, int z) {
        if (random.nextDouble() <= 0.1 + ICE_NOISE.eval(x / 30.0, z / 30.0) * 1.5) {
            return Blocks.PACKED_ICE.getDefaultState();
        }
        return Blocks.SNOW_BLOCK.getDefaultState();
    }

    @Override
    public BlockState underState(Random random, int x, int z) {
        return Blocks.STONE.getDefaultState();
    }

    @Override
    public BlockState underWaterState(Random random, int x, int z) {
        return BiomeGen.super.underWaterState(random, x, z);
    }

    @Override
    public MapGen tree(int x, int z, Random random) {
        return IceSpikesMapGen.INSTANCE;
    }

    @Override
    public double modifyTreeChance(double original) {
        return BiomeGen.super.modifyTreeChance(original);
    }

    @Override
    public int grassChance(int x, int z, Random random) {
        return 512;
    }

    @Override
    public MapGen grass(int x, int z, Random random) {
        return BiomeGen.super.grass(x, z, random);
    }
}
