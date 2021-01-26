package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import supercoder79.survivalgames.game.map.gen.RoofedTreeGen;
import xyz.nucleoid.plasmid.game.gen.MapGen;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class RoofedForestGen implements BiomeGen {
	public static final RoofedForestGen INSTANCE = new RoofedForestGen();

	@Override
	public MapGen tree(int x, int z, Random random) {
		return RoofedTreeGen.INSTANCE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return 12;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.DARK_FOREST;
	}
}
