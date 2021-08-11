package supercoder79.survivalgames.game.map.noise;

import java.util.Random;

import com.mojang.serialization.Codec;
import dev.gegy.noise.sampler.NoiseSampler2d;
import supercoder79.survivalgames.SurvivalGames;
import supercoder79.survivalgames.game.config.SurvivalGamesConfig;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.biome.source.FakeBiomeSource;
import net.minecraft.util.math.MathHelper;
import supercoder79.survivalgames.noise.simplex.OpenSimplexNoise;

public class DefaultNoiseGenerator implements NoiseGenerator {
	public static final Codec<DefaultNoiseGenerator> CODEC = Codec.unit(new DefaultNoiseGenerator());
	private NoiseSampler2d baseNoise;
	private NoiseSampler2d interpolationNoise;
	private NoiseSampler2d lowerInterpolatedNoise;
	private NoiseSampler2d upperInterpolatedNoise;
	private NoiseSampler2d detailNoise;

	private NoiseSampler2d riverNoise;
	private NoiseSampler2d riverNoise2;
	private NoiseSampler2d riverDepthNoise;

	@Override
	public void initialize(Random random, SurvivalGamesConfig config) {
		this.baseNoise = compile(random, 256.0);
		this.interpolationNoise = compile(random, 50.0);
		this.lowerInterpolatedNoise = compile(random,  60.0);
		this.upperInterpolatedNoise = compile(random,  60.0);
		this.detailNoise = compile(random, 20.0);

		this.riverNoise = compile(random, 160.0);
		this.riverNoise2 = compile(random, 32.0); // TODO: octave sampler
		this.riverDepthNoise = compile(random, 60.0);
	}

	public static NoiseSampler2d compile(Random random, double scale) {
		return SurvivalGames.NOISE_COMPILER.compile(OpenSimplexNoise.create().scale(1 / scale, 1 / scale), NoiseSampler2d.TYPE).create(random.nextLong());
	}

	@Override
	public double getHeightAt(FakeBiomeSource biomeSource, int x, int z) {
		double upperNoiseFactor = 0;
		double lowerNoiseFactor = 0;
		double upperLerpHigh = 0;
		double upperLerpLow = 0;
		double lowerLerpHigh = 0;
		double lowerLerpLow = 0;
		double detailFactor = 0;
		double weight = 0;

		for (int aX = -4; aX <= 4; aX++) {
			for (int aZ = -4; aZ <= 4; aZ++) {
				BiomeGen biome = biomeSource.getRealBiome(x + aX, z + aZ);
				upperNoiseFactor += biome.upperNoiseFactor();
				lowerNoiseFactor += biome.lowerNoiseFactor();
				upperLerpHigh += biome.upperLerpHigh();
				upperLerpLow += biome.upperLerpLow();
				lowerLerpHigh += biome.lowerLerpHigh();
				lowerLerpLow += biome.lowerLerpLow();
				detailFactor += biome.detailFactor();

				weight++;
			}
		}

		upperNoiseFactor /= weight;
		lowerNoiseFactor /= weight;
		upperLerpHigh /= weight;
		upperLerpLow /= weight;
		lowerLerpHigh /= weight;
		lowerLerpLow /= weight;
		detailFactor /= weight;

		// Create base terrain
		double noise = baseNoise.get(x, z);
		noise *= noise > 0 ? upperNoiseFactor : lowerNoiseFactor;

		// Add hills in a similar method to mc interpolation noise
		double lerp = interpolationNoise.get(x, z) * 2.5;
		if (lerp > 1) {
			double upperNoise = upperInterpolatedNoise.get(x, z);
			upperNoise *= upperNoise > 0 ? upperLerpHigh : upperLerpLow;
			noise += upperNoise;
		} else if (lerp < 0) {
			double lowerNoise = lowerInterpolatedNoise.get(x, z);
			lowerNoise *= lowerNoise > 0 ? lowerLerpHigh : lowerLerpLow;
			noise += lowerNoise;
		} else {
			double upperNoise = upperInterpolatedNoise.get(x, z);
			upperNoise *= upperNoise > 0 ? upperLerpHigh : upperLerpLow;

			double lowerNoise = lowerInterpolatedNoise.get(x, z);
			lowerNoise *= lowerNoise > 0 ? lowerLerpHigh : lowerLerpLow;

			noise += MathHelper.lerp(lerp, lowerNoise, upperNoise);
		}

		// Add small details to make the terrain less rounded
		noise += detailNoise.get(x, z) * detailFactor;

		// River gen
		double river = this.riverNoise.get(x, z) + this.riverNoise2.get(x, z) * 0.2;
		if (river > -0.24 && river < 0.24) {
			double depth = -10 + this.riverDepthNoise.get(x, z) * 1.75;

			noise = MathHelper.lerp(smoothstep(river / 0.24), noise, depth);
		}

		return noise;
	}

	private static double smoothstep(double x) {
		return (1 - x * x) * (1 - x * x) * (1 - x * x);
	}

	@Override
	public Codec<? extends NoiseGenerator> getCodec() {
		return CODEC;
	}
}
