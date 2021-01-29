package supercoder79.survivalgames.game.map.biome.source;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import kdotjpg.opensimplex.OpenSimplexNoise;
import supercoder79.survivalgames.game.map.biome.*;
import supercoder79.survivalgames.game.map.biome.generator.BiomeGenerator;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
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

	private final OpenSimplexNoise temperatureNoise;
	private final OpenSimplexNoise rainfallNoise;
	private final OpenSimplexNoise roughnessNoise;

	public FakeBiomeSource(Registry<Biome> biomeRegistry, long seed, BiomeGenerator biomeGenerator) {
		super(ImmutableList.of());
		this.biomeRegistry = biomeRegistry;
		this.seed = seed;
		this.biomeGenerator = biomeGenerator;

		temperatureNoise = new OpenSimplexNoise(seed + 79);
		rainfallNoise = new OpenSimplexNoise(seed - 79);
		roughnessNoise = new OpenSimplexNoise(seed);
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
		double temperature = (temperatureNoise.eval(x / 240.0, z / 240.0) + 1) / 2;
		temperature = temperature * 0.9 + (((roughnessNoise.eval(x / 72.0, z / 72.0) + 1) / 2) * 0.1);

		double rainfall = (rainfallNoise.eval(x / 240.0, z / 240.0) + 1) / 2;
		rainfall = rainfall * 0.8 + (((roughnessNoise.eval(x / 40.0, z / 40.0) + 1) / 2) * 0.2);

		return this.biomeGenerator.getBiome(temperature, rainfall);
	}
}
