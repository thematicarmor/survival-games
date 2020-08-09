package supercoder79.survivalgames.game.map;

import java.util.concurrent.CompletableFuture;

import net.minecraft.util.Util;

public class SurvivalGamesMapGenerator {
	public CompletableFuture<SurvivalGamesMap> create() {
		return CompletableFuture.supplyAsync(this::build, Util.getServerWorkerExecutor());
	}

	public SurvivalGamesMap build() {
		return new SurvivalGamesMap();
	}
}
