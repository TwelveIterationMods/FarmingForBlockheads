package net.blay09.mods.farmingforblockheads.registry;

import net.minecraft.item.ItemStack;

public interface MarketRegistryDefaultHandler {
	void apply(MarketRegistry registry, ItemStack defaultPayment);
	boolean isEnabledByDefault();
	ItemStack getDefaultPayment();
}
