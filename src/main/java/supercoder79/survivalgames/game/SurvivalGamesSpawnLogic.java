package supercoder79.survivalgames.game;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.WorldChunk;
import supercoder79.survivalgames.game.config.SurvivalGamesConfig;
import xyz.nucleoid.plasmid.game.GameWorld;

import java.util.Random;

public final class SurvivalGamesSpawnLogic {
    private final GameWorld world;
    private final SurvivalGamesConfig config;

    public SurvivalGamesSpawnLogic(GameWorld world, SurvivalGamesConfig config) {
        this.world = world;
        this.config = config;
    }

    public void resetPlayer(ServerPlayerEntity player, GameMode gameMode) {
        player.inventory.clear();
        player.getEnderChestInventory().clear();
        player.clearStatusEffects();
        player.setHealth(20.0F);
        player.getHungerManager().setFoodLevel(20);
        player.fallDistance = 0.0F;
        player.setGameMode(gameMode);
        player.setExperienceLevel(0);
        player.setExperiencePoints(0);
    }

    public void spawnPlayer(ServerPlayerEntity player) {
        ServerWorld world = this.world.getWorld();

        // TODO: trig distribution
        Random random = world.getRandom();
        int x = random.nextInt(config.borderConfig.startSize) - (config.borderConfig.startSize / 2);
        int z = random.nextInt(config.borderConfig.startSize) - (config.borderConfig.startSize / 2);

        ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
        world.getChunkManager().addTicket(ChunkTicketType.field_19347, chunkPos, 1, player.getEntityId());

        WorldChunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
        BlockPos pos = new BlockPos(x, chunk.sampleHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z) + 1, z);

        player.teleport(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0F, 0.0F);
    }
}
