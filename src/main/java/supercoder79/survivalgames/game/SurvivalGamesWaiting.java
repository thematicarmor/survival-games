package supercoder79.survivalgames.game;

import java.util.concurrent.CompletableFuture;

import supercoder79.survivalgames.game.config.SurvivalGamesConfig;
import xyz.nucleoid.plasmid.game.GameWorld;
import xyz.nucleoid.plasmid.game.StartResult;
import xyz.nucleoid.plasmid.game.event.AttackEntityListener;
import xyz.nucleoid.plasmid.game.event.OfferPlayerListener;
import xyz.nucleoid.plasmid.game.event.PlayerAddListener;
import xyz.nucleoid.plasmid.game.event.PlayerDeathListener;
import xyz.nucleoid.plasmid.game.event.RequestStartListener;
import xyz.nucleoid.plasmid.game.event.UseBlockListener;
import xyz.nucleoid.plasmid.game.event.UseItemListener;
import xyz.nucleoid.plasmid.game.player.JoinResult;
import xyz.nucleoid.plasmid.game.rule.GameRule;
import xyz.nucleoid.plasmid.game.rule.RuleResult;
import supercoder79.survivalgames.game.map.SurvivalGamesMap;
import supercoder79.survivalgames.game.map.SurvivalGamesMapGenerator;
import xyz.nucleoid.plasmid.game.world.bubble.BubbleWorldConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.GameMode;

public final class SurvivalGamesWaiting {
	private final GameWorld world;
	private final SurvivalGamesMap map;
	private final SurvivalGamesConfig config;

	private final SurvivalGamesSpawnLogic spawnLogic;

	private SurvivalGamesWaiting(GameWorld world, SurvivalGamesMap map, SurvivalGamesConfig config) {
		this.world = world;
		this.map = map;
		this.config = config;

		this.spawnLogic = new SurvivalGamesSpawnLogic(world, config);
	}

	public static CompletableFuture<Void> open(MinecraftServer server, SurvivalGamesConfig config) {
		SurvivalGamesMapGenerator generator = new SurvivalGamesMapGenerator();

		return generator.create().thenAccept(map -> {
			BubbleWorldConfig worldConfig = new BubbleWorldConfig()
					.setGenerator(map.chunkGenerator(server))
					.setDefaultGameMode(GameMode.SPECTATOR);

			GameWorld world = GameWorld.open(server, worldConfig);

			SurvivalGamesWaiting waiting = new SurvivalGamesWaiting(world, map, config);

			world.openGame(game -> {
				game.setRule(GameRule.CRAFTING, RuleResult.DENY);
				game.setRule(GameRule.PORTALS, RuleResult.DENY);
				game.setRule(GameRule.PVP, RuleResult.DENY);
				game.setRule(GameRule.FALL_DAMAGE, RuleResult.DENY);
				game.setRule(GameRule.HUNGER, RuleResult.DENY);

				game.on(RequestStartListener.EVENT, waiting::requestStart);
				game.on(OfferPlayerListener.EVENT, waiting::offerPlayer);

				game.on(PlayerAddListener.EVENT, waiting::addPlayer);
				game.on(PlayerDeathListener.EVENT, waiting::onPlayerDeath);
				game.on(AttackEntityListener.EVENT, waiting::onAttackEntity);
				game.on(UseBlockListener.EVENT, waiting::onUseBlock);
				game.on(UseItemListener.EVENT, waiting::onUseItem);
			});
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

	private JoinResult offerPlayer(ServerPlayerEntity player) {
		if (this.world.getPlayerCount() >= this.config.playerConfig.getMaxPlayers()) {
			return JoinResult.gameFull();
		}

		return JoinResult.ok();
	}

	private StartResult requestStart() {
		if (this.world.getPlayerCount() < this.config.playerConfig.getMinPlayers()) {
			return StartResult.notEnoughPlayers();
		}

		SurvivalGamesActive.open(this.world, this.map, this.config);

		return StartResult.ok();
	}

	private void addPlayer(ServerPlayerEntity player) {
		this.spawnPlayer(player);
	}

	private boolean onPlayerDeath(ServerPlayerEntity player, DamageSource source) {
		this.spawnPlayer(player);
		return true;
	}

	private void spawnPlayer(ServerPlayerEntity player) {
		this.spawnLogic.resetPlayer(player, GameMode.ADVENTURE);
		this.spawnLogic.spawnPlayer(player);
	}
}
