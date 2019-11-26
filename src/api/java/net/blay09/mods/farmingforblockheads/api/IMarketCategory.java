package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IMarketCategory extends Comparable<IMarketCategory> {
	ResourceLocation getRegistryName();

	/**
	 * @return the language key for the tooltip of the category button
	 */
	String getTooltipLangKey();

	/**
	 * @return the item to use for the category icon
	 */
	ItemStack getIconStack();

	int getSortIndex();

	default boolean passes(IMarketEntry entry) {
		return entry.getCategory() == this;
	}

	@Override
	default int compareTo(@Nonnull IMarketCategory o) {
		return Integer.compare(getSortIndex(), o.getSortIndex());
	}
}
