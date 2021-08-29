package supercoder79.survivalgames.game;

import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public final class GenerationTracker {
    private final Set<BlockPos> redstoneTracked = new HashSet<>();

    public synchronized void addRedstoneTracked(BlockPos pos) {
        this.redstoneTracked.add(pos);
    }

    public synchronized Set<BlockPos> getRedstoneTracked() {
        return this.redstoneTracked;
    }

    public synchronized void removeRedstoneTracked(BlockPos pos) {
        this.redstoneTracked.remove(pos);
    }
}
