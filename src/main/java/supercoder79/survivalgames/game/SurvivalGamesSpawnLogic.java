package supercoder79.survivalgames.game;

import java.util.Random;

import net.gegy1000.plasmid.game.map.GameMap;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.Heightmap;

public final class SurvivalGamesSpawnLogic {
    private final GameMap map;

    public SurvivalGamesSpawnLogic(GameMap map) {
        this.map = map;
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
        ServerWorld world = this.map.getWorld();

        Random random = world.getRandom();
        int x = random.nextInt(512) - 256;
        int z = random.nextInt(512) - 256;
        BlockPos pos = new BlockPos(x, world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z), z);

        player.teleport(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0F, 0.0F);
    }
}
