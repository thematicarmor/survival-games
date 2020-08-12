package supercoder79.survivalgames.game;

import java.util.Set;
import java.util.stream.Collectors;

import supercoder79.survivalgames.game.config.SurvivalGamesConfig;
import xyz.nucleoid.plasmid.game.GameWorld;
import xyz.nucleoid.plasmid.game.event.GameOpenListener;
import xyz.nucleoid.plasmid.game.event.GameTickListener;
import xyz.nucleoid.plasmid.game.event.OfferPlayerListener;
import xyz.nucleoid.plasmid.game.event.PlayerAddListener;
import xyz.nucleoid.plasmid.game.event.PlayerDeathListener;
import xyz.nucleoid.plasmid.game.player.JoinResult;
import xyz.nucleoid.plasmid.game.rule.GameRule;
import xyz.nucleoid.plasmid.game.rule.RuleResult;
import xyz.nucleoid.plasmid.util.PlayerRef;
import supercoder79.survivalgames.game.map.SurvivalGamesMap;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.WorldBorderS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.GameMode;

public class SurvivalGamesActive {
	private final GameWorld world;
	private final SurvivalGamesMap map;
	private final SurvivalGamesConfig config;

	private final Set<PlayerRef> participants;

	private final SurvivalGamesSpawnLogic spawnLogic;
	private long startTime;
	private boolean borderShrinkStarted = false;

	private SurvivalGamesActive(GameWorld world, SurvivalGamesMap map, SurvivalGamesConfig config, Set<PlayerRef> participants) {
		this.world = world;
		this.map = map;
		this.config = config;
		this.participants = participants;

		this.spawnLogic = new SurvivalGamesSpawnLogic(world, config);
	}

	public static void open(GameWorld world, SurvivalGamesMap map, SurvivalGamesConfig config) {
		Set<PlayerRef> participants = world.getPlayers().stream()
				.map(PlayerRef::of)
				.collect(Collectors.toSet());

		SurvivalGamesActive active = new SurvivalGamesActive(world, map, config, participants);

		world.openGame(game -> {
			game.setRule(GameRule.CRAFTING, RuleResult.ALLOW);
			game.setRule(GameRule.PORTALS, RuleResult.DENY);
			game.setRule(GameRule.PVP, RuleResult.ALLOW);
			game.setRule(GameRule.BLOCK_DROPS, RuleResult.ALLOW);
			game.setRule(GameRule.FALL_DAMAGE, RuleResult.ALLOW);
			game.setRule(GameRule.HUNGER, RuleResult.DENY);

			game.on(GameOpenListener.EVENT, active::open);

			game.on(OfferPlayerListener.EVENT, player -> JoinResult.ok());
			game.on(PlayerAddListener.EVENT, active::addPlayer);

			game.on(GameTickListener.EVENT, active::tick);

			game.on(PlayerDeathListener.EVENT, active::onPlayerDeath);
		});
	}

	private void open() {
		ServerWorld world = this.world.getWorld();

		// World border stuff
		world.getWorldBorder().setCenter(0, 0);
		world.getWorldBorder().setSize(config.borderConfig.startSize);
		world.getWorldBorder().setDamagePerBlock(0.5);
		startTime = world.getTime();

		for (PlayerRef playerId : this.participants) {
			ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUuid(playerId.getId());
			if (player != null) {
				this.spawnParticipant(player);
				player.networkHandler.sendPacket(new WorldBorderS2CPacket(world.getWorldBorder(), WorldBorderS2CPacket.Type.SET_CENTER));
				player.networkHandler.sendPacket(new WorldBorderS2CPacket(world.getWorldBorder(), WorldBorderS2CPacket.Type.SET_SIZE));
				//TODO: check if this works
				player.setCustomName(new LiteralText(""));
			}
		}
	}

	private void addPlayer(ServerPlayerEntity player) {
		if (!this.participants.contains(PlayerRef.of(player))) {
			this.spawnSpectator(player);
		}
	}

	private void tick() {
		if (!this.borderShrinkStarted) {
			ServerWorld world = this.world.getWorld();
			if ((world.getTime() - startTime) > config.borderConfig.safeSecs * 20) {
				borderShrinkStarted = true;
				world.getWorldBorder().interpolateSize(config.borderConfig.startSize, config.borderConfig.endSize, 1000 * config.borderConfig.shrinkSecs);
				for (PlayerRef playerId : this.participants) {
					ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUuid(playerId.getId());
					if (player != null) {
						player.networkHandler.sendPacket(new WorldBorderS2CPacket(world.getWorldBorder(), WorldBorderS2CPacket.Type.LERP_SIZE));
					}
				}
			}
		}
	}

	private boolean onPlayerDeath(ServerPlayerEntity player, DamageSource source) {
		this.eliminatePlayer(player);
		return true;
	}

	private void spawnParticipant(ServerPlayerEntity player) {
		this.spawnLogic.resetPlayer(player, GameMode.SURVIVAL);
		this.spawnLogic.spawnPlayer(player);

		for (ItemStack stack : config.kit) {
			player.inventory.insertStack(stack);
		}
	}

	private void eliminatePlayer(ServerPlayerEntity player) {
		Text message = player.getDisplayName().shallowCopy().append(" has been eliminated!")
				.formatted(Formatting.RED);

		this.broadcastMessage(message);
		this.broadcastSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);

		ItemScatterer.spawn(this.world.getWorld(), player.getBlockPos(), player.inventory);

		this.spawnSpectator(player);
	}

	private void spawnSpectator(ServerPlayerEntity player) {
		this.spawnLogic.resetPlayer(player, GameMode.SPECTATOR);
		this.spawnLogic.spawnPlayer(player);
	}

	private void broadcastMessage(Text message) {
		for (ServerPlayerEntity player : this.world.getPlayers()) {
			player.sendMessage(message, false);
		}
	}

	private void broadcastSound(SoundEvent sound) {
		for (ServerPlayerEntity player : this.world.getPlayers()) {
			player.playSound(sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
	}
}
