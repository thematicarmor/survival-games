package supercoder79.survivalgames.game.map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class SurvivalGamesMap {
	public ChunkGenerator chunkGenerator(MinecraftServer server) {
		return new SurvivalGamesChunkGenerator(server);
	}
}
