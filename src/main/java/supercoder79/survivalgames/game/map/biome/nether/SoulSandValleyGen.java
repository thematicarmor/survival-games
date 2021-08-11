package supercoder79.survivalgames.game.map.biome.nether;

import kdotjpg.opensimplex.OpenSimplexNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.gen.BranchingTreeGen;
import supercoder79.survivalgames.game.map.gen.FireGen;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public final class SoulSandValleyGen implements BiomeGen {
    public static final SoulSandValleyGen INSTANCE = new SoulSandValleyGen();
    private static final OpenSimplexNoise SAND_NOISE = new OpenSimplexNoise(23);

    @Override
    public BlockState topState(Random random, int x, int z) {
        if (random.nextDouble() <= 0.1 + SAND_NOISE.eval(x / 30.0, z / 30.0) * 1.5) {
            return Blocks.SOUL_SAND.getDefaultState();
        }

        return Blocks.SOUL_SOIL.getDefaultState();
    }

    @Override
    public BlockState underState(Random random, int x, int z) {
        return Blocks.SOUL_SOIL.getDefaultState();
    }

    @Override
    public double upperNoiseFactor() {
        return 12;
    }

    @Override
    public double lowerNoiseFactor() {
        return 8;
    }

    @Override
    public double upperLerpHigh() {
        return 10;
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
        return 2;
    }

    @Override
    public double detailFactor() {
        return 4.5;
    }

    @Override
    public MapGen tree(int x, int z, Random random) {
        if (random.nextInt(3) == 0) {
            return FireGen.INSTANCE;
        }
        return BranchingTreeGen.BONE;
    }

    @Override
    public double modifyTreeChance(double original) {
        return original * 1.5;
    }

    @Override
    public RegistryKey<Biome> getFakingBiome() {
        return BiomeKeys.SOUL_SAND_VALLEY;
    }

}
