package supercoder79.survivalgames.game.map;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import kdotjpg.opensimplex.OpenSimplexNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;

import supercoder79.survivalgames.game.config.SurvivalGamesConfig;
import supercoder79.survivalgames.game.map.biome.BiomeGen;
import supercoder79.survivalgames.game.map.biome.FakeBiomeSource;
import supercoder79.survivalgames.game.map.gen.structure.ChunkBox;
import supercoder79.survivalgames.game.map.gen.structure.StructureGen;
import supercoder79.survivalgames.game.map.gen.structure.Structures;
import supercoder79.survivalgames.game.map.loot.LootHelper;
import supercoder79.survivalgames.game.map.loot.LootProviders;
import supercoder79.survivalgames.noise.WorleyNoise;
import xyz.nucleoid.plasmid.game.gen.feature.DiskGen;
import xyz.nucleoid.plasmid.game.gen.feature.GrassGen;
import xyz.nucleoid.plasmid.game.world.generator.GameChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.fabricmc.loader.api.FabricLoader;

public class SurvivalGamesChunkGenerator extends GameChunkGenerator {
	private final OpenSimplexNoise baseNoise;
	private final OpenSimplexNoise interpolationNoise;
	private final OpenSimplexNoise lowerInterpolatedNoise;
	private final OpenSimplexNoise upperInterpolatedNoise;
	private final OpenSimplexNoise detailNoise;

	private final OpenSimplexNoise treeDensityNoise;

	private final WorleyNoise structureNoise;
	private final WorleyNoise chestNoise;

	private final FakeBiomeSource biomeSource;

	private final Long2ObjectMap<List<PoolStructurePiece>> piecesByChunk;
	private final List<SurvivalGamesJigsawGenerator> jigsawGenerator;

