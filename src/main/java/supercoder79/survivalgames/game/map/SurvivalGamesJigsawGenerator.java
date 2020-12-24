package supercoder79.survivalgames.game.map;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class SurvivalGamesJigsawGenerator {
    private final Random random = new Random();
    private final DynamicRegistryManager registryManager;
    private final StructureManager structureManager;
    private final ChunkGenerator generator;

    private BlockPos origin = BlockPos.ORIGIN;
    private final Long2ObjectMap<List<PoolStructurePiece>> piecesByChunk = new Long2ObjectOpenHashMap<>();

    public SurvivalGamesJigsawGenerator(MinecraftServer server, ChunkGenerator generator) {
        this.registryManager = server.getRegistryManager();
        this.structureManager = server.getStructureManager();
        this.generator = generator;
    }

    public void arrangePieces(BlockPos origin, Identifier startPoolId, int depth) {
        this.origin = origin;

        StructureFeature.method_28664();

        List<PoolStructurePiece> pieces = new ArrayList<>();

        StructurePoolFeatureConfig config = new StructurePoolFeatureConfig(() -> this.registryManager.get(Registry.TEMPLATE_POOL_WORLDGEN).get(startPoolId), depth);

        BlockRotation startRotation = BlockRotation.random(this.random);
        StructurePool startPool = config.getStartPool().get();
        StructurePoolElement startElement = startPool.getRandomElement(this.random);
        PoolStructurePiece startPiece = new PoolStructurePiece(
                this.structureManager, startElement,
                origin, startElement.getGroundLevelDelta(), startRotation,
                startElement.getBoundingBox(this.structureManager, origin, startRotation)
        );

        BlockBox startBox = startPiece.getBoundingBox();
        int centerX = (startBox.maxX + startBox.minX) / 2;
        int centerZ = (startBox.maxZ + startBox.minZ) / 2;
        int centerY = origin.getY() + this.generator.getHeightOnGround(centerX, centerZ, Heightmap.Type.WORLD_SURFACE_WG);

        int targetY = startBox.minY + startPiece.getGroundLevelDelta();
        startPiece.translate(0, centerY - targetY, 0);
        pieces.add(startPiece);

        StructurePoolBasedGenerator.method_27230(this.registryManager, startPiece, config.getSize(), PoolStructurePiece::new, this.generator, this.structureManager, pieces, this.random);

        for (PoolStructurePiece piece : pieces) {
            BlockBox box = piece.getBoundingBox();
            int minChunkX = box.minX >> 4;
            int minChunkZ = box.minZ >> 4;
            int maxChunkX = box.maxX >> 4;
            int maxChunkZ = box.maxZ >> 4;

            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
                    long chunkPos = ChunkPos.toLong(chunkX, chunkZ);
                    List<PoolStructurePiece> piecesByChunk = this.piecesByChunk.computeIfAbsent(chunkPos, p -> new ArrayList<>());
                    piecesByChunk.add(piece);
                }
            }
        }
    }

    public void generate(ChunkRegion region, StructureAccessor structures) {
        ChunkPos chunkPos = new ChunkPos(region.getCenterChunkX(), region.getCenterChunkZ());
        List<PoolStructurePiece> pieces = this.piecesByChunk.remove(chunkPos.toLong());

        if (pieces != null) {
            BlockBox chunkBox = new BlockBox(chunkPos.getStartX(), 0, chunkPos.getStartZ(), chunkPos.getEndX(), 255, chunkPos.getEndZ());
            for (PoolStructurePiece piece : pieces) {
                piece.method_27236(region, structures, this.generator, this.random, chunkBox, this.origin, false);
            }
        }
    }
}
