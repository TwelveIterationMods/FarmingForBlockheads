package net.blay09.mods.farmingforblockheads.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MarketCategory extends BlankRecipeCategory<MarketRecipe> {

	public static final String UID = "farmingforblockheads:market";

	private static final ResourceLocation TEXTURE = new ResourceLocation(FarmingForBlockheads.MOD_ID, "textures/gui/jei_market.png");

	private final IDrawableStatic background;

	public MarketCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 86, 48);
	}

	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return I18n.format("jei." + UID);
	}

	@Override
	public String getModName() {
		return "Farming for Blockheads";
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, MarketRecipe recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 15, 12);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
		recipeLayout.getItemStacks().init(1, false, 53, 12);
		recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).get(0));
	}

}
