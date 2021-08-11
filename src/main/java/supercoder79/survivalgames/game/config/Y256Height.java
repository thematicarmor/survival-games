package supercoder79.survivalgames.game.config;

import net.minecraft.world.HeightLimitView;

public final class Y256Height implements HeightLimitView {
    public static final Y256Height INSTANCE = new Y256Height();

    @Override
    public int getHeight() {
        return 256;
    }

    @Override
    public int getBottomY() {
        return 0;
    }
}
