package net.blay09.mods.farmingforblockheads.compat.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class MarketRecipeHandler implements IRecipeHandler<MarketRecipe> {

	@Override
	public Class<MarketRecipe> getRecipeClass() {
		return MarketRecipe.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return MarketCategory.UID;
	}

	@Override
	public String getRecipeCategoryUid(MarketRecipe recipe) {
		return MarketCategory.UID;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(MarketRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(MarketRecipe recipe) {
		return true;
	}

}
