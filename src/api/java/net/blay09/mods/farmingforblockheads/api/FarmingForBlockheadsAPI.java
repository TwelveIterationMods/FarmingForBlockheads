package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;

public class FarmingForBlockheadsAPI {

	public static final ResourceLocation MARKET_CATEGORY_SEEDS = new ResourceLocation("farmingforblockheads", "seeds");
	public static final ResourceLocation MARKET_CATEGORY_SAPLINGS = new ResourceLocation("farmingforblockheads", "saplings");
	public static final ResourceLocation MARKET_CATEGORY_FLOWERS = new ResourceLocation("farmingforblockheads", "flowers");
	public static final ResourceLocation MARKET_CATEGORY_OTHER = new ResourceLocation("farmingforblockheads", "other");

	private static InternalMethods internalMethods;

	/**
	 * INTERNAL USE ONLY
	 */
	public static void __setupAPI(InternalMethods internalMethods) {
		FarmingForBlockheadsAPI.internalMethods = internalMethods;
	}

	public static void registerMarketCategory(ResourceLocation registryName, String tooltipLangKey, ResourceLocation texturePath, int textureX, int textureY, int sortIndex) {
		internalMethods.registerMarketCategory(registryName, tooltipLangKey, texturePath, textureX, textureY, sortIndex);
	}

	public static void registerMarketEntry(ItemStack outputItem, ItemStack costItem, IMarketCategory category) {
		internalMethods.registerMarketEntry(outputItem, costItem, category);
	}

	public static void registerMarketDefaultHandler(String key, MarketRegistryDefaultHandler defaultHandler) {
		internalMethods.registerMarketDefaultHandler(key, defaultHandler);
	}

	@Nullable
	public static IMarketCategory getMarketCategory(ResourceLocation registryName) {
		return internalMethods.getMarketCategory(registryName);
	}

	@Nullable
	public static IMarketEntry getMarketEntry(ItemStack outputItem) {
		return internalMethods.getMarketEntry(outputItem);
	}

	public static Collection<IMarketEntry> getMarketEntries() {
		return internalMethods.getMarketEntries();
	}

	@SuppressWarnings("ConstantConditions")
	public static IMarketCategory getMarketCategorySeeds() {
		return getMarketCategory(MARKET_CATEGORY_SEEDS);
	}

	@SuppressWarnings("ConstantConditions")
	public static IMarketCategory getMarketCategoryFlowers() {
		return getMarketCategory(MARKET_CATEGORY_FLOWERS);
	}

	@SuppressWarnings("ConstantConditions")
	public static IMarketCategory getMarketCategorySaplings() {
		return getMarketCategory(MARKET_CATEGORY_SAPLINGS);
	}

	@SuppressWarnings("ConstantConditions")
	public static IMarketCategory getMarketCategoryOther() {
		return getMarketCategory(MARKET_CATEGORY_OTHER);
	}

}
