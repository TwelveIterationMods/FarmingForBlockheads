package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;

public interface IMarketEntry {
	ItemStack getOutputItem();
	ItemStack getCostItem();
	IMarketCategory getCategory();
}
