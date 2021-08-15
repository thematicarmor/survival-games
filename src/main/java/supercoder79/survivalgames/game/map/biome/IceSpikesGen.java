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

public class IceSpikesGen implements BiomeGen {
    public static final BiomeGen INSTANCE = new IceSpikesGen();

    @Override
    public RegistryKey<Biome> getFakingBiome() {
        return BiomeKeys.ICE_SPIKES;
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
        return Blocks.SNOW_BLOCK.getDefaultState();
    }

    @Override
    public BlockState underState(Random random, int x, int z) {
        return Blocks.SNOW_BLOCK.getDefaultState();
    }

    @Override
    public BlockState underWaterState(Random random, int x, int z) {
        return Blocks.STONE.getDefaultState();
    }

    @Override
    public MapGen tree(int x, int z, Random random) {
        return IceSpikesMapGen.INSTANCE;
    }

    @Override
    public double modifyTreeChance(double original) {
        return original * 2;
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
