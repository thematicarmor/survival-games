package supercoder79.survivalgames.game.map.gen.structure;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import supercoder79.survivalgames.game.GameTrackable;
import supercoder79.survivalgames.game.GenerationTracker;
import supercoder79.survivalgames.game.map.biome.generator.BiomeGenerator;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import supercoder79.survivalgames.game.map.loot.LootProviders;
import xyz.nucleoid.substrate.gen.GenHelper;

import java.util.Random;

public final class SpawnerStructure implements StructureGen, GameTrackable {
    public static final SpawnerStructure INSTANCE = new SpawnerStructure();

    @Override
    public int nearbyChestCount(Random random) {
        return 0;
    }

    @Override
    public LootProvider getLootProvider() {
        return LootProviders.GENERIC;
    }

    @Override
    public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                int dist = Math.abs(x) + Math.abs(z);
                int height = 0;

                if (dist == 0) {
                    height = 6 + random.nextInt(3);
                } else if (dist == 1) {
                    height = 2 + random.nextInt(3);
                } else if (dist == 2) {
                    height = random.nextInt(2);
                }

                if (height == 0) {
                    continue;
                }

                for (int y = -2; y < height; y++) {
                    BlockPos local = pos.add(x, y, z);
                    if (world.getBlockState(local).isAir() || y == 1) { // Skip air check around spawner
                        world.setBlockState(local, getState(random), 3);
                    }
                }
            }
        }

        setSpawner(world, pos.up());

        setButtons(world, pos.up());
    }

    private static BlockState getState(Random random) {
        int i = random.nextInt(5);
        if (i < 2) {
            return Blocks.MOSSY_STONE_BRICKS.getDefaultState();
        } else if (i == 3) {
            return Blocks.CRACKED_STONE_BRICKS.getDefaultState();
        } else {
            return Blocks.STONE_BRICKS.getDefaultState();
        }
    }

    private static void setSpawner(ServerWorldAccess world, BlockPos pos) {
        world.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 3);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof MobSpawnerBlockEntity) {
            ((MobSpawnerBlockEntity)blockEntity).getLogic().setEntityId(EntityType.ZOMBIE);
        }
    }

    private static void setButtons(ServerWorldAccess world, BlockPos pos) {
        for (Direction horizontal : GenHelper.HORIZONTALS) {
            world.setBlockState(pos.offset(horizontal, 2), Blocks.WARPED_BUTTON.getDefaultState().with(Properties.HORIZONTAL_FACING, horizontal), 3);
        }
    }

    @Override
    public Tracker getTracker() {
        return (tracker, pos) -> tracker.addRedstoneTracked(pos.up());
    }
}
