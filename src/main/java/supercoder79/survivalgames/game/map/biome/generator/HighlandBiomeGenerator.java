package supercoder79.survivalgames.game.map.biome.generator;

import com.mojang.serialization.Codec;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.biome.highland.*;

public class HighlandBiomeGenerator implements BiomeGenerator {

    public static final Codec<HighlandBiomeGenerator> CODEC = Codec.unit(new HighlandBiomeGenerator());

    @Override
    public BiomeGen getBiome(double temperature, double rainfall) {
//        if (temperature < 0.6) {
//            return temperature > 0.4 ? HighlandHillsGen.INSTANCE : HighlandPeaksGen.INSTANCE;
//        } else if (temperature > 0.75) {
//            return HighlandSpringGen.INSTANCE;
//        }
//        return HighlandPlainsGen.INSTANCE;

        double mountainness = Math.abs(temperature);

        if (mountainness > 0.7) {
            return HighlandPeaksGen.INSTANCE;
        } else if (mountainness > 0.45) {
            return HighlandHillsGen.INSTANCE;
        } else if (mountainness > 0.1) {
            if (rainfall > -0.1) {
                return HighlandPlainsGen.INSTANCE;
            }

            return HighlandSpringGen.INSTANCE;
        } else {
            return HighlandRiverGen.INSTANCE;
        }
    }

    @Override
    public Codec<? extends BiomeGenerator> getCodec() {
        return CODEC;
    }
}
