package supercoder79.survivalgames.game;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.GameMode;
import supercoder79.survivalgames.game.config.SurvivalGamesConfig;
import supercoder79.survivalgames.game.map.SurvivalGamesMap;
import xyz.nucleoid.fantasy.BubbleWorldConfig;
import xyz.nucleoid.fantasy.BubbleWorldSpawner;
import xyz.nucleoid.plasmid.game.*;
import xyz.nucleoid.plasmid.game.event.PlayerDeathListener;
import xyz.nucleoid.plasmid.game.event.RequestStartListener;

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
		BubbleWorldConfig worldConfig = new BubbleWorldConfig()
				.setGenerator(map.chunkGenerator(context.getServer()))
				.setSpawner(BubbleWorldSpawner.atSurface(0, 0))
				.setDefaultGameMode(GameMode.SPECTATOR);

		return context.createOpenProcedure(worldConfig, (game) -> {
			SurvivalGamesWaiting waiting = new SurvivalGamesWaiting(game.getSpace(), map, context.getConfig());
			GameWaitingLobby.applyTo(game, context.getConfig().playerConfig);

			game.on(RequestStartListener.EVENT, waiting::requestStart);
			game.on(PlayerDeathListener.EVENT, waiting::onPlayerDeath);
		});
	}

	private StartResult requestStart() {
		SurvivalGamesActive.open(this.world, this.map, this.config);
		return StartResult.OK;
	}

	private ActionResult onPlayerDeath(ServerPlayerEntity player, DamageSource source) {
		return ActionResult.FAIL;
	}
}
