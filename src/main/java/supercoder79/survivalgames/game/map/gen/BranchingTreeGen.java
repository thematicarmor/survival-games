package supercoder79.survivalgames.game.map.gen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import xyz.nucleoid.substrate.gen.MapGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BranchingTreeGen implements MapGen {
    public static final MapGen DARK_OAK = new BranchingTreeGen(Blocks.DARK_OAK_LOG.getDefaultState(), Blocks.DARK_OAK_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Blocks.GRASS_BLOCK.getDefaultState(), 8, 1);
    public static final MapGen JUNGLE = new BranchingTreeGen(Blocks.JUNGLE_LOG.getDefaultState(), Blocks.JUNGLE_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Blocks.GRASS_BLOCK.getDefaultState(), 16, 1);
    public static final MapGen ACACIA = new BranchingTreeGen(Blocks.ACACIA_LOG.getDefaultState(), Blocks.ACACIA_LEAVES.getDefaultState().with(Properties.DISTANCE_1_7, 1), Blocks.GRASS_BLOCK.getDefaultState(), 8, 3);
    public static final MapGen WARPED = new BranchingTreeGen(Blocks.WARPED_STEM.getDefaultState(), Blocks.WARPED_WART_BLOCK.getDefaultState(), Blocks.WARPED_NYLIUM.getDefaultState(), 12, 0);
    public static final MapGen CRIMSON = new BranchingTreeGen(Blocks.CRIMSON_STEM.getDefaultState(), Blocks.NETHER_WART_BLOCK.getDefaultState(), Blocks.CRIMSON_NYLIUM.getDefaultState(), 12, 0);
    public static final MapGen BONE = new BranchingTreeGen(Blocks.BONE_BLOCK.getDefaultState(), Blocks.AIR.getDefaultState(), Blocks.SOUL_SOIL.getDefaultState(), 4, 1);
    public static final MapGen BASALT_COLUMN = new BranchingTreeGen(Blocks.BASALT.getDefaultState(), Blocks.AIR.getDefaultState(), Blocks.BASALT.getDefaultState(), 6, 0);

    private final BlockState log;
    private final BlockState leaves;
    private final BlockState plantable;
    private final int height;
    private final int branchLength;

    public BranchingTreeGen(BlockState log, BlockState leaves, BlockState plantable, int height, int branchLength) {
        this.log = log;
        this.leaves = leaves;
        this.height = height;
        this.branchLength = branchLength;
        this.plantable = plantable;
    }

    @Override
    public void generate(ServerWorldAccess world, BlockPos pos, Random random) {
        if (world.getBlockState(pos.down()) != this.plantable) return;

        int height = this.height + random.nextInt(Math.max(1, this.height / 4));
        int branchThreshold = (int) (height * 0.4);
        List<BlockPos> leaves = new ArrayList<>();

        BlockPos.Mutable mutable = pos.mutableCopy();
        for (int y = 0; y <= height; y++) {
            world.setBlockState(mutable, this.log, 3);

            if (y > branchThreshold && random.nextInt(2) == 0) {
                BlockPos local = mutable.toImmutable();
                double theta = random.nextDouble() * Math.PI * 2;
                // TODO: scale with height
                int branchLength = random.nextInt(3) + this.branchLength;
                for (int i = 0; i <= branchLength; i++) {
                    int dx = (int) (Math.cos(theta) * i);
                    int dy = i / 2;
                    int dz = (int) (Math.sin(theta) * i);

                    world.setBlockState(local.add(dx, dy, dz), this.log, 3);

                    if (i == branchLength) {
                        leaves.add(local.add(dx, dy, dz).toImmutable());
                    }
                }
            }

            if (y == height) {
                BlockPos local = mutable.toImmutable();
                int topCount = 2 + random.nextInt(3);

                for (int i = 0; i < topCount; i++) {

                    double theta = (i / (double) topCount) * Math.PI * 2;
                    theta += random.nextDouble() * 0.3;

                    int branchLength = random.nextInt(4) + 2;
                    for (int j = 0; i <= branchLength; i++) {
                        int dx = (int) (Math.cos(theta) * j);
                        int dz = (int) (Math.sin(theta) * j);

                        world.setBlockState(local.add(dx, j, dz), this.log, 3);

                        if (i == branchLength) {
                            leaves.add(local.add(dx, j, dz).toImmutable());
                        }
                    }
                }

                break;
            }

            mutable.move(Direction.UP);
        }

        for (BlockPos leaf : leaves) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos local = leaf.add(x, 1, z);
                    if (world.getBlockState(local).isAir()) {
                        world.setBlockState(local, this.leaves, 3);
                    }
                }
            }

            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if (Math.abs(x) == 2 && Math.abs(z) == 2) continue;

                    BlockPos local = leaf.add(x, 0, z);
                    if (world.getBlockState(local).isAir()) {
                        world.setBlockState(local, this.leaves, 3);
                    }
                }
            }
        }
    }
}
