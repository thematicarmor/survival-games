package supercoder79.survivalgames.game.map.gen.biome;

import java.util.Random;

public interface BiomeProvider {
	void initialize(Random random);

	BiomeGen get(int x, int z);
}
