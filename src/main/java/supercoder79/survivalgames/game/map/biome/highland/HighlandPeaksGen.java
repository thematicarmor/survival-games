package supercoder79.survivalgames.game.map.biome.highland;

import kdotjpg.opensimplex.OpenSimplexNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.gen.ThistleGen;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public class HighlandPeaksGen implements BiomeGen {
    public static final BiomeGen INSTANCE = new HighlandPeaksGen();
    public final OpenSimplexNoise GRASS_NOISE = new OpenSimplexNoise(23);
    public final OpenSimplexNoise SNOW_NOISE = new OpenSimplexNoise(24);
    public final OpenSimplexNoise GRANITE_NOISE = new OpenSimplexNoise(25);

    @Override
    public RegistryKey<Biome> getFakingBiome() {
        return RegistryKey.of(Registry.BIOME_KEY, new Identifier("survivalgames", "highland"));
    }

    @Override
    public double upperNoiseFactor() {
        return 6;
    }

    @Override
    public double lowerNoiseFactor() {
        return 4;
    }

    @Override
    public double upperLerpHigh() {
        return 42;
    }

    @Override
    public double upperLerpLow() {
        return 36;
    }

    @Override
    public double lowerLerpHigh() {
        return 32;
    }

    @Override
    public double lowerLerpLow() {
        return 28;
    }

    @Override
    public double detailFactor() {
        return 4;
    }

    @Override
    public BlockState topState(Random random, int x, int z) {
        if (random.nextDouble() < GRASS_NOISE.eval(x / 45.0, z / 45.0) + 0.25) {
            return Blocks.GRASS_BLOCK.getDefaultState();
        } else if (random.nextDouble() < SNOW_NOISE.eval(x / 45.0, z / 45.0) + 0.2) {
            return Blocks.SNOW_BLOCK.getDefaultState();
        } else if (random.nextDouble() + 0.1 < GRANITE_NOISE.eval(x / 45.0, z / 45.0) / 12) {
            return Blocks.GRANITE.getDefaultState();
        }
        return Blocks.STONE.getDefaultState();
    }

    @Override
    public BlockState underState(Random random, int x, int z) {
        return Blocks.STONE.getDefaultState();
    }

    @Override
    public double modifyTreeChance(double original) {
        return 1;
    }

    @Override
    public int grassChance(int x, int z, Random random) {
        return 16;
    }

    @Override
    public MapGen grass(int x, int z, Random random) {
        return ThistleGen.INSTANCE;
    }
}
