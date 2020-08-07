package supercoder79.survivalgames.game.map.gen.feature;

import java.util.Random;

import net.gegy1000.plasmid.game.map.GameMapBuilder;
import supercoder79.survivalgames.game.map.gen.GenHelper;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CactusGen implements MapGen {

	private final BlockPos origin;

	public CactusGen(BlockPos origin) {
		this.origin = origin;
	}

	@Override
	public void generate(GameMapBuilder builder) {
		Random random = new Random();

		for (int i = 0; i < 12; i++) {
			int aX = random.nextInt(6) - random.nextInt(6);
			int aY = random.nextInt(8) - random.nextInt(8);
			int aZ = random.nextInt(6) - random.nextInt(6);
			BlockPos pos = origin.add(aX, aY, aZ);

			boolean canSpawn = true;

			for (Direction direction : GenHelper.HORIZONTALS) {
				BlockPos dLocal = pos.offset(direction);
				if (builder.getBlockState(dLocal).isOpaque()) {
					canSpawn = false;

					break;
				}
			}

			if (canSpawn && (builder.getBlockState(pos.down()) == Blocks.SAND.getDefaultState() || builder.getBlockState(pos.down()) == Blocks.CACTUS.getDefaultState()) && builder.getBlockState(pos).isAir()) {
				builder.setBlockState(pos, Blocks.CACTUS.getDefaultState(), false);
			}
		}
	}
}