package supercoder79.survivalgames;

import net.gegy1000.plasmid.game.GameType;
import net.gegy1000.plasmid.game.config.GameMapConfig;
import net.gegy1000.plasmid.game.map.provider.MapProvider;
import supercoder79.survivalgames.game.SurvivalGamesConfig;
import supercoder79.survivalgames.game.SurvivalGamesWaiting;
import supercoder79.survivalgames.game.map.SurvivalGamesMapProvider;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import net.fabricmc.api.ModInitializer;

public class SurvivalGames implements ModInitializer {
	public static final GameType<SurvivalGamesConfig> TYPE = GameType.register(
			new Identifier("survivalgames", "survivalgames"),
			(server, config) -> {
				GameMapConfig<SurvivalGamesConfig> mapConfig = config.getMapConfig();
				RegistryKey<World> dimension = mapConfig.getDimension();
				BlockPos origin = mapConfig.getOrigin();
				ServerWorld world = server.getWorld(dimension);

				return mapConfig.getProvider().createAt(world, origin, config).thenApply(map -> SurvivalGamesWaiting.open(map, config));
			},
			SurvivalGamesConfig.CODEC
	);

	@Override
	public void onInitialize() {
		MapProvider.REGISTRY.register(new Identifier("survivalgames", "survivalgames"), SurvivalGamesMapProvider.CODEC);
	}
}
