package supercoder79.survivalgames.game.map.gen.structure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class ChunkBox {
	private int minX;
	private int minZ;
	private int maxX;
	private int maxZ;
	public ChunkBox() {
		this.minX = 0;
		this.minZ = 0;
		this.maxX = 0;
		this.maxZ = 0;
	}

	public void encompass(ChunkPos pos) {
		this.minX = Math.min(pos.x, this.minX);
		this.minZ = Math.min(pos.z, this.minZ);
		this.maxX = Math.max(pos.x, this.maxX);
		this.maxZ = Math.max(pos.x, this.maxZ);
	}

	public boolean isIn(ChunkPos pos) {
		return isIn(pos.x, pos.z);
	}

	public boolean isIn(int x, int z) {
		if (x >= this.minX && x <= this.maxX) {
			if (z >= this.minZ && z <= this.maxZ) {
				return true;
			}
		}

		return false;
	}

	public boolean isBlockIn(BlockPos pos) {
		return isBlockIn(pos.getX(), pos.getZ());
	}

	public boolean isBlockIn(int x, int z) {
		int minX = this.minX << 4;
		int minZ = this.minZ << 4;
		int maxX = this.maxX << 4;
		int maxZ = this.maxZ << 4;

		if (x >= minX && x <= maxX) {
			if (z >= minZ && z <= maxZ) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return "[(" + this.minX + ", " + this.minZ + "), (" + this.maxX + ", " + this.maxZ + ")]";
	}
}
