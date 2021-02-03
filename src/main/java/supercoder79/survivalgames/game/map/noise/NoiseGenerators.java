package supercoder79.survivalgames.game.map.noise;

import com.mojang.serialization.Codec;

import net.minecraft.util.Identifier;

public final class NoiseGenerators {
	public static void init() {
		register("default", DefaultNoiseGenerator.CODEC);
		register("island", IslandNoiseGenerator.CODEC);
	}

	public static void register(String name, Codec<? extends NoiseGenerator> generator) {
		NoiseGenerator.REGISTRY.register(new Identifier("survivalgames", name), generator);
	}
}
