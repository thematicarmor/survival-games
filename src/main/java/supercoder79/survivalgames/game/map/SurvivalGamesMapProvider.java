package supercoder79.survivalgames.game.map;

import java.util.concurrent.CompletableFuture;

import com.mojang.serialization.Codec;
import net.gegy1000.plasmid.game.map.GameMap;
import net.gegy1000.plasmid.game.map.GameMapBuilder;
import net.gegy1000.plasmid.game.map.provider.MapProvider;
import net.gegy1000.plasmid.world.BlockBounds;
import supercoder79.survivalgames.game.SurvivalGamesConfig;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class SurvivalGamesMapProvider implements MapProvider<SurvivalGamesConfig> {
	public static final Codec<SurvivalGamesMapProvider> CODEC = Codec.unit(new SurvivalGamesMapProvider());
	@Override
	public CompletableFuture<GameMap> createAt(ServerWorld world, BlockPos origin, SurvivalGamesConfig config) {
		BlockBounds bounds = new BlockBounds(
				new BlockPos(-10, 0, -10),
				new BlockPos(10, 1, 10)
		);

		GameMapBuilder builder = GameMapBuilder.open(world, origin, bounds);

		return CompletableFuture.supplyAsync(() -> {
			this.buildMap(builder);
			return builder.build();
		}, world.getServer());
	}

	@Override
	public Codec<? extends MapProvider<?>> getCodec() {
		return CODEC;
	}

	private void buildMap(GameMapBuilder builder) {
		for (int x = -10; x <= 10; x++) {
			for (int z = -10; z <= 10; z++) {
				builder.setBlockState(new BlockPos(x, 0, z), Blocks.STONE.getDefaultState());
			}
		}
	}
}
