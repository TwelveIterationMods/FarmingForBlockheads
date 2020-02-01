package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;

public interface InternalMethods {
	IMarketCategory registerMarketCategory(ResourceLocation id, String tooltipLangKey, ItemStack icon, int sortIndex);
	void registerMarketEntry(ItemStack outputItem, ItemStack costItem, IMarketCategory category);
	void registerMarketDefaultHandler(String key, IMarketRegistryDefaultHandler defaultHandler);
	@Nullable
	IMarketCategory getMarketCategory(ResourceLocation id);
	@Nullable
	IMarketEntry getMarketEntry(ItemStack outputItem);
	Collection<IMarketEntry> getMarketEntries();
}
