package net.blay09.mods.farmingforblockheads.compat.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class MarketRecipe extends BlankRecipeWrapper {

	private final MarketEntry entry;

	public MarketRecipe(MarketEntry entry) {
		this.entry = entry;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, entry.getCostItem());
		ingredients.setOutput(ItemStack.class, entry.getOutputItem());
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		String costText = getFormattedCostString(entry);
		int stringWidth = minecraft.fontRenderer.getStringWidth(costText);
		minecraft.fontRenderer.drawString(costText, 42 - stringWidth / 2, 35, 0xFFFFFF, true);
	}

	private String getFormattedCostString(MarketEntry entry) {
		String color = TextFormatting.GREEN.toString();
		if(entry.getCostItem().getItem() == Items.DIAMOND) {
			color = TextFormatting.AQUA.toString();
		}
		return color + I18n.format("gui.farmingforblockheads:market.cost", entry.getCostItem().getCount(), entry.getCostItem().getDisplayName());
	}

}
