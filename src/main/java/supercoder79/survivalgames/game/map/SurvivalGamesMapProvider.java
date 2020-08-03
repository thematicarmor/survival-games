package supercoder79.survivalgames.game.map;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import com.mojang.serialization.Codec;
import kdotjpg.opensimplex.OpenSimplexNoise;
import net.gegy1000.plasmid.game.map.GameMap;
import net.gegy1000.plasmid.game.map.GameMapBuilder;
import net.gegy1000.plasmid.game.map.provider.MapProvider;
import net.gegy1000.plasmid.world.BlockBounds;
import supercoder79.survivalgames.game.SurvivalGamesConfig;
import supercoder79.survivalgames.game.map.gen.AspenTreeGen;
import supercoder79.survivalgames.game.map.gen.GrassGen;
import supercoder79.survivalgames.game.map.gen.PoplarTreeGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SurvivalGamesMapProvider implements MapProvider<SurvivalGamesConfig> {
	public static final Codec<SurvivalGamesMapProvider> CODEC = Codec.unit(new SurvivalGamesMapProvider());
	@Override
	public CompletableFuture<GameMap> createAt(ServerWorld world, BlockPos origin, SurvivalGamesConfig config) {
		BlockBounds bounds = new BlockBounds(
				new BlockPos(-512, 0, -512),
				new BlockPos(512, 120, 512)
		);

		GameMapBuilder builder = GameMapBuilder.open(world, origin, bounds);
		this.buildMap(builder);

		return CompletableFuture.supplyAsync(builder::build, world.getServer());
	}

	@Override
	public Codec<? extends MapProvider<?>> getCodec() {
		return CODEC;
	}

	private void buildMap(GameMapBuilder builder) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		// TODO: config option for this
		// Clean up map for testing purposes, faster than waiting for TACS >:(
		for (int x = -280; x <= 280; x++) {
			for (int z = -280; z <= 280; z++) {
				for (int y = 0; y < 256; y++) {
					builder.setBlockState(mutable.set(x, y, z), Blocks.AIR.getDefaultState(), false);
				}
			}

			System.out.println((x + 280) / 540.0);
		}

		Random random = new Random();
		OpenSimplexNoise interpolationNoise = new OpenSimplexNoise(random.nextLong());
		OpenSimplexNoise lowerInterpolatedNoise = new OpenSimplexNoise(random.nextLong());
		OpenSimplexNoise upperInterpolatedNoise = new OpenSimplexNoise(random.nextLong());
		OpenSimplexNoise detailNoise = new OpenSimplexNoise(random.nextLong());

		System.out.println("Generating terrain...");
		long time = System.currentTimeMillis();

		int[] heightmap = new int[513 * 513];
		for (int x = -256; x <= 256; x++) {
			for (int z = -256; z <= 256; z++) {

				// A similar method to MC interpolation noise
				double lerp = interpolationNoise.eval(x / 50.0, z / 50.0) * 2.5;
				double noise;
				if (lerp > 1) {
					noise = upperInterpolatedNoise.eval(x / 60.0, z / 60.0) * 12;
				} else if (lerp < 0) {
					noise = lowerInterpolatedNoise.eval(x / 60.0, z / 60.0) * 8;
				} else {
					double upperNoise = upperInterpolatedNoise.eval(x / 60.0, z / 60.0) * 12;
					double lowerNoise = lowerInterpolatedNoise.eval(x / 60.0, z / 60.0) * 8;
					noise = MathHelper.lerp(lerp, lowerNoise, upperNoise);
				}

				noise += detailNoise.eval(x / 20.0, z / 20.0) * 3;

				int height = 60 + (int)noise;
				heightmap[((x + 256) * 512) + (z + 256)] = height;

				for (int y = 0; y <= height; y++) {
					// Simple surface building
					BlockState state = Blocks.STONE.getDefaultState();
					if (y == height) {
						state = Blocks.GRASS_BLOCK.getDefaultState();
					} else if ((height - y) <= 3) {
						state = Blocks.DIRT.getDefaultState();
					}

					builder.setBlockState(mutable.set(x, y, z), state, false);
				}
			}

			System.out.println((x + 256) / 512.0);
		}

		// Feature generation stack
		System.out.println("Generating features!");
		OpenSimplexNoise treeGenMask = new OpenSimplexNoise(random.nextLong());
		OpenSimplexNoise treeDensity = new OpenSimplexNoise(random.nextLong());
		OpenSimplexNoise treeType = new OpenSimplexNoise(random.nextLong());
		for (int x = -256; x <= 256; x++) {
			for (int z = -256; z <= 256; z++) {
				// Generate trees in certain areas
				int y = heightmap[((x + 256) * 512) + (z + 256)];
				
				if (treeGenMask.eval(x / 80.0, z / 80.0) > 0) {
					if (random.nextInt(80 + (int) (treeDensity.eval(x / 45.0, z / 45.0) * 30)) == 0) {
						double typeNoise = treeType.eval(x / 120.0, z / 120.0) * 2.5;
						if (typeNoise > 1) {
							new PoplarTreeGen(mutable.set(x, y, z)).generate(builder);
						} else if (typeNoise < 0) {
							new AspenTreeGen(mutable.set(x, y, z)).generate(builder);
						} else {
							// Create tree gradient
							if (random.nextDouble() < typeNoise) {
								new PoplarTreeGen(mutable.set(x, y, z)).generate(builder);
							} else {
								new AspenTreeGen(mutable.set(x, y, z)).generate(builder);
							}
						}
					}
				}

				if (random.nextInt(12) == 0) {
					new GrassGen(mutable.set(x, y, z)).generate(builder);
				}
			}
		}
		System.out.println("Generated world in " + (System.currentTimeMillis() - time));
	}
}
