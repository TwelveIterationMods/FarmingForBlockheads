package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.minecraft.item.ItemStack;

public class MarketEntry implements IMarketEntry {

	private final ItemStack outputItem;
	private final ItemStack costItem;
	private final IMarketCategory category;

	public MarketEntry(ItemStack outputItem, ItemStack costItem, IMarketCategory category) {
		this.outputItem = outputItem;
		this.costItem = costItem;
		this.category = category;
	}

	@Override
	public ItemStack getCostItem() {
		return costItem;
	}

	@Override
	public ItemStack getOutputItem() {
		return outputItem;
	}

	@Override
	public IMarketCategory getCategory() {
		return category;
	}

}
