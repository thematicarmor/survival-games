package supercoder79.survivalgames.game.map.biome.source;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.gegy.noise.sampler.NoiseSampler2d;
import supercoder79.survivalgames.noise.simplex.OpenSimplexNoise;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import supercoder79.survivalgames.SurvivalGames;
import supercoder79.survivalgames.game.map.biome.*;
import supercoder79.survivalgames.game.map.biome.generator.BiomeGenerator;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public final class FakeBiomeSource extends BiomeSource {

	public static final Codec<FakeBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(source -> source.biomeRegistry),
			Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed),
			BiomeGenerator.CODEC.fieldOf("biome_generator").forGetter(source -> source.biomeGenerator))
			.apply(instance, instance.stable(FakeBiomeSource::new)));

	private final Registry<Biome> biomeRegistry;
	private final long seed;
	private final BiomeGenerator biomeGenerator;

	private final NoiseSampler2d temperatureNoise;
	private final NoiseSampler2d rainfallNoise;
	private final NoiseSampler2d roughnessNoise;

	public FakeBiomeSource(Registry<Biome> biomeRegistry, long seed, BiomeGenerator biomeGenerator) {
		super(ImmutableList.of());
		this.biomeRegistry = biomeRegistry;
		this.seed = seed;
		this.biomeGenerator = biomeGenerator;

		temperatureNoise = compile(seed + 79, 240.0, 0.85);
		rainfallNoise = compile(seed - 79, 240.0, 0.85);
		roughnessNoise = compile(seed, 60.0, 0.15);
	}

	private static NoiseSampler2d compile(long seed, double scale, double amp) {
		return SurvivalGames.NOISE_COMPILER.compile(OpenSimplexNoise.create().scale(1 / scale, 1 / scale).add(1.0).div(2.0).mul(amp), NoiseSampler2d.TYPE).create(seed);
	}

	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long seed) {
		return new FakeBiomeSource(this.biomeRegistry, seed, this.biomeGenerator);
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		return biomeRegistry.get(getRealBiome(biomeX << 2, biomeZ << 2).getFakingBiome());
	}

	public BiomeGen getRealBiome(int x, int z) {
		double roughness = this.roughnessNoise.get(x,  z);
		double temperature = this.temperatureNoise.get(x,  z) + roughness;
		double rainfall = this.rainfallNoise.get(x,  z) + roughness;

		return this.biomeGenerator.getBiome(temperature, rainfall);
	}
}
