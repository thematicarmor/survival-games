package supercoder79.survivalgames.game.map;

import net.minecraft.world.gen.chunk.ChunkGenerator;

public class SurvivalGamesMap {
	public ChunkGenerator chunkGenerator() {
		return new SurvivalGamesChunkGenerator();
	}
}
