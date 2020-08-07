package supercoder79.survivalgames.game.map.gen.biome;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class Biomes {
	public static final BiomeGen PLAINS = new PlainsGen();
	public static final BiomeGen FOREST = new ForestGen();
	public static final BiomeGen TAIGA = new TaigaGen();
	public static final BiomeGen SCRUBLAND = new ScrublandGen();
	public static final BiomeGen MOUNTAINS = new MountainsGen();

	public static final List<BiomeGen> BIOMES = ImmutableList.of(PLAINS, FOREST, TAIGA, SCRUBLAND, MOUNTAINS);
}
