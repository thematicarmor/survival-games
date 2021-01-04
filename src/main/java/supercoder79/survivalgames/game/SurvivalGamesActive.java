package supercoder79.survivalgames.game;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.WorldBorderS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import supercoder79.survivalgames.game.config.SurvivalGamesConfig;
import supercoder79.survivalgames.game.map.SurvivalGamesMap;
import xyz.nucleoid.plasmid.game.GameCloseReason;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.event.*;
import xyz.nucleoid.plasmid.game.player.JoinResult;
import xyz.nucleoid.plasmid.game.player.PlayerSet;
import xyz.nucleoid.plasmid.game.rule.GameRule;
import xyz.nucleoid.plasmid.game.rule.RuleResult;
import xyz.nucleoid.plasmid.util.PlayerRef;
import xyz.nucleoid.plasmid.widget.GlobalWidgets;

import java.util.HashSet;
import java.util.Set;

public class SurvivalGamesActive {
	private final GameSpace world;
	private final SurvivalGamesMap map;
	private final SurvivalGamesConfig config;

	private final PlayerSet participants;

	private final SurvivalGamesSpawnLogic spawnLogic;
	private final SurvivalGamesBar bar;

	private long startTime;
	private long shrinkStartTime;
	private boolean borderShrinkStarted = false;
	private long gameCloseTick = Long.MAX_VALUE;

	private SurvivalGamesActive(GameSpace world, SurvivalGamesMap map, SurvivalGamesConfig config, PlayerSet participants, GlobalWidgets widgets) {
		this.world = world;
		this.map = map;
		this.config = config;
		this.participants = participants;

		this.spawnLogic = new SurvivalGamesSpawnLogic(world, config);
		this.bar = SurvivalGamesBar.create(widgets);
	}

	public static void open(GameSpace world, SurvivalGamesMap map, SurvivalGamesConfig config) {
		world.openGame(game -> {
			GlobalWidgets widgets = new GlobalWidgets(game);
			SurvivalGamesActive active = new SurvivalGamesActive(world, map, config, world.getPlayers(), widgets);

			game.setRule(GameRule.CRAFTING, RuleResult.ALLOW);
			game.setRule(GameRule.PORTALS, RuleResult.DENY);
			game.setRule(GameRule.PVP, RuleResult.ALLOW);
			game.setRule(GameRule.BLOCK_DROPS, RuleResult.ALLOW);
			game.setRule(GameRule.FALL_DAMAGE, RuleResult.ALLOW);
			game.setRule(GameRule.HUNGER, RuleResult.DENY);
			game.setRule(GameRule.UNSTABLE_TNT, RuleResult.ALLOW);

			game.on(GameOpenListener.EVENT, active::open);
			game.on(GameCloseListener.EVENT, active::close);

			game.on(OfferPlayerListener.EVENT, player -> JoinResult.ok());
			game.on(PlayerAddListener.EVENT, active::addPlayer);

			game.on(GameTickListener.EVENT, active::tick);

			game.on(BreakBlockListener.EVENT, active::onBreakBlock);

			game.on(PlayerDeathListener.EVENT, active::onPlayerDeath);
			game.on(UseBlockListener.EVENT, active::onUseBlock);
		});
	}

	private void open() {
		ServerWorld world = this.world.getWorld();

		// World border stuff
		world.getWorldBorder().setCenter(0, 0);
		world.getWorldBorder().setSize(config.borderConfig.startSize);
		world.getWorldBorder().setDamagePerBlock(0.5);
		startTime = world.getTime();

		int index = 0;
		int spawnDistance = (config.borderConfig.startSize / 2) * 3 / 4;

		for (ServerPlayerEntity player : this.participants) {
			player.networkHandler.sendPacket(new WorldBorderS2CPacket(world.getWorldBorder(), WorldBorderS2CPacket.Type.INITIALIZE));

			double theta = ((double) index++ / this.participants.size()) * 2 * Math.PI;
			int x = MathHelper.floor(Math.cos(theta) * spawnDistance);
			int z = MathHelper.floor(Math.sin(theta) * spawnDistance);

			this.spawnLogic.resetPlayer(player, GameMode.SURVIVAL);
			this.spawnLogic.spawnPlayerAt(player, x, z);

			for (ItemStack stack : config.kit) {
				player.inventory.insertStack(stack.copy());
			}
		}
	}

	private void close() {
		// this should hopefully fix players returning as survival mode to the lobby
		for (ServerPlayerEntity player : this.participants) {
			player.setGameMode(GameMode.ADVENTURE);
		}
	}