	public SurvivalGamesChunkGenerator(MinecraftServer server, SurvivalGamesConfig config) {
		super(server);
		Random random = new Random();

		this.biomeSource = new FakeBiomeSource(server.getRegistryManager().get(Registry.BIOME_KEY), random.nextLong());

		this.baseNoise = new OpenSimplexNoise(random.nextLong());
		this.interpolationNoise = new OpenSimplexNoise(random.nextLong());
		this.lowerInterpolatedNoise = new OpenSimplexNoise(random.nextLong());
		this.upperInterpolatedNoise = new OpenSimplexNoise(random.nextLong());
		this.detailNoise = new OpenSimplexNoise(random.nextLong());

		this.treeDensityNoise = new OpenSimplexNoise(random.nextLong());

		this.structureNoise = new WorleyNoise(random.nextLong());
		this.chestNoise = new WorleyNoise(random.nextLong());

		this.piecesByChunk = new Long2ObjectOpenHashMap<>();
		this.piecesByChunk.defaultReturnValue(ImmutableList.of());

		List<SurvivalGamesJigsawGenerator> generators = new ArrayList<>();

		SurvivalGamesJigsawGenerator generator = new SurvivalGamesJigsawGenerator(server, this, piecesByChunk);
		generator.arrangePieces(new BlockPos(0, 64, 0), new Identifier("survivalgames", "starts"), 12);
		ChunkBox townArea = generator.getBox();
		generators.add(generator);

		for (int i = 0; i < 16; i++) {
			int startX = random.nextInt(config.borderConfig.startSize) - random.nextInt(config.borderConfig.startSize);
			int startZ = random.nextInt(config.borderConfig.startSize) - random.nextInt(config.borderConfig.startSize);

			if (townArea.isBlockIn(startX,startZ)) {
				continue;
			}

			SurvivalGamesJigsawGenerator outskirtGenerator = new SurvivalGamesJigsawGenerator(server, this, piecesByChunk);
			outskirtGenerator.arrangePieces(new BlockPos(startX, 0, startZ), new Identifier("survivalgames", "building"), 0);
			generators.add(outskirtGenerator);
		}

		this.jigsawGenerator = generators;

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			DebugJigsawMapper.map(this.piecesByChunk, townArea);
		}
	}

	@Override
	public void populateNoise(WorldAccess world, StructureAccessor structures, Chunk chunk) {
		int chunkX = chunk.getPos().x * 16;
		int chunkZ = chunk.getPos().z * 16;

		// To smooth into other chunks, we need to gather all of the nearby structures
		Set<PoolStructurePiece> pieces = new ObjectOpenHashSet<>();
		for(int x = -1; x <= 1; x++) {
		    for(int z = -1; z <= 1; z++) {
				pieces.addAll(this.piecesByChunk.get(new ChunkPos(chunk.getPos().x + x, chunk.getPos().z + z).toLong()));
		    }
		}

		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for (int x = chunkX; x < chunkX + 16; x++) {
		    for (int z = chunkZ; z < chunkZ + 16; z++) {
				double noise = getNoise(x, z);

				int height = (int) (56 + noise);

				// TODO: this is really slow and bad
				for (PoolStructurePiece piece : pieces) {
					BlockBox box = piece.getBoundingBox();
					if (piece.getPoolElement().getProjection() == StructurePool.Projection.RIGID) {
						// At structure: raise to level
						if (box.intersectsXZ(x, z, x, z )) {
							height = Math.max(48, piece.getPos().getY());
						} else if (box.intersectsXZ(x - 8, z - 8, x + 8, z + 8)) {
							// Within radius: smooth
							// I won't lie, I have no idea what I just wrote here.
							// It seems to work though so... it stays.

							double dx = Math.max(0, Math.max(box.minX - x, x - box.maxX)) / 8.0;
							double dz = Math.max(0, Math.max(box.minZ - z, z - box.maxZ)) / 8.0;
							double rad = dx * dx + dz * dz;

							height = (int) MathHelper.clampedLerp(height, piece.getPos().getY(), 1 - rad);
						}
					}
				}

				// Generation height ensures that the generator iterates up to at least the water level.
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

	private double getNoise(int x, int z) {
		double upperNoiseFactor = 0;
		double lowerNoiseFactor = 0;
		double upperLerpHigh = 0;
		double upperLerpLow = 0;
		double lowerLerpHigh = 0;
		double lowerLerpLow = 0;
		double detailFactor = 0;
		double weight = 0;

		for (int aX = -4; aX <= 4; aX++) {
			for (int aZ = -4; aZ < 4; aZ++) {
				BiomeGen biome = this.biomeSource.getRealBiome(x + aX, z + aZ);
				upperNoiseFactor += biome.upperNoiseFactor();
				lowerNoiseFactor += biome.lowerNoiseFactor();
				upperLerpHigh += biome.upperLerpHigh();
				upperLerpLow += biome.upperLerpLow();
				lowerLerpHigh += biome.lowerLerpHigh();
				lowerLerpLow += biome.lowerLerpLow();
				detailFactor += biome.detailFactor();

				weight++;
			}
		}

		upperNoiseFactor /= weight;
		lowerNoiseFactor /= weight;
		upperLerpHigh /= weight;
		upperLerpLow /= weight;
		lowerLerpHigh /= weight;
		lowerLerpLow /= weight;
		detailFactor /= weight;

		// Create base terrain
		double noise = baseNoise.eval(x / 256.0, z / 256.0);
		noise *= noise > 0 ? upperNoiseFactor : lowerNoiseFactor;

		// Add hills in a similar method to mc interpolation noise
		double lerp = interpolationNoise.eval(x / 50.0, z / 50.0) * 2.5;
		if (lerp > 1) {
			double upperNoise = upperInterpolatedNoise.eval(x / 60.0, z / 60.0);
			upperNoise *= upperNoise > 0 ? upperLerpHigh : upperLerpLow;
			noise += upperNoise;
		} else if (lerp < 0) {
			double lowerNoise = lowerInterpolatedNoise.eval(x / 60.0, z / 60.0);
			lowerNoise *= lowerNoise > 0 ? lowerLerpHigh : lowerLerpLow;
			noise += lowerNoise;
		} else {
			double upperNoise = upperInterpolatedNoise.eval(x / 60.0, z / 60.0);
			upperNoise *= upperNoise > 0 ? upperLerpHigh : upperLerpLow;

			double lowerNoise = lowerInterpolatedNoise.eval(x / 60.0, z / 60.0);
			lowerNoise *= lowerNoise > 0 ? lowerLerpHigh : lowerLerpLow;

			noise += MathHelper.lerp(lerp, lowerNoise, upperNoise);
		}

		// Add small details to make the terrain less rounded
		noise += detailNoise.eval(x / 20.0, z / 20.0) * detailFactor;

		return noise;
	}

	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmapType) {
		int height = (int) (56 + getNoise(x, z));
		return Math.max(height, 48);
	}

	public void generateJigsaws(ChunkRegion region, StructureAccessor structures) {
		ChunkPos chunkPos = new ChunkPos(region.getCenterChunkX(), region.getCenterChunkZ());
		List<PoolStructurePiece> pieces = this.piecesByChunk.get(chunkPos.toLong());

		if (pieces != null) {
			// generate all intersecting pieces with the mask of this chunk
			BlockBox chunkMask = new BlockBox(chunkPos.getStartX(), 0, chunkPos.getStartZ(), chunkPos.getEndX(), 255, chunkPos.getEndZ());
			for (PoolStructurePiece piece : pieces) {
				piece.method_27236(region, structures, this, new Random(), chunkMask, BlockPos.ORIGIN, false);
			}
		}
	}

	@Override
	public void generateFeatures(ChunkRegion region, StructureAccessor structures) {
		generateJigsaws(region, structures);

		int chunkX = region.getCenterChunkX() * 16;
		int chunkZ = region.getCenterChunkZ() * 16;
		Random random = new Random();

		BlockPos.Mutable mutable = new BlockPos.Mutable();
		boolean spawnedStructure = false;

		for (int x = chunkX; x < chunkX + 16; x++) {
			for (int z = chunkZ; z < chunkZ + 16; z++) {
				BiomeGen biome = this.biomeSource.getRealBiome(x, z);

				int y = region.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, x, z);

				if (y > 48) {
					if (this.chestNoise.sample(x / 45.0, z / 45.0) < 0.01) {
						LootHelper.placeProviderChest(region, mutable.set(x, y, z).toImmutable(), LootProviders.GENERIC);
					}

					if (!spawnedStructure && this.structureNoise.sample(x / 120.0, z / 120.0) < 0.005) {
						spawnedStructure = true;

						StructureGen structure = Structures.POOL.pickRandom(random);
						structure.generate(region, mutable.set(x, y, z).toImmutable(), random);

						for (int i = 0; i < structure.nearbyChestCount(random); i++) {
							int ax = x + (random.nextInt(16) - random.nextInt(16));
							int az = z + (random.nextInt(16) - random.nextInt(16));
							int ay = region.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, ax, az);

							LootHelper.placeProviderChest(region, mutable.set(ax, ay, az).toImmutable(), structure.getLootProvider());
						}
					}

					y = region.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, x, z);

					int treeDensity = (int) biome.modifyTreeCount((treeDensityNoise.eval(x / 180.0, z / 180.0) + 1) * 64);

					if (random.nextInt(96 + treeDensity) == 0) {
						biome.tree(x, z, random).generate(region, mutable.set(x, y, z).toImmutable(), random);
					}

					if (random.nextInt(16) == 0) {
						GrassGen.INSTANCE.generate(region, mutable.set(x, y, z).toImmutable(), random);
					}
				} else {
					if (random.nextInt(64) == 0) {
						DiskGen.INSTANCE.generate(region, mutable.set(x, y, z).toImmutable(), random);
					}
				}
			}
		}
	}

	@Override
	public void populateBiomes(Registry<Biome> registry, Chunk chunk) {
		ChunkPos chunkPos = chunk.getPos();
		((ProtoChunk)chunk).setBiomes(new BiomeArray(registry, chunkPos, this.biomeSource));
	}

	@Override
	public void carve(long seed, BiomeAccess access, Chunk chunk, GenerationStep.Carver carver) {
	}
}
