package supercoder79.survivalgames.game.map;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import supercoder79.survivalgames.game.map.gen.structure.ChunkBox;
import supercoder79.survivalgames.mixin.SinglePoolElementAccessor;

import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;

public class DebugJigsawMapper {
	public static void map(Long2ObjectMap<List<PoolStructurePiece>> piecesByChunk, ChunkBox townArea) {
		BufferedImage img = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);

		for (int chunkX = -16; chunkX < 16; chunkX++) {
		    for (int chunkZ = -16; chunkZ < 16; chunkZ++) {
		    	int chunkStartX = chunkX * 16;
		    	int chunkStartZ = chunkZ * 16;

				List<PoolStructurePiece> pieces = piecesByChunk.get(new ChunkPos(chunkX, chunkZ).toLong());

		        for (int x = chunkStartX; x < chunkStartX + 16; x++) {
		            for (int z = chunkStartZ; z < chunkStartZ + 16; z++) {
						int color = townArea.isBlockIn(x, z) ? 0x777777 : 0x444444;

						for (PoolStructurePiece piece : pieces) {
							BlockBox box = piece.getBoundingBox();
							if (box.intersectsXZ(x - 2, z - 2, x + 2, z + 2)) {
								color = 0xaaaaaa;

								String location = ((SinglePoolElementAccessor)piece.getPoolElement()).getLocation().left().get().toString();

								// TODO: clean up this mess
								if (location.contains("road")) {
									color = 0xeeeeee;
								}

								if (location.contains("center")) {
									color = 0xffffff;
									break;
								}

								if (location.contains("house")) {
									color = 0xff4444;
									break;
								}

								if (location.contains("tower")) {
									color = 0x44ff44;
									break;
								}

								if (location.contains("enchanting_table")) {
									color = 0x4444ff;
									break;
								}
							}
						}

						img.setRGB(x + 256, z + 256, 0xff000000 | color);
		            }
		        }
		    }
		}

		try {
			Path p = Paths.get("jigsawmap.png");
			ImageIO.write(img, "png", p.toAbsolutePath().toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
