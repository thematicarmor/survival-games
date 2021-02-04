package supercoder79.survivalgames.game.map.biome.generator;

import com.mojang.serialization.Codec;

import supercoder79.survivalgames.game.map.biome.AspenForestGen;
import supercoder79.survivalgames.game.map.biome.BadlandsGen;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.biome.DeadlandGen;
import supercoder79.survivalgames.game.map.biome.DesertGen;
import supercoder79.survivalgames.game.map.biome.JungleGen;
import supercoder79.survivalgames.game.map.biome.JungleHillsGen;
import supercoder79.survivalgames.game.map.biome.MesaPlateauGen;
import supercoder79.survivalgames.game.map.biome.MountainGen;
import supercoder79.survivalgames.game.map.biome.PlainsGen;
import supercoder79.survivalgames.game.map.biome.PoplarForestGen;
import supercoder79.survivalgames.game.map.biome.RoofedForestGen;
import supercoder79.survivalgames.game.map.biome.SavannaGen;
import supercoder79.survivalgames.game.map.biome.ShatteredSavannaGen;

public class TropicalBiomeGenerator implements BiomeGenerator {
    public static final Codec<TropicalBiomeGenerator> CODEC = Codec.unit(new TropicalBiomeGenerator());

	@Override
	public BiomeGen getBiome(double temperature, double rainfall) {
		if (temperature < 0.35) {
			if (rainfall > 0.5) {
				return SavannaGen.INSTANCE;
			}

			return ShatteredSavannaGen.INSTANCE;
		}

		if (rainfall < 0.35) {
			if (temperature > 0.7 || rainfall < 0.1) {
				return DesertGen.INSTANCE;
			}

			if (temperature > 0.55) {
				return MesaPlateauGen.INSTANCE;
			}

			return BadlandsGen.INSTANCE;
		}

		if (rainfall > 0.7) {
			if (temperature > 0.5) {
				return AspenForestGen.INSTANCE;
			}
			return JungleGen.INSTANCE;
		}
		return JungleHillsGen.INSTANCE;
	}

	@Override
	public Codec<? extends BiomeGenerator> getCodec() {
		return CODEC;
	}
}
