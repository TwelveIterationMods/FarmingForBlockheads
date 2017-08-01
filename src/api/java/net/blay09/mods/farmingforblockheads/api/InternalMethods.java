package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;

public interface InternalMethods {
	void registerMarketCategory(ResourceLocation id, String tooltipLangKey, ResourceLocation texturePath, int textureX, int textureY, int sortIndex);
	void registerMarketEntry(ItemStack outputItem, ItemStack costItem, IMarketCategory category);
	void registerMarketDefaultHandler(String key, MarketRegistryDefaultHandler defaultHandler);
	@Nullable
	IMarketCategory getMarketCategory(ResourceLocation id);
	@Nullable
	IMarketEntry getMarketEntry(ItemStack outputItem);
	Collection<IMarketEntry> getMarketEntries();
}
