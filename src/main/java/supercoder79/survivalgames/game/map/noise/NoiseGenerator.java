package supercoder79.survivalgames.game.map.noise;

import java.util.Random;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import supercoder79.survivalgames.game.config.SurvivalGamesConfig;
import supercoder79.survivalgames.game.map.biome.source.FakeBiomeSource;
import xyz.nucleoid.plasmid.registry.TinyRegistry;

public interface NoiseGenerator {
	TinyRegistry<Codec<? extends NoiseGenerator>> REGISTRY = TinyRegistry.create();
	MapCodec<NoiseGenerator> CODEC = REGISTRY.dispatchMap(NoiseGenerator::getCodec, Function.identity());

	void initialize(Random random, SurvivalGamesConfig config);

	double getHeightAt(FakeBiomeSource biomeSource, int x, int z);

	default boolean shouldOutskirtsSpawn(int x, int z) {
		return true;
	}

	default double maxSpawnDistFactor() {
		return 0.95;
	}

	default double minSpawnDistFactor() {
		return 0.60;
	}

	Codec<? extends NoiseGenerator> getCodec();
}
