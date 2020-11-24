package supercoder79.survivalgames.game;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.GameMode;
import supercoder79.survivalgames.game.config.SurvivalGamesConfig;
import supercoder79.survivalgames.game.map.SurvivalGamesMap;
import xyz.nucleoid.fantasy.BubbleWorldConfig;
import xyz.nucleoid.fantasy.BubbleWorldSpawner;
import xyz.nucleoid.plasmid.game.GameOpenContext;
import xyz.nucleoid.plasmid.game.GameOpenProcedure;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.GameWaitingLobby;
import xyz.nucleoid.plasmid.game.StartResult;
import xyz.nucleoid.plasmid.game.event.*;

public final class SurvivalGamesWaiting {
	private final GameSpace world;
	private final SurvivalGamesMap map;
	private final SurvivalGamesConfig config;

	private final SurvivalGamesSpawnLogic spawnLogic;

	private SurvivalGamesWaiting(GameSpace world, SurvivalGamesMap map, SurvivalGamesConfig config) {
		this.world = world;
		this.map = map;
		this.config = config;

		this.spawnLogic = new SurvivalGamesSpawnLogic(world, config);
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

			game.on(PlayerAddListener.EVENT, waiting::addPlayer);
			game.on(PlayerDeathListener.EVENT, waiting::onPlayerDeath);
			game.on(AttackEntityListener.EVENT, waiting::onAttackEntity);
			game.on(UseBlockListener.EVENT, waiting::onUseBlock);
			game.on(UseItemListener.EVENT, waiting::onUseItem);
		});
	}

	private ActionResult onAttackEntity(ServerPlayerEntity attacker, Hand hand, Entity attacked, EntityHitResult hitResult) {
		return ActionResult.SUCCESS;
	}

	private ActionResult onUseBlock(ServerPlayerEntity player, Hand hand, BlockHitResult hitResult) {
		return ActionResult.SUCCESS;
	}

	private TypedActionResult<ItemStack> onUseItem(ServerPlayerEntity player, Hand hand) {
		return TypedActionResult.success(player.getStackInHand(hand));
	}

	private StartResult requestStart() {
		SurvivalGamesActive.open(this.world, this.map, this.config);
		return StartResult.OK;
	}

	private void addPlayer(ServerPlayerEntity player) {
		this.spawnPlayer(player);
	}

	private ActionResult onPlayerDeath(ServerPlayerEntity player, DamageSource source) {
		this.spawnPlayer(player);
		return ActionResult.SUCCESS;
	}

	private void spawnPlayer(ServerPlayerEntity player) {
		this.spawnLogic.resetPlayer(player, GameMode.ADVENTURE);
		this.spawnLogic.spawnPlayer(player);
	}
}
