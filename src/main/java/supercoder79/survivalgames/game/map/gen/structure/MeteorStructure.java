package supercoder79.survivalgames.game.map.gen.structure;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.SimpleRandom;
import supercoder79.survivalgames.game.map.loot.LootProvider;
import supercoder79.survivalgames.game.map.loot.LootProviders;
import supercoder79.survivalgames.game.map.loot.WeightedList;

import java.util.Random;

public final class MeteorStructure implements StructureGen {
    public static final MeteorStructure INSTANCE = new MeteorStructure();

    private static final WeightedList<BlockState> STATES = new WeightedList<BlockState>()
            .add(Blocks.OBSIDIAN.getDefaultState(), 10)
            .add(Blocks.DIAMOND_ORE.getDefaultState(), 1)
            .add(Blocks.IRON_ORE.getDefaultState(), 2)
            .add(Blocks.RAW_IRON_BLOCK.getDefaultState(), 1);

    @Override
    public int nearbyChestCount(Random random) {
        return 1 + random.nextInt(2);
    }

    @Override
    public LootProvider getLootProvider() {
        return LootProviders.GENERIC;
    }

    @Override
    public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
        generateNetherrack(world, pos, random);
        generateMeteor(world, pos, random);
    }

    public void generateNetherrack(ServerWorldAccess world, BlockPos pos, Random random) {
        int radius = 5 + random.nextInt(3);
        double dRadius = radius;
        DoublePerlinNoiseSampler noise = DoublePerlinNoiseSampler.create(new SimpleRandom(random.nextLong()), -3, 0.1);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double ax = x / dRadius;
                double az = z / dRadius;

                double rad = 1.0 + noise.sample(pos.getX() + x, pos.getY(), pos.getZ() + z);
                rad += random.nextDouble() * 0.1;

                if (ax * ax + az * az <= rad) {
                    BlockPos top = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, pos.add(x, 0, z)).down();
                    world.setBlockState(top, Blocks.NETHERRACK.getDefaultState(), 3);
                    if (random.nextInt(5) == 0) {
                        world.setBlockState(top.up(), Blocks.FIRE.getDefaultState(), 3);
                    }
                }
            }
        }
    }

    public void generateMeteor(ServerWorldAccess world, BlockPos pos, Random random) {
        int radius = 2 + random.nextInt(2);
        double dRadius = radius;
        DoublePerlinNoiseSampler noise = DoublePerlinNoiseSampler.create(new SimpleRandom(random.nextLong()), -3, 0.1);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -radius; y <= radius; y++) {
                    double ax = x / dRadius;
                    double az = z / dRadius;
                    double ay = y / dRadius;

                    if (ax * ax + az * az + ay * ay <= 1.0 + noise.sample(pos.getX() + x, pos.getY() + y, pos.getZ() + z)) {
                        world.setBlockState(pos.add(x, y, z), STATES.pickRandom(random), 3);
                    }
                }
            }
        }
    }
}
