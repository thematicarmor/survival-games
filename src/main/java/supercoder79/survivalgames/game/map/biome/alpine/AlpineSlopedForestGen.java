package supercoder79.survivalgames.game.map.biome.alpine;

import net.minecraft.block.BlockState;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.gen.GroundCoverGen;
import supercoder79.survivalgames.game.map.gen.TaigaTreeGen;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public class AlpineSlopedForestGen implements BiomeGen {
    public static BiomeGen INSTANCE = new AlpineSlopedForestGen();

    @Override
    public RegistryKey<Biome> getFakingBiome() {
        return BiomeKeys.SNOWY_TAIGA_HILLS;
    }

    @Override
    public double upperNoiseFactor() {
        return 5;
    }

    @Override
    public double lowerNoiseFactor() {
        return 2;
    }

    @Override
    public double upperLerpHigh() {
        return 12;
    }

    @Override
    public double upperLerpLow() {
        return 8;
    }

    @Override
    public double lowerLerpHigh() {
        return 8;
    }

    @Override
    public double lowerLerpLow() {
        return 6;
    }

    @Override
    public double detailFactor() {
        return BiomeGen.super.detailFactor();
    }

    @Override
    public BlockState topState(Random random, int x, int z) {
        return BiomeGen.super.topState(random, x, z);
    }

    @Override
    public BlockState underState(Random random, int x, int z) {
        return BiomeGen.super.underState(random, x, z);
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
        return 16;
    }

    @Override
    public int grassChance(int x, int z, Random random) {
        return (int) (BiomeGen.super.grassChance(x, z, random) / 1.5);
    }

    @Override
    public MapGen grass(int x, int z, Random random) {
        return GroundCoverGen.SNOW;
    }
}
