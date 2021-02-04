package supercoder79.survivalgames.game.map.biome.generator;

import com.mojang.serialization.Codec;

import net.minecraft.util.Identifier;

public final class BiomeGenerators {
	public static void init() {
		register("default", DefaultBiomeGenerator.CODEC);
		register("winterland", WinterlandBiomeGenerator.CODEC);
		register("tropical", TropicalBiomeGenerator.CODEC);
	}

	public static void register(String name, Codec<? extends BiomeGenerator> generator) {
		BiomeGenerator.REGISTRY.register(new Identifier("survivalgames", name), generator);
	}
}
