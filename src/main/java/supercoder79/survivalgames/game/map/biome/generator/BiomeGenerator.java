package supercoder79.survivalgames.game.map.biome.generator;

import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import xyz.nucleoid.plasmid.registry.TinyRegistry;

public interface BiomeGenerator {
	TinyRegistry<Codec<? extends BiomeGenerator>> REGISTRY = TinyRegistry.newStable();
	MapCodec<BiomeGenerator> CODEC = REGISTRY.dispatchMap(BiomeGenerator::getCodec, Function.identity());

	BiomeGen getBiome(double temperature, double rainfall);

	default boolean generateSnow() {
		return false;
	}

	Codec<? extends BiomeGenerator> getCodec();
}
