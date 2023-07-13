package supercoder79.survivalgames.game.logic;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import supercoder79.survivalgames.entity.SpawnerZombieEntity;
import supercoder79.survivalgames.game.SurvivalGamesActive;
import supercoder79.survivalgames.game.map.loot.LootProviders;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

import java.util.HashSet;
import java.util.Set;

public final class SpawnerLogic implements ActiveLogic {
    private final SurvivalGamesActive active;
    private final BlockPos pos;
    private Stage stage = Stage.INIT;
    private int placedMonsters = 0;
    private int lootRemaining = 10;
    private final Set<SpawnerZombieEntity> zombies = new HashSet<>();
    private final Random random = Random.create();
    private final int targetMonsters = 8 + this.random.nextInt(4);

    public SpawnerLogic(SurvivalGamesActive active, BlockPos pos) {
        this.active = active;
        this.pos = pos;

        // Start fireworks
        for (int i = 0; i < 3; i++) {
            spawnFirework(0xFF0000);
        }
    }

    private void spawnFirework(int color) {
        ServerWorld world = this.active.getWorld();
        BlockPos pos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, this.pos);

        FireworkRocketEntity firework = new FireworkRocketEntity(
                world,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                ItemStackBuilder.firework(color, 2, FireworkRocketItem.Type.LARGE_BALL).build()
        );

        world.spawnEntity(firework);
    }

    @Override
    public void tick(long time) {
        if (time % 8 == 0) {
            if (this.stage == Stage.INIT) {
                if (this.placedMonsters >= this.targetMonsters) {
                    this.stage = Stage.ACTIVE;
                    return;
                }

                int x = this.pos.getX() + (this.random.nextInt(8) - this.random.nextInt(8));
                int z = this.pos.getZ() + (this.random.nextInt(8) - this.random.nextInt(8));
                int y = this.active.getWorld().getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);

                SpawnerZombieEntity zombie = new SpawnerZombieEntity(this.active.getWorld());
                // More damage
                zombie.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(5.0);

                zombie.refreshPositionAndAngles(new BlockPos(x, y, z), 0, 0);
                this.active.getWorld().spawnEntity(zombie);
                this.zombies.add(zombie);

                this.placedMonsters++;
            } else if (this.stage == Stage.ACTIVE) {
                if (this.zombies.size() == 0) {
                    this.stage = Stage.FINISHED;
                    return;
                }

                int x = this.pos.getX() + (this.random.nextInt(8) - this.random.nextInt(8));
                int z = this.pos.getZ() + (this.random.nextInt(8) - this.random.nextInt(8));
                int y = this.pos.getY() + (this.random.nextInt(6) - this.random.nextInt(6));

                this.active.getWorld().spawnParticles(ParticleTypes.SMOKE, x, y, z, 4, 0.1, 0.1, 0.1, 0.1);
            } else if (this.stage == Stage.FINISHED) {
                if (this.lootRemaining >= 0) {
                    int x = this.pos.getX() + (this.random.nextInt(8) - this.random.nextInt(8));
                    int z = this.pos.getZ() + (this.random.nextInt(8) - this.random.nextInt(8));
                    int y = this.active.getWorld().getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);


                    LootTable lootTable = active.getWorld().getServer().getLootManager().getLootTable(LootProviders.SPAWNER_LOOT.identifier);
                    ObjectArrayList<ItemStack> items = lootTable.generateLoot(new LootContextParameterSet.Builder(active.getWorld()).build(LootContextType.create().build()));

                    ItemStack stack = items.get(random.nextBetween(0, items.size() - 1));
                    tryEnchant(stack);

                    ItemEntity item = new ItemEntity(this.active.getWorld(), x, y + 6, z, stack);
                    this.active.getWorld().spawnEntity(item);

                    this.lootRemaining--;
                } else {
                    for (int i = 0; i < 3; i++) {
                        spawnFirework(0x0000FF);
                    }
                    // Destroy spawner
                    this.active.getWorld().removeBlock(this.pos, false);
                    this.active.getWorld().removeBlockEntity(this.pos);
                    // Done!
                    this.active.destroyLogic(this);
                    return;
                }
            }
        }
    }

    private void tryEnchant(ItemStack stack) {
        if (this.random.nextInt(2) == 0) {
            if (stack.isEnchantable()) {
                EnchantmentHelper.enchant(this.random, stack, 5 + this.random.nextInt(this.random.nextInt(25) + 1), true);
            }
        }
    }

    @Override
    public ActionResult onEntityDeath(Entity entity, DamageSource source) {
        if (entity instanceof SpawnerZombieEntity zombie) {
            if (this.zombies.contains(zombie)) {
                this.zombies.remove(zombie);

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    private enum Stage {
        INIT,
        ACTIVE,
        FINISHED
    }
}
