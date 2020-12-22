package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import xyz.nucleoid.plasmid.game.gen.MapGen;
import xyz.nucleoid.plasmid.game.gen.feature.tree.AspenTreeGen;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class AspenForestGen implements BiomeGen {
	public static final AspenForestGen INSTANCE = new AspenForestGen();

	@Override
	public MapGen tree(int x, int z, Random random) {
		return AspenTreeGen.INSTANCE;
	}

	@Override
	public double modifyTreeCount(double original) {
		return original * 0.4;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.BIRCH_FOREST;
	}
}
