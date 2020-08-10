package supercoder79.survivalgames.game.map;

import java.util.Random;

import kdotjpg.opensimplex.OpenSimplexNoise;
import net.gegy1000.plasmid.game.world.generator.GameChunkGenerator;
import supercoder79.survivalgames.game.map.gen.feature.DiskGen;
import supercoder79.survivalgames.game.map.gen.feature.GrassGen;
import supercoder79.survivalgames.game.map.gen.feature.PoplarTreeFeature;
import supercoder79.survivalgames.game.map.loot.LootHelper;
import supercoder79.survivalgames.game.map.loot.LootProviders;
import supercoder79.survivalgames.noise.WorleyNoise;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;

public class SurvivalGamesChunkGenerator extends GameChunkGenerator {
	private final OpenSimplexNoise baseNoise;
	private final OpenSimplexNoise interpolationNoise;
	private final OpenSimplexNoise lowerInterpolatedNoise;
	private final OpenSimplexNoise upperInterpolatedNoise;
	private final OpenSimplexNoise detailNoise;

	private final OpenSimplexNoise treeDensityNoise;

	private final WorleyNoise structureNoise;
	private final WorleyNoise chestNoise;
	public SurvivalGamesChunkGenerator() {
		Random random = new Random();
		baseNoise = new OpenSimplexNoise(random.nextLong());
		interpolationNoise = new OpenSimplexNoise(random.nextLong());
		lowerInterpolatedNoise = new OpenSimplexNoise(random.nextLong());
		upperInterpolatedNoise = new OpenSimplexNoise(random.nextLong());
		detailNoise = new OpenSimplexNoise(random.nextLong());

		treeDensityNoise = new OpenSimplexNoise(random.nextLong());

		structureNoise = new WorleyNoise(random.nextLong());
		chestNoise = new WorleyNoise(random.nextLong());
	}

	@Override
	public void populateNoise(WorldAccess world, StructureAccessor structures, Chunk chunk) {
		int chunkX = chunk.getPos().x * 16;
		int chunkZ = chunk.getPos().z * 16;

		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for (int x = chunkX; x < chunkX + 16; x++) {
		    for (int z = chunkZ; z < chunkZ + 16; z++) {
				// Create base terrain
				double noise = baseNoise.eval(x / 256.0, z / 256.0);
				noise *= noise > 0 ? 14 : 12;

				// Add hills in a similar method to mc interpolation noise
				double lerp = interpolationNoise.eval(x / 50.0, z / 50.0) * 2.5;
				if (lerp > 1) {
					double upperNoise = upperInterpolatedNoise.eval(x / 60.0, z / 60.0);
					upperNoise *= upperNoise > 0 ? 12 : 8;
					noise += upperNoise;
				} else if (lerp < 0) {
					double lowerNoise = lowerInterpolatedNoise.eval(x / 60.0, z / 60.0);
					lowerNoise *= lowerNoise > 0 ? 8 : 6;
					noise += lowerNoise;
				} else {
					double upperNoise = upperInterpolatedNoise.eval(x / 60.0, z / 60.0);
					upperNoise *= upperNoise > 0 ? 12 : 8;

					double lowerNoise = lowerInterpolatedNoise.eval(x / 60.0, z / 60.0);
					lowerNoise *= lowerNoise > 0 ? 8 : 6;

					noise += MathHelper.lerp(lerp, lowerNoise, upperNoise);
				}

				// Add small details to make the terrain less rounded
				noise += detailNoise.eval(x / 20.0, z / 20.0) * 3.25;

				int height = (int) (56 + noise);

				// Generation height ensures that the generator interates up to at least the water level.
				int genHeight = Math.max(height, 48);
				for (int y = 0; y <= genHeight; y++) {
					// Simple surface building
					BlockState state = Blocks.STONE.getDefaultState();
					if (y == height) {
						// If the height and the generation height are the same, it means that we're on land
						if (height == genHeight) {
							state = Blocks.GRASS_BLOCK.getDefaultState();
						} else {
							// height and genHeight are different, so we're under water. Place dirt instead of grass.
							state = Blocks.DIRT.getDefaultState();
						}
					} else if ((height - y) <= 3) { //TODO: biome controls under depth
						state = Blocks.DIRT.getDefaultState();
					} else if (y == 0) {
						state = Blocks.BEDROCK.getDefaultState();
					}

					// If the y is higher than the land height, then we must place water
					if (y > height) {
						state = Blocks.WATER.getDefaultState();
					}

					// Set the state here
					chunk.setBlockState(mutable.set(x, y, z), state, false);
				}
			}
		}
	}

	@Override
	public void generateFeatures(ChunkRegion region, StructureAccessor structures) {
		int chunkX = region.getCenterChunkX() * 16;
		int chunkZ = region.getCenterChunkZ() * 16;
		Random random = new Random();

		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for (int x = chunkX; x < chunkX + 16; x++) {
			for (int z = chunkZ; z < chunkZ + 16; z++) {
				int y = region.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, x, z);

				if (y > 48) {
					int treeDensity = (int) ((treeDensityNoise.eval(x / 180.0, z / 180.0) + 1) * 64);
					if (random.nextInt(96 + treeDensity) == 0) {
						PoplarTreeFeature.INSTANCE.generate(region, mutable.set(x, y, z).toImmutable(), random);
					}

					if (random.nextInt(16) == 0) {
						GrassGen.INSTANCE.generate(region, mutable.set(x, y, z).toImmutable(), random);
					}

					if (random.nextInt(4096) == 0) {
						LootHelper.placeProviderChest(region, mutable.set(x, y, z).toImmutable(), LootProviders.TEMP_POOl.pickRandom(random));
					}

					if (random.nextInt(16384) == 0) {
						if (region.getBlockState(mutable.set(x, y - 1, z)).isOf(Blocks.GRASS_BLOCK)) {
							region.setBlockState(mutable.set(x, y, z), Blocks.ENCHANTING_TABLE.getDefaultState(), 3);
						}
					}
				} else {
					if (random.nextInt(64) == 0) {
						DiskGen.INSTANCE.generate(region, mutable.set(x, y, z).toImmutable(), random);
					}
				}
			}
		}
	}
}
