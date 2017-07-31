package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;

public interface MarketRegistryDefaultHandler {
	void apply(ItemStack defaultPayment);
	boolean isEnabledByDefault();
	ItemStack getDefaultPayment();
}
