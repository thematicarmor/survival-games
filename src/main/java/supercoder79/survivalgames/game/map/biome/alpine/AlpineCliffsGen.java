package supercoder79.survivalgames.game.map.biome.alpine;

import kdotjpg.opensimplex.OpenSimplexNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.gen.GroundCoverGen;
import supercoder79.survivalgames.game.map.gen.TaigaTreeGen;
import xyz.nucleoid.substrate.gen.GrassGen;
import xyz.nucleoid.substrate.gen.MapGen;

import net.minecraft.util.math.random.Random;

public class AlpineCliffsGen implements BiomeGen {
    public static final BiomeGen INSTANCE = new AlpineCliffsGen();
    private static final OpenSimplexNoise POWDER_SNOW_NOISE = new OpenSimplexNoise();

    @Override
    public double upperNoiseFactor() {
        return 12;
    }

    @Override
    public double lowerNoiseFactor() {
        return 4;
    }

    @Override
    public double upperLerpHigh() {
        return 26;
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
        return 8;
    }

    @Override
    public BlockState topState(Random random, int x, int z) {
        if (POWDER_SNOW_NOISE.eval(x / 16.0, z / 16.0) <= 0.25 + (random.nextDouble() * 0.05)) {
            return Blocks.POWDER_SNOW.getDefaultState();
        }
        return Blocks.GRASS_BLOCK.getDefaultState();
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
        return TaigaTreeGen.INSTANCE;
    }

    @Override
    public double modifyTreeChance(double original) {
        return 512;
    }

    @Override
    public int grassChance(int x, int z, Random random) {
        return 512;
    }

    @Override
    public MapGen grass(int x, int z, Random random) {
        return GrassGen.INSTANCE;
    }

    @Override
    public RegistryKey<Biome> getFakingBiome() {
        return BiomeKeys.SNOWY_SLOPES;
    }
}