package supercoder79.survivalgames.game.map.biome.alpine;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.gen.GroundCoverGen;
import supercoder79.survivalgames.game.map.gen.TaigaTreeGen;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public class AlpineCliffsGen implements BiomeGen {
    public static final BiomeGen INSTANCE = new AlpineCliffsGen();

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
        return BiomeGen.super.grassChance(x, z, random) / 2;
    }

    @Override
    public MapGen grass(int x, int z, Random random) {
        return GroundCoverGen.SNOW;
    }

    @Override
    public RegistryKey<Biome> getFakingBiome() {
        return BiomeKeys.SNOWY_MOUNTAINS;
    }
}
