package supercoder79.survivalgames.game;

import java.util.Set;
import java.util.UUID;

import net.gegy1000.plasmid.game.Game;
import net.gegy1000.plasmid.game.JoinResult;
import net.gegy1000.plasmid.game.event.GameOpenListener;
import net.gegy1000.plasmid.game.event.GameTickListener;
import net.gegy1000.plasmid.game.event.OfferPlayerListener;
import net.gegy1000.plasmid.game.event.PlayerAddListener;
import net.gegy1000.plasmid.game.event.PlayerDamageListener;
import net.gegy1000.plasmid.game.event.PlayerDeathListener;
import net.gegy1000.plasmid.game.event.PlayerRejoinListener;
import net.gegy1000.plasmid.game.map.GameMap;
import net.gegy1000.plasmid.game.rule.GameRule;
import net.gegy1000.plasmid.game.rule.RuleResult;
import net.gegy1000.plasmid.util.ItemStackBuilder;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;

public class SurvivalGamesActive {
	private final GameMap map;
	private final SurvivalGamesConfig config;

	private final Set<UUID> participants;

	private final SurvivalGamesSpawnLogic spawnLogic;

	private SurvivalGamesActive(GameMap map, SurvivalGamesConfig config, Set<UUID> participants) {
		this.map = map;
		this.config = config;
		this.participants = participants;

		this.spawnLogic = new SurvivalGamesSpawnLogic(map);
	}

	public static Game open(GameMap map, SurvivalGamesConfig config, Set<UUID> participants) {
		SurvivalGamesActive active = new SurvivalGamesActive(map, config, participants);

		Game.Builder builder = Game.builder();
		builder.setMap(map);

		builder.setRule(GameRule.ALLOW_CRAFTING, RuleResult.ALLOW);
		builder.setRule(GameRule.ALLOW_PORTALS, RuleResult.DENY);
		builder.setRule(GameRule.ALLOW_PVP, RuleResult.ALLOW);
		builder.setRule(GameRule.BLOCK_DROPS, RuleResult.ALLOW);
		builder.setRule(GameRule.FALL_DAMAGE, RuleResult.ALLOW);
		builder.setRule(GameRule.ENABLE_HUNGER, RuleResult.DENY);

		builder.on(GameOpenListener.EVENT, active::open);

		builder.on(OfferPlayerListener.EVENT, (game, player) -> JoinResult.ok());
		builder.on(PlayerAddListener.EVENT, active::addPlayer);

		builder.on(GameTickListener.EVENT, active::tick);

		builder.on(PlayerDamageListener.EVENT, active::onPlayerDamage);
		builder.on(PlayerDeathListener.EVENT, active::onPlayerDeath);
		builder.on(PlayerRejoinListener.EVENT, active::rejoinPlayer);

		return builder.build();
	}

	private void open(Game game) {
		ServerWorld world = game.getWorld();
		for (UUID playerId : this.participants) {
			ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUuid(playerId);
			if (player != null) {
				this.spawnParticipant(player);
			}
		}
	}

	private void addPlayer(Game game, ServerPlayerEntity player) {
		if (!this.participants.contains(player.getUuid())) {
			this.spawnSpectator(player);
		}
	}

	private void rejoinPlayer(Game game, ServerPlayerEntity player) {
		this.spawnSpectator(player);
	}

	private void tick(Game game) {
	}

	private boolean onPlayerDamage(Game game, ServerPlayerEntity player, DamageSource source, float amount) {
		//TODO: send chat events for damage
		return true;
	}

	private boolean onPlayerDeath(Game game, ServerPlayerEntity player, DamageSource source) {
		this.eliminatePlayer(game, player);
		return true;
	}

	private void spawnParticipant(ServerPlayerEntity player) {
		this.spawnLogic.resetPlayer(player, GameMode.ADVENTURE);
		this.spawnLogic.spawnPlayer(player);

		//TODO: kit customization
	}

	private void eliminatePlayer(Game game, ServerPlayerEntity player) {
		Text message = player.getDisplayName().shallowCopy().append(" has been eliminated!")
				.formatted(Formatting.RED);

		this.broadcastMessage(game, message);
		this.broadcastSound(game, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);

		this.spawnSpectator(player);
	}

	private void spawnSpectator(ServerPlayerEntity player) {
		this.spawnLogic.resetPlayer(player, GameMode.SPECTATOR);
		this.spawnLogic.spawnPlayer(player);
	}

	private void broadcastMessage(Game game, Text message) {
		ServerWorld world = game.getWorld();
		for (UUID playerId : game.getPlayers()) {
			ServerPlayerEntity otherPlayer = (ServerPlayerEntity) world.getPlayerByUuid(playerId);
			if (otherPlayer != null) {
				otherPlayer.sendMessage(message, false);
			}
		}
	}

	private void broadcastSound(Game game, SoundEvent sound) {
		ServerWorld world = game.getWorld();
		for (UUID playerId : game.getPlayers()) {
			ServerPlayerEntity otherPlayer = (ServerPlayerEntity) world.getPlayerByUuid(playerId);
			if (otherPlayer != null) {
				otherPlayer.playSound(sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		}
	}
}
