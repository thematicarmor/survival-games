package supercoder79.survivalgames.game.map.biome.generator;

import com.mojang.serialization.Codec;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.biome.highland.HighlandHillsGen;
import supercoder79.survivalgames.game.map.biome.highland.HighlandPeaksGen;
import supercoder79.survivalgames.game.map.biome.highland.HighlandPlainsGen;
import supercoder79.survivalgames.game.map.biome.highland.HighlandSpringGen;

public class HighlandBiomeGenerator implements BiomeGenerator {

    public static final Codec<HighlandBiomeGenerator> CODEC = Codec.unit(new HighlandBiomeGenerator());

    @Override
    public BiomeGen getBiome(double temperature, double rainfall) {
        if (temperature < 0.6) {
            return temperature > 0.4 ? HighlandHillsGen.INSTANCE : HighlandPeaksGen.INSTANCE;
        } else if (temperature > 0.75) {
            return HighlandSpringGen.INSTANCE;
        }
        return HighlandPlainsGen.INSTANCE;
    }

    @Override
    public Codec<? extends BiomeGenerator> getCodec() {
        return CODEC;
    }
}
