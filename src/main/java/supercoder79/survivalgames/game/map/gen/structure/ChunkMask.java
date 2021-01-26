package supercoder79.survivalgames.game.map.gen.structure;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public final class ChunkMask {
	private final LongSet chunks = new LongOpenHashSet();

	public ChunkMask() {

	}

	public void and(ChunkPos pos) {
		chunks.add(pos.toLong());
	}

	public void and(ChunkBox box) {
		chunks.addAll(box.getAllPositions());
	}

	public boolean isIn(ChunkPos pos) {
		return chunks.contains(pos.toLong());
	}

	public boolean isIn(int x, int z) {
		return chunks.contains(ChunkPos.toLong(x, z));
	}
}
