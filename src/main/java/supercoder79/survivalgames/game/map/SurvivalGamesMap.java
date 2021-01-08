package supercoder79.survivalgames.game.map;

import supercoder79.survivalgames.game.config.SurvivalGamesConfig;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class SurvivalGamesMap {
	public ChunkGenerator chunkGenerator(MinecraftServer server, SurvivalGamesConfig config) {
		return new SurvivalGamesChunkGenerator(server, config);
	}
}
