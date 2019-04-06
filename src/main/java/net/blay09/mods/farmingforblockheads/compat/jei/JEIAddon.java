package net.blay09.mods.farmingforblockheads.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.client.gui.GuiMarket;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

@JeiPlugin
public class JEIAddon implements IModPlugin {

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(MarketRegistry.getEntries(), MarketCategory.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.market), MarketCategory.UID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(GuiMarket.class, new IGuiContainerHandler<GuiMarket>() {
            @Override
            public List<Rectangle2d> getGuiExtraAreas(GuiMarket guiContainer) {
                List<Rectangle2d> list = Lists.newArrayList();
                for (GuiButton button : guiContainer.getFilterButtons()) {
                    list.add(new Rectangle2d(button.x, button.y, button.width, button.height));
                }
                return list;
            }
        });
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new MarketCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("farmingforblockheads", "jei");
    }
}
