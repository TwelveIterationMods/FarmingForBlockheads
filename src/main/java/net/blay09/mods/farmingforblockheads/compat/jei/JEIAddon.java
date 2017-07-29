package net.blay09.mods.farmingforblockheads.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.client.gui.GuiMarket;
import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

@JEIPlugin
public class JEIAddon extends BlankModPlugin {

	@Override
	public void register(IModRegistry registry) {
		List<MarketRecipe> marketRecipes = Lists.newArrayList();
		for(MarketEntry entry : MarketRegistry.getEntries()) {
			marketRecipes.add(new MarketRecipe(entry));
		}
		registry.addRecipes(marketRecipes, MarketCategory.UID);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.market), MarketCategory.UID);

		registry.addAdvancedGuiHandlers(new IAdvancedGuiHandler<GuiMarket>() {
			@Override
			public Class<GuiMarket> getGuiContainerClass() {
				return GuiMarket.class;
			}

			@Override
			public List<Rectangle> getGuiExtraAreas(GuiMarket guiContainer) {
				List<Rectangle> list = Lists.newArrayList();
				for(GuiButton button : guiContainer.getFilterButtons()) {
					list.add(new Rectangle(button.xPosition, button.yPosition, button.width, button.height));
				}
				return list;
			}

			@Nullable
			@Override
			public Object getIngredientUnderMouse(GuiMarket guiContainer, int mouseX, int mouseY) {
				return null;
			}
		});
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new MarketCategory(registry.getJeiHelpers().getGuiHelper()));
	}
}
