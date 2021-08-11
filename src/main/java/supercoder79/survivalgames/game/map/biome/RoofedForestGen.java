package supercoder79.survivalgames.game.map.biome;

import java.util.Random;

import supercoder79.survivalgames.game.map.gen.BranchingTreeGen;
import xyz.nucleoid.substrate.gen.MapGen;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class RoofedForestGen implements BiomeGen {
	public static final RoofedForestGen INSTANCE = new RoofedForestGen();

	@Override
	public MapGen tree(int x, int z, Random random) {
		return BranchingTreeGen.DARK_OAK;
	}

	@Override
	public double modifyTreeChance(double original) {
		return 6;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.DARK_FOREST;
	}
}
