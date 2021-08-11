package supercoder79.survivalgames.game.map.biome.nether;

import kdotjpg.opensimplex.OpenSimplexNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.gen.FireGen;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public class NetherWastesGen implements BiomeGen {
    public static final NetherWastesGen INSTANCE = new NetherWastesGen();
    public static final OpenSimplexNoise GOLD_NOISE = new OpenSimplexNoise(14);

    @Override
    public RegistryKey<Biome> getFakingBiome() {
        return BiomeKeys.NETHER_WASTES;
    }

    @Override
    public double upperNoiseFactor() {
        return 1;
    }

    @Override
    public double lowerNoiseFactor() {
        return 1;
    }

    @Override
    public double upperLerpHigh() {
        return 8;
    }

    @Override
    public double upperLerpLow() {
        return 6;
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
        return 4;
    }

    @Override
    public BlockState topState(Random random, int x, int z) {
        if (random.nextDouble() <= 0.1 + GOLD_NOISE.eval(x / 30.0, z / 30.0) * 0.1) {
            return Blocks.NETHER_GOLD_ORE.getDefaultState();
        }
        return Blocks.NETHERRACK.getDefaultState();
    }

    @Override
    public BlockState underState(Random random, int x, int z) {
        return Blocks.NETHERRACK.getDefaultState();
    }

    @Override
    public BlockState underWaterState(Random random, int x, int z) {
        return Blocks.NETHERRACK.getDefaultState();
    }

    @Override
    public double modifyTreeChance(double original) {
        return 8;
    }

    @Override
    public MapGen tree(int x, int z, Random random) {
        if (random.nextInt(3) == 0) {
            return PiglinGen.INSTANCE;
        }
        return FireGen.INSTANCE;
    }
}