	private void addPlayer(ServerPlayerEntity player) {
		if (!this.participants.contains(PlayerRef.of(player))) {
			this.spawnSpectator(player);
		}
	}

	private void tick() {
		ServerWorld world = this.world.getWorld();

		if (!this.borderShrinkStarted) {
			long totalSafeTime = config.borderConfig.safeSecs * 20L;
			this.bar.tickSafe(totalSafeTime - (world.getTime() - startTime), totalSafeTime);

			if ((world.getTime() - startTime) > totalSafeTime) {
				this.bar.setActive();
				this.borderShrinkStarted = true;
				this.shrinkStartTime = world.getTime();

				world.getWorldBorder().interpolateSize(config.borderConfig.startSize, config.borderConfig.endSize, 1000L * config.borderConfig.shrinkSecs);
				for (ServerPlayerEntity player : this.participants) {
					player.networkHandler.sendPacket(new WorldBorderS2CPacket(world.getWorldBorder(), WorldBorderS2CPacket.Type.LERP_SIZE));
				}
			}
		} else {
			long totalShrinkTime = config.borderConfig.shrinkSecs * 20L;

			if ((world.getTime() - shrinkStartTime) <= totalShrinkTime) {
				this.bar.tickActive(totalShrinkTime - (world.getTime() - shrinkStartTime), totalShrinkTime);
			} else {
				this.bar.setFinished();
			}
		}

		if (world.getTime() > this.gameCloseTick) {
			this.world.close(GameCloseReason.FINISHED);
		}
	}

	private ActionResult onPlayerDeath(ServerPlayerEntity player, DamageSource source) {
		this.eliminatePlayer(player);
		return ActionResult.SUCCESS;
	}

	private void eliminatePlayer(ServerPlayerEntity player) {
		Text message = player.getDisplayName().shallowCopy().append(" has been eliminated!")
				.formatted(Formatting.RED);

		PlayerSet players = this.world.getPlayers();
		players.sendMessage(message);
		players.sendSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);

		ItemScatterer.spawn(this.world.getWorld(), player.getBlockPos(), player.inventory);

		this.spawnSpectator(player);

		// TODO: fix this aaaaa
		int survival = 0;
		for (ServerPlayerEntity participant : this.participants) {
			if (participant.interactionManager.getGameMode().isSurvivalLike()) {
				survival++;
			}
		}

		if (survival == 1) {
			for (ServerPlayerEntity participant : this.participants) {
				if (participant.interactionManager.getGameMode().isSurvivalLike()) {
					players.sendMessage(new LiteralText(player.getEntityName() + " won!"));
					this.gameCloseTick = this.world.getWorld().getTime() + (20 * 10);
					break;
				}
			}
		}
	}

	private void spawnSpectator(ServerPlayerEntity player) {
		this.spawnLogic.resetPlayer(player, GameMode.SPECTATOR);
		this.spawnLogic.spawnPlayerAtCenter(player);
	}

	private ActionResult onBreakBlock(ServerPlayerEntity player, BlockPos pos) {
		ServerWorld world = player.getServerWorld();
		BlockState state = world.getBlockState(pos);

		if (state.isIn(BlockTags.LOGS) && !player.isSneaking()) {
			Set<BlockPos> logs = new HashSet<>();
			logs.add(pos);

			findLogs(world, pos, logs);

			for (BlockPos log : logs) {
				BlockState logState = world.getBlockState(log);
				world.breakBlock(log, false);

				world.spawnEntity(new ItemEntity(world, log.getX(), log.getY(), log.getZ(), new ItemStack(logState.getBlock())));
			}

			return ActionResult.FAIL;
		}

		return ActionResult.PASS;
	}

	private void findLogs(ServerWorld world, BlockPos pos, Set<BlockPos> logs) {
		for(int x = -1; x <= 1; x++) {
			for(int z = -1; z <= 1; z++) {
				for(int y = -1; y <= 1; y++) {
					BlockPos local = pos.add(x, y, z);
					BlockState state = world.getBlockState(local);

					if (!logs.contains(local)) {
						if (state.isIn(BlockTags.LOGS)) {
							logs.add(local);
							findLogs(world, local, logs);
						}
					}
				}
			}
		}
	}

	private ActionResult onUseBlock(ServerPlayerEntity playerEntity, Hand hand, BlockHitResult hitResult) {
		if (hitResult.getBlockPos().getY() >= 100) {
			return ActionResult.FAIL;
		}

		return ActionResult.PASS;
	}
}
