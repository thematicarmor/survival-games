package supercoder79.survivalgames.game.map.biome;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import xyz.nucleoid.substrate.gen.MapGen;
import xyz.nucleoid.substrate.gen.tree.AspenTreeGen;

import java.util.Random;

public final class AspenForestGen implements BiomeGen {
	public static final AspenForestGen INSTANCE = new AspenForestGen();

	@Override
	public MapGen tree(int x, int z, Random random) {
		return AspenTreeGen.INSTANCE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return original * 0.9;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.BIRCH_FOREST;
	}
}
