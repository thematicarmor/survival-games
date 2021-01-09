package supercoder79.survivalgames.game.map.gen.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import supercoder79.survivalgames.game.map.loot.LootHelper;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import supercoder79.survivalgames.game.map.loot.LootProviders;

import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class LootChestProcessor extends StructureProcessor {
	public static Codec<LootChestProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BlockPos.CODEC.fieldOf("pos").forGetter(p -> p.chestPos),
			Codec.BOOL.fieldOf("match_terrain").orElse(false).forGetter(p -> p.matchTerrain),
			Codec.STRING.fieldOf("type").forGetter(p -> p.lootType)
	).apply(instance, LootChestProcessor::new));

	private final BlockPos chestPos;
	private final boolean matchTerrain;
	private final String lootType;

	public LootChestProcessor(BlockPos chestPos, boolean matchTerrain, String lootType) {
		this.chestPos = chestPos;
		this.matchTerrain = matchTerrain;
		this.lootType = lootType;
	}

	@Override
	public Structure.StructureBlockInfo process(WorldView world, BlockPos worldPos, BlockPos localPos, Structure.StructureBlockInfo localInfo, Structure.StructureBlockInfo worldInfo, StructurePlacementData structurePlacementData) {
		BlockPos pos = localInfo.pos;
		if (pos.asLong() == this.chestPos.asLong()) {
			// TODO: rotation

			if (this.matchTerrain) {
				int y = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, worldInfo.pos.getX(), worldInfo.pos.getZ());

				BlockPos topPos = new BlockPos(worldInfo.pos.getX(), y, worldInfo.pos.getZ());
				LootHelper.placeProviderChest((WorldAccess) world, topPos, getLootProvider(this.lootType));
			} else {
				LootHelper.placeProviderChest((WorldAccess) world, worldInfo.pos, getLootProvider(this.lootType));
			}

			// Place air here
			return null;
		}


		return worldInfo;
	}

	// TODO: loot provider registry
	private static LootProvider getLootProvider(String name) {
		switch (name) {
		case "house":
			return LootProviders.HOUSE;
		case "tower":
			return LootProviders.TOWER;
		case "enchanting_table":
			return LootProviders.ENCHANTING_TABLE;
		case "ore_pile":
			return LootProviders.ORE_PILE;
		default:
			return LootProviders.GENERIC;
		}
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return SurvivalGamesProcessorTypes.LOOT;
	}
}
