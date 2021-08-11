package supercoder79.survivalgames.game;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import supercoder79.survivalgames.game.config.SurvivalGamesConfig;
import supercoder79.survivalgames.game.map.SurvivalGamesMap;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;
import xyz.nucleoid.plasmid.game.GameOpenContext;
import xyz.nucleoid.plasmid.game.GameOpenProcedure;
import xyz.nucleoid.plasmid.game.GameResult;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.common.GameWaitingLobby;
import xyz.nucleoid.plasmid.game.event.GameActivityEvents;
import xyz.nucleoid.plasmid.game.event.GamePlayerEvents;

public final class SurvivalGamesWaiting {
	private final GameSpace world;
	private final SurvivalGamesMap map;
	private final SurvivalGamesConfig config;

	private SurvivalGamesWaiting(GameSpace world, SurvivalGamesMap map, SurvivalGamesConfig config) {
		this.world = world;
		this.map = map;
		this.config = config;
	}

	public static GameOpenProcedure open(GameOpenContext<SurvivalGamesConfig> context) {
		SurvivalGamesMap map = new SurvivalGamesMap();
		RuntimeWorldConfig worldConfig = new RuntimeWorldConfig()
				.setTimeOfDay(context.config().time)
				.setGenerator(map.chunkGenerator(context.server(), context.config()))
				//.setSpawner(BubbleWorldSpawner.atSurface(0, 0))
				.setDimensionType(RegistryKey.of(Registry.DIMENSION_TYPE_KEY, context.config().dimension));


		return context.openWithWorld(worldConfig, (game, world) -> {
			SurvivalGamesWaiting waiting = new SurvivalGamesWaiting(game.getGameSpace(), map, context.config());
			GameWaitingLobby.applyTo(game, context.config().playerConfig);

			game.listen(GameActivityEvents.REQUEST_START, () -> waiting.requestStart(world));
			game.listen(GamePlayerEvents.REMOVE, waiting::onPlayerDeath);
			game.listen(GamePlayerEvents.OFFER, offer -> offer.accept(world, new Vec3d(0, 70, 0)));
		});
	}

	private GameResult requestStart(ServerWorld world) {
		SurvivalGamesActive.open(this.world, this.map, this.config, world);
		return GameResult.ok();
	}

	private void onPlayerDeath(ServerPlayerEntity player) {
	}
}
