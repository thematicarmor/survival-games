package supercoder79.survivalgames.game.map.biome.nether;

import kdotjpg.opensimplex.OpenSimplexNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.gen.BranchingTreeGen;
import supercoder79.survivalgames.game.map.gen.LavaHoleGen;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public class BasaltDeltasGen implements BiomeGen {
    public static final BasaltDeltasGen INSTANCE = new BasaltDeltasGen();
    public static final OpenSimplexNoise BLACKSTONE_NOISE = new OpenSimplexNoise(21);

    @Override
    public RegistryKey<Biome> getFakingBiome() {
        return BiomeKeys.BASALT_DELTAS;
    }

    @Override
    public double upperNoiseFactor() {
        return 20;
    }

    @Override
    public double lowerNoiseFactor() {
        return 12;
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
        return 24;
    }

    @Override
    public BlockState topState(Random random, int x, int z) {
        if (random.nextDouble() <= 0.2 + BLACKSTONE_NOISE.eval(x / 30.0, z / 30.0) * 0.1) {
            return Blocks.BLACKSTONE.getDefaultState();
        }

        return Blocks.BASALT.getDefaultState();
    }

    @Override
    public BlockState underState(Random random, int x, int z) {
        return Blocks.BASALT.getDefaultState();
    }

    @Override
    public BlockState underWaterState(Random random, int x, int z) {
        return Blocks.BLACKSTONE.getDefaultState();
    }

    @Override
    public MapGen tree(int x, int z, Random random) {
        if(random.nextInt(4) == 0) {
            return BranchingTreeGen.BASALT_COLUMN;
        }
        return LavaHoleGen.INSTANCE;
    }

    @Override
    public double modifyTreeChance(double original) {
        return 2;
    }

}
