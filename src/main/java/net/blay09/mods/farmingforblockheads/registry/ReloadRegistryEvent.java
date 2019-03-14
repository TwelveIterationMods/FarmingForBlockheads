package net.blay09.mods.farmingforblockheads.registry;

import net.minecraftforge.eventbus.api.Event;

public class ReloadRegistryEvent extends Event {

	private final AbstractRegistry registry;

	public ReloadRegistryEvent(AbstractRegistry registry) {
		this.registry = registry;
	}

	public AbstractRegistry getRegistry() {
		return registry;
	}

}
