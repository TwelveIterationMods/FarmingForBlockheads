package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.api.InternalMethods;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryDefaultHandler;
import net.blay09.mods.farmingforblockheads.registry.MarketCategory;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;

public class InternalMethodsImpl implements InternalMethods {
	@Override
	public void registerMarketCategory(ResourceLocation registryName, String tooltipLangKey, ResourceLocation texturePath, int textureX, int textureY) {
		MarketRegistry.INSTANCE.registerCategory(new MarketCategory(registryName, tooltipLangKey, texturePath, textureX, textureY));
	}

	@Override
	public void registerMarketEntry(ItemStack outputItem, ItemStack costItem, IMarketCategory category) {
		MarketRegistry.INSTANCE.registerEntry(outputItem, costItem, category);
	}

	@Override
	public void registerMarketDefaultHandler(String key, MarketRegistryDefaultHandler defaultHandler) {
		MarketRegistry.registerDefaultHandler(key, defaultHandler);
	}

	@Override
	@Nullable
	public IMarketCategory getMarketCategory(ResourceLocation registryName) {
		return MarketRegistry.getCategory(registryName);
	}

	@Override
	@Nullable
	public IMarketEntry getMarketEntry(ItemStack outputItem) {
		return MarketRegistry.getEntryFor(outputItem);
	}

	@Override
	public Collection<IMarketEntry> getMarketEntries() {
		return MarketRegistry.getEntries();
	}
}
