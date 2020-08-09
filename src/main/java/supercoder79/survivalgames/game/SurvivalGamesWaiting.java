package supercoder79.survivalgames.game;

import java.util.concurrent.CompletableFuture;

import net.gegy1000.plasmid.game.Game;
import net.gegy1000.plasmid.game.GameWorld;
import net.gegy1000.plasmid.game.GameWorldState;
import net.gegy1000.plasmid.game.StartResult;
import net.gegy1000.plasmid.game.config.PlayerConfig;
import net.gegy1000.plasmid.game.event.AttackEntityListener;
import net.gegy1000.plasmid.game.event.OfferPlayerListener;
import net.gegy1000.plasmid.game.event.PlayerAddListener;
import net.gegy1000.plasmid.game.event.PlayerDeathListener;
import net.gegy1000.plasmid.game.event.RequestStartListener;
import net.gegy1000.plasmid.game.event.UseBlockListener;
import net.gegy1000.plasmid.game.event.UseItemListener;
import net.gegy1000.plasmid.game.player.JoinResult;
import net.gegy1000.plasmid.game.rule.GameRule;
import net.gegy1000.plasmid.game.rule.RuleResult;
import supercoder79.survivalgames.game.map.SurvivalGamesChunkGenerator;
import supercoder79.survivalgames.game.map.SurvivalGamesMap;
import supercoder79.survivalgames.game.map.SurvivalGamesMapGenerator;

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

public final class SurvivalGamesWaiting {
	private final GameWorld world;
	private final SurvivalGamesMap map;
	private final SurvivalGamesConfig config;

	private final SurvivalGamesSpawnLogic spawnLogic;

	private SurvivalGamesWaiting(GameWorld world, SurvivalGamesMap map, SurvivalGamesConfig config) {
		this.world = world;
		this.map = map;
		this.config = config;

		this.spawnLogic = new SurvivalGamesSpawnLogic(world);
	}

	public static CompletableFuture<Void> open(GameWorldState worldState, SurvivalGamesConfig config) {
		SurvivalGamesMapGenerator generator = new SurvivalGamesMapGenerator();

		return generator.create().thenAccept((map) -> {
			GameWorld world = worldState.openWorld(map.chunkGenerator());

			SurvivalGamesWaiting waiting = new SurvivalGamesWaiting(world, map, config);

			world.newGame(game -> {
				game.setRule(GameRule.ALLOW_CRAFTING, RuleResult.DENY);
				game.setRule(GameRule.ALLOW_PORTALS, RuleResult.DENY);
				game.setRule(GameRule.ALLOW_PVP, RuleResult.DENY);
				game.setRule(GameRule.FALL_DAMAGE, RuleResult.DENY);
				game.setRule(GameRule.ENABLE_HUNGER, RuleResult.DENY);

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
