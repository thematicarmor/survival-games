package supercoder79.survivalgames.game.map.gen;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;

public interface MapGen {
	void generate(ServerWorldAccess world, BlockPos pos, Random random);
}
