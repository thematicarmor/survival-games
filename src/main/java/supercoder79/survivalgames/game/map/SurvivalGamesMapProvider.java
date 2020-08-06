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
import supercoder79.survivalgames.game.map.gen.AspenTreeGen;
import supercoder79.survivalgames.game.map.gen.GrassGen;
import supercoder79.survivalgames.game.map.gen.PoplarTreeGen;
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

		Random random = new Random();
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

				// A similar method to MC interpolation noise
				double lerp = interpolationNoise.eval(x / 50.0, z / 50.0) * 2.5;
				double noise = baseNoise.eval(x / 256.0, z / 256.0) * 16;
				if (lerp > 1) {
					noise += upperInterpolatedNoise.eval(x / 60.0, z / 60.0) * 16;
				} else if (lerp < 0) {
					noise += lowerInterpolatedNoise.eval(x / 60.0, z / 60.0) * 10;
				} else {
					double upperNoise = upperInterpolatedNoise.eval(x / 60.0, z / 60.0) * 16;
					double lowerNoise = lowerInterpolatedNoise.eval(x / 60.0, z / 60.0) * 10;
					noise += MathHelper.lerp(lerp, lowerNoise, upperNoise);
				}

				noise += detailNoise.eval(x / 20.0, z / 20.0) * 3.25;

				int height = 60 + (int)noise;
				heightmap[((x + 256) * 512) + (z + 256)] = height;

				double structureExtent = structureNoise.sample(x / 120.0, z / 120.0);
				double chestExtent = chestNoise.sample(x / 45.0, z / 45.0);

				for (int y = 0; y <= height; y++) {
					// Simple surface building
					BlockState state = Blocks.STONE.getDefaultState();
					if (y == height) {
						state = Blocks.GRASS_BLOCK.getDefaultState();

						// Add a chest if the chest noise is low enough
						if (chestExtent < 0.01) {
							lootChests.add(mutable.set(x, y + 1, z).toImmutable());
						}

						// If the structure start noise is low enough, place a structure
						if (structureExtent < 0.005) {
							structureStarts.add(mutable.set(x, y, z).toImmutable());
						}
					} else if ((height - y) <= 3) {
						state = Blocks.DIRT.getDefaultState();
					} else if (y == 0) {
						state = Blocks.BEDROCK.getDefaultState();
					}

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
