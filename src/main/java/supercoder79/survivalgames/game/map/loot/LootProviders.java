package supercoder79.survivalgames.game.map.loot;

import bond.thematic.minigamemanager.MinigameManagerMod;
import net.minecraft.util.Identifier;

/**
 * Changed to use vanilla loot tables, the converter I used to convert between their format and vanilla can be found <a href="https://gist.github.com/ANutley/a9d7135e6a81d61139c0ac16a901f258">here</a>
 */
public class LootProviders {

    public static final LootProvider GENERIC = new LootProvider(new Identifier(MinigameManagerMod.ID, "survivalgames/generic"));
    public static final LootProvider HOUSE = new LootProvider(new Identifier(MinigameManagerMod.ID, "survivalgames/house"));
    public static final LootProvider ENCHANTING_TABLE = new LootProvider(new Identifier(MinigameManagerMod.ID, "survivalgames/enchantingtable"));
    public static final LootProvider TOWER = new LootProvider(new Identifier(MinigameManagerMod.ID, "survivalgames/tower"));
    public static final LootProvider ORE_PILE = new LootProvider(new Identifier(MinigameManagerMod.ID, "survivalgames/orepile"));
    public static final LootProvider FARMLAND = new LootProvider(new Identifier(MinigameManagerMod.ID, "survivalgames/farmland"));
    public static final LootProvider METEOR = new LootProvider(new Identifier(MinigameManagerMod.ID, "survivalgames/meteor"));
    public static final LootProvider SPAWNER_LOOT = new LootProvider(new Identifier(MinigameManagerMod.ID, "survivalgames/spawner"));

}
