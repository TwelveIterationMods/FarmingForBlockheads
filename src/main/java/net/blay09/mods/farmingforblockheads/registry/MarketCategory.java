package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.minecraft.util.ResourceLocation;

public class MarketCategory implements IMarketCategory {
	private final ResourceLocation registryName;
	private final String tooltipLangKey;
	private final ResourceLocation texturePath;
	private final int textureX;
	private final int textureY;
	private int sortIndex;

	public MarketCategory(ResourceLocation registryName, String tooltipLangKey, ResourceLocation texturePath, int textureX, int textureY, int sortIndex) {
		this.registryName = registryName;
		this.tooltipLangKey = tooltipLangKey;
		this.texturePath = texturePath;
		this.textureX = textureX;
		this.textureY = textureY;
		this.sortIndex = sortIndex;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return registryName;
	}

	@Override
	public String getTooltipLangKey() {
		return tooltipLangKey;
	}

	@Override
	public ResourceLocation getIconTexture() {
		return texturePath;
	}

	@Override
	public int getIconTextureX() {
		return textureX;
	}

	@Override
	public int getIconTextureY() {
		return textureY;
	}

	@Override
	public int getSortIndex() {
		return sortIndex;
	}
}
