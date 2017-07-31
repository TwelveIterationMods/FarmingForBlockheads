package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.util.ResourceLocation;

public interface IMarketCategory {
	ResourceLocation getRegistryName();

	/**
	 * @return the language key for the tooltip of the category button
	 */
	String getTooltipLangKey();

	/**
	 * @return the texture sheet to use for the category button - icon must be 20x20
	 */
	ResourceLocation getIconTexture();

	/**
	 * @return the texture x coordinate to use for the category button - width is always 20
	 */
	int getIconTextureX();

	/**
	 * @return the texture y coordinate to use for the category button - height is always 20
	 */
	int getIconTextureY();

	default boolean passes(IMarketEntry entry) {
		return entry.getCategory() == this;
	}
}
