package supercoder79.survivalgames.game;

import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class GenerationTracker {
    private final Set<BlockPos> redstoneTracked = new HashSet<>();

    public void addRedstoneTracked(BlockPos pos) {
        synchronized (this.redstoneTracked) {
            this.redstoneTracked.add(pos);
        }
    }

    public void iterateRedstoneTracked(Predicate<BlockPos> consumer) {
        synchronized (this.redstoneTracked) {
            this.redstoneTracked.removeIf(consumer);
        }
    }

    public synchronized void removeRedstoneTracked(BlockPos pos) {
        synchronized (this.redstoneTracked) {
            this.redstoneTracked.remove(pos);
        }
    }
}
