package supercoder79.survivalgames.game;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import net.gegy1000.plasmid.game.GameWorld;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;

public final class SurvivalGamesSpawnLogic {
    private final GameWorld world;

    public SurvivalGamesSpawnLogic(GameWorld world) {
        this.world = world;
    }

    public void resetPlayer(ServerPlayerEntity player, GameMode gameMode) {
        player.inventory.clear();
        player.getEnderChestInventory().clear();
        player.clearStatusEffects();
        player.setHealth(20.0F);
        player.getHungerManager().setFoodLevel(20);
        player.fallDistance = 0.0F;
        player.setGameMode(gameMode);
    }

    public void spawnPlayer(ServerPlayerEntity player) {
        ServerWorld world = this.world.getWorld();

        Random random = world.getRandom();
        int x = random.nextInt(512) - 256;
        int z = random.nextInt(512) - 256;
        BlockPos pos = new BlockPos(x, world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, x, z), z);

        ChunkPos chunkPos = new ChunkPos(pos);
        world.getChunkManager().addTicket(ChunkTicketType.field_19347, chunkPos, 1, player.getEntityId());

        // Get the y position by using this amazing hack
        BlockPos.Mutable mutable = pos.mutableCopy();
        mutable.setY(256);
        for (int y = 256; y > 0; y--) {
            if (world.getBlockState(mutable.set(x, y, z)).isOpaque()) {
                break;
            }
        }

        pos = mutable.up(2).toImmutable();

        System.out.println(pos);

        player.teleport(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0F, 0.0F);
    }
}
