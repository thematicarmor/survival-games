package supercoder79.survivalgames.game.map.gen.biome;

import java.util.Random;

import kdotjpg.opensimplex.OpenSimplexNoise;

import net.minecraft.util.math.MathHelper;

public class DefaultBiomeProvider implements BiomeProvider {
	//TODO: codec

	OpenSimplexNoise temperature;
	OpenSimplexNoise temperatureRough;
	OpenSimplexNoise rainfall;

	@Override
	public void initialize(Random random) {
		temperature = new OpenSimplexNoise(random.nextLong());
		temperatureRough = new OpenSimplexNoise(random.nextLong());
		rainfall = new OpenSimplexNoise(random.nextLong());
	}

	@Override
	public BiomeGen get(int x, int z) {
		double temp = (temperature.eval(x / 180.0, z / 180.0) + 1) / 2.0;
		double tempRough = (temperatureRough.eval(x / 60.0, z / 60.0) + 1) / 2.0;
		temp = MathHelper.clamp((temp * 0.8) + (tempRough  * 0.2), 0, 1);
		double rain = ((rainfall.eval(x / 220.0, z / 220.0) + 1) / 2.0) * temp;

		if (temp > 0.7) {
			return Biomes.SCRUBLAND;
		}

		if (rain > 0.25) {
			return Biomes.FOREST;
		}

		if (temp < 0.4) {
			if (rain > 0.1) {
				return Biomes.TAIGA;
			} else {
				return Biomes.MOUNTAINS;
			}
		}

		return Biomes.PLAINS;
	}
}
