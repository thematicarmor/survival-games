package supercoder79.survivalgames.entity;

import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;

public final class SpawnerZombieEntity extends ZombieEntity {
    public SpawnerZombieEntity(World world) {
        super(world);
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }
}
