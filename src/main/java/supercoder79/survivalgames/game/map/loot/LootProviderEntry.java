package supercoder79.survivalgames.game.map.loot;

public final class LootProviderEntry {
	public final LootProvider provider;
	public final int count;

	public LootProviderEntry(LootProvider provider, int count) {
		this.provider = provider;
		this.count = count;
	}
}
