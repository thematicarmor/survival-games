package supercoder79.survivalgames.game.logic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public interface ActiveLogic {
    void tick(long time);

    default ActionResult onEntityDeath(Entity entity, DamageSource source) {
        return ActionResult.PASS;
    }
}
