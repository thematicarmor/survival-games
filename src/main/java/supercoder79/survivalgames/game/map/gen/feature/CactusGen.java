package supercoder79.survivalgames.game.map.gen.feature;

import java.util.Random;

import net.gegy1000.plasmid.game.map.GameMapBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.math.BlockPos;

public class CactusGen implements MapGen {

	private final BlockPos origin;

	public CactusGen(BlockPos origin) {
		this.origin = origin;
	}

	@Override
	public void generate(GameMapBuilder builder) {
		Random random = new Random();

		for (int i = 0; i < 12; i++) {
			int aX = random.nextInt(12) - random.nextInt(12);
			int aY = random.nextInt(4) - random.nextInt(4);
			int aZ = random.nextInt(12) - random.nextInt(12);
			BlockPos pos = origin.add(aX, aY, aZ);

			if ((builder.getBlockState(pos.down()) == Blocks.SAND.getDefaultState() || builder.getBlockState(pos.down()) == Blocks.CACTUS.getDefaultState()) && builder.getBlockState(pos).isAir()) {
				builder.setBlockState(pos, Blocks.CACTUS.getDefaultState(), false);
			}
		}
	}
}