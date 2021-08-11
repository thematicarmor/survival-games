package supercoder79.survivalgames.game.map.biome;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import supercoder79.survivalgames.game.map.gen.TaigaTreeGen;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.Random;

public final class TaigaGen implements BiomeGen {
	public static final TaigaGen INSTANCE = new TaigaGen();
	@Override
	public MapGen tree(int x, int z, Random random) {
		return TaigaTreeGen.INSTANCE;
	}

	@Override
	public double modifyTreeChance(double original) {
		return original * 1.25;
	}

	@Override
	public RegistryKey<Biome> getFakingBiome() {
		return BiomeKeys.TAIGA;
	}
}
