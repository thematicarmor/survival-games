package supercoder79.survivalgames.game.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import kdotjpg.opensimplex.OpenSimplexNoise;
import net.gegy1000.plasmid.game.map.GameMap;
import net.gegy1000.plasmid.game.map.GameMapBuilder;
import net.gegy1000.plasmid.game.map.provider.MapProvider;
import net.gegy1000.plasmid.world.BlockBounds;
import supercoder79.survivalgames.game.SurvivalGamesConfig;
import supercoder79.survivalgames.game.map.gen.biome.BiomeGen;
import supercoder79.survivalgames.game.map.gen.biome.BiomeProvider;
import supercoder79.survivalgames.game.map.gen.biome.Biomes;
import supercoder79.survivalgames.game.map.gen.biome.DefaultBiomeProvider;
import supercoder79.survivalgames.game.map.gen.feature.AspenTreeGen;
import supercoder79.survivalgames.game.map.gen.feature.GrassGen;
import supercoder79.survivalgames.game.map.gen.feature.PoplarTreeGen;
import supercoder79.survivalgames.game.map.gen.structure.EnchantingTableStructure;
import supercoder79.survivalgames.game.map.gen.structure.FarmlandPatchStructure;
import supercoder79.survivalgames.game.map.gen.structure.HouseStructure;
import supercoder79.survivalgames.game.map.gen.structure.OrePileStructure;
import supercoder79.survivalgames.game.map.gen.structure.StructureGen;
import supercoder79.survivalgames.game.map.gen.structure.TowerStructure;
import supercoder79.survivalgames.game.map.loot.LootHelper;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import supercoder79.survivalgames.game.map.loot.LootProviderEntry;
import supercoder79.survivalgames.game.map.loot.LootProviders;
import supercoder79.survivalgames.noise.WorleyNoise;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SurvivalGamesMapProvider implements MapProvider<SurvivalGamesConfig> {
	public static final WeightedList<Function<BlockPos, StructureGen>> STRUCTURE_POOl = new WeightedList<Function<BlockPos, StructureGen>>()
			.add(HouseStructure::new, 1)
			.add(EnchantingTableStructure::new, 1)
			.add(TowerStructure::new, 1)
			.add(OrePileStructure::new, 1)
			.add(FarmlandPatchStructure::new, 1);

	public static final Codec<SurvivalGamesMapProvider> CODEC = Codec.unit(new SurvivalGamesMapProvider());

	@Override
	public CompletableFuture<GameMap> createAt(ServerWorld world, BlockPos origin, SurvivalGamesConfig config) {
		BlockBounds bounds = new BlockBounds(
				new BlockPos(-256, 0, -256),
				new BlockPos(256, 120, 256)
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
		Random random = new Random();

		//TODO: codec to choose
		BiomeProvider biomeProvider = new DefaultBiomeProvider();
		biomeProvider.initialize(random);

		for (BiomeGen biome : Biomes.BIOMES) {
			biome.setupSeed(random);
		}

		BlockPos.Mutable mutable = new BlockPos.Mutable();
		// TODO: config option for this
		// Clean up map for testing purposes, faster than waiting for TACS >:(
		for (int x = -280; x <= 280; x++) {
			for (int z = -280; z <= 280; z++) {
				for (int y = 0; y < 256; y++) {
					builder.setBlockState(mutable.set(x, y, z), Blocks.AIR.getDefaultState(), false);
				}
			}
		}

		// TODO: remove all item entities from map

		OpenSimplexNoise baseNoise = new OpenSimplexNoise(random.nextLong());
		OpenSimplexNoise interpolationNoise = new OpenSimplexNoise(random.nextLong());
		OpenSimplexNoise lowerInterpolatedNoise = new OpenSimplexNoise(random.nextLong());
		OpenSimplexNoise upperInterpolatedNoise = new OpenSimplexNoise(random.nextLong());
		OpenSimplexNoise detailNoise = new OpenSimplexNoise(random.nextLong());
		WorleyNoise structureNoise = new WorleyNoise(random.nextLong());
		WorleyNoise chestNoise = new WorleyNoise(random.nextLong());

		System.out.println("Generating terrain...");
		long time = System.currentTimeMillis();

		List<BlockPos> structureStarts = new CopyOnWriteArrayList<>();
		List<BlockPos> lootChests = new CopyOnWriteArrayList<>();
		int[] heightmap = new int[513 * 513];
		for (int x = -256; x <= 256; x++) {
			for (int z = -256; z <= 256; z++) {
				double baseFactor = 0;
				double lowerFactor = 0;
				double upperFactor = 0;
				double weight = 0;

				BiomeGen center = biomeProvider.get(x, z);

				// Interpolate biome data
				for (int bX = -2; bX <= 2; bX++) {
				    for (int bZ = -2; bZ <= 2; bZ++) {
				    	BiomeGen biome = biomeProvider.get(x + bX, z + bZ);
				    	baseFactor += biome.baseHeightFactor();
						lowerFactor += biome.lowerHeightFactor();
						upperFactor += biome.upperHeightFactor();

						weight++;
				    }
				}
				baseFactor /= weight;
				lowerFactor /= weight;
				upperFactor /= weight;

				// Create base terrain
				double noise = baseNoise.eval(x / 256.0, z / 256.0) * baseFactor;

				// Add hills in a similar method to mc interpolation noise
				double lerp = interpolationNoise.eval(x / 50.0, z / 50.0) * 2.5;
				if (lerp > 1) {
					noise += upperInterpolatedNoise.eval(x / 60.0, z / 60.0) * upperFactor;
				} else if (lerp < 0) {
					noise += lowerInterpolatedNoise.eval(x / 60.0, z / 60.0) * lowerFactor;
				} else {
					double upperNoise = upperInterpolatedNoise.eval(x / 60.0, z / 60.0) * upperFactor;
					double lowerNoise = lowerInterpolatedNoise.eval(x / 60.0, z / 60.0) * lowerFactor;
					noise += MathHelper.lerp(lerp, lowerNoise, upperNoise);
				}

				// Add small details to make the terrain less rounded
				noise += detailNoise.eval(x / 20.0, z / 20.0) * 3.25;

				int height = (int) (56 + noise);
				heightmap[((x + 256) * 512) + (z + 256)] = height;

				// Get extent data from voronoi noises
				double structureExtent = structureNoise.sample(x / 120.0, z / 120.0);
				double chestExtent = chestNoise.sample(x / 45.0, z / 45.0);

				// Generation height ensures that the generator interates up to at least the water level.
				int genHeight = Math.max(height, 48);
				for (int y = 0; y <= genHeight; y++) {
					// Simple surface building
					BlockState state = Blocks.STONE.getDefaultState();
					if (y == height) {
						// If the height and the generation height are the same, it means that we're on land
						if (height == genHeight) {
							state = center.topState(x, z, random);

							// Add a chest if the chest noise is low enough
							if (chestExtent < 0.01) {
								lootChests.add(mutable.set(x, y + 1, z).toImmutable());
							}

							// If the structure start noise is low enough, place a structure
							if (structureExtent < 0.005) {
								structureStarts.add(mutable.set(x, y, z).toImmutable());
							}
						} else {
							// height and genHeight are different, so we're under water. Place dirt instead of grass.
							state = center.underwaterState(x, z, random);
						}
					} else if ((height - y) <= 3) { //TODO: biome controls under depth
						state = center.underState(x, z, random);
					} else if (y == 0) {
						state = Blocks.BEDROCK.getDefaultState();
					}

					// If the y is higher than the land height, then we must place water
					if (y > height) {
						state = Blocks.WATER.getDefaultState();
					}

					// Set the state here
					builder.setBlockState(mutable.set(x, y, z), state, false);
				}
			}
		}

		// Pre-process structure data
		for (BlockPos pos : structureStarts) {
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					for (int y = -1; y <= 1; y++) {
						if (x == 0 && z == 0 && y == 0) {
							continue;
						}

						BlockPos local = pos.add(x, y, z);
						if (structureStarts.contains(local)) {
							structureStarts.remove(pos.add(x, y, z));
						}
					}
				}
			}
		}

		Long2ObjectOpenHashMap<LootProvider> structureLootRefs = new Long2ObjectOpenHashMap<>();
		structureLootRefs.defaultReturnValue(LootProviders.GENERIC);

		System.out.println("Generating structures!");
		for (BlockPos pos : structureStarts) {
			StructureGen structure = STRUCTURE_POOl.pickRandom(random).apply(pos);

			structure.generate(builder);
			LootProvider provider = structure.getLootProvider();

			for (int x = -48; x <= 48; x++) {
			    for (int z = -48; z <= 48; z++) {
					structureLootRefs.put(BlockPos.asLong(pos.getX() + x, 0, pos.getZ() + z), provider);
			    }
			}
		}

		System.out.println("Generating chests!");
		for (BlockPos pos : lootChests) {
			Map<LootProvider, Integer> providerCounts = new HashMap<>();

			for (int x = -48; x <= 48; x++) {
				for (int z = -48; z <= 48; z++) {
					LootProvider provider = structureLootRefs.get(BlockPos.asLong(pos.getX() + x, 0, pos.getZ() + z));
					int count = providerCounts.containsKey(provider) ? providerCounts.get(provider) + 1 : 1;
					providerCounts.put(provider, count);
				}
			}

			List<LootProviderEntry> entries = new ArrayList<>();
			for (Map.Entry<LootProvider, Integer> entry : providerCounts.entrySet()) {
				entries.add(new LootProviderEntry(entry.getKey(), entry.getValue()));
			}

			List<ItemStack> stacks = LootHelper.get(entries);
			builder.setBlockState(pos, Blocks.CHEST.getDefaultState(), false);
			ChestBlockEntity be = (ChestBlockEntity) builder.getBlockEntity(pos);

			for (ItemStack stack : stacks) {
				be.setStack(random.nextInt(27), stack);
			}
		}

		// Feature generation stack
		System.out.println("Generating features!");
		for (int x = -256; x <= 256; x++) {
			for (int z = -256; z <= 256; z++) {
				BiomeGen biome = biomeProvider.get(x, z);
				int y = heightmap[((x + 256) * 512) + (z + 256)] + 1;

				biome.treeAt(mutable.set(x, y, z), random).generate(builder);
				biome.grassAt(mutable.set(x, y - 1, z), random).generate(builder);
			}
		}

		System.out.println("Propagating lighting!");
		for (int x = -16; x < 16; x++) {
		    for (int z = -16; z < 16; z++) {
				builder.getWorld().setBlockState(new BlockPos(x * 16, 255, z * 16), Blocks.AIR.getDefaultState());
		    }
		}

		System.out.println("Generated world in " + (System.currentTimeMillis() - time));
	}
}
