package net.blay09.mods.farmingforblockheads.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class MarketCategory implements IRecipeCategory<IMarketEntry> {

    public static final ResourceLocation UID = new ResourceLocation("farmingforblockheads:market");

    private static final ResourceLocation TEXTURE = new ResourceLocation(FarmingForBlockheads.MOD_ID, "textures/gui/jei_market.png");

    private final IDrawable icon;
    private final IDrawableStatic background;

    public MarketCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.market));
        background = guiHelper.createDrawable(TEXTURE, 0, 0, 86, 48);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends IMarketEntry> getRecipeClass() {
        return IMarketEntry.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("jei." + UID);
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(IMarketEntry marketRecipe, IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, marketRecipe.getCostItem());
        ingredients.setOutput(VanillaTypes.ITEM, marketRecipe.getOutputItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IMarketEntry recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 15, 12);
        recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        recipeLayout.getItemStacks().init(1, false, 53, 12);
        recipeLayout.getItemStacks().set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }

    @Override
    public void draw(IMarketEntry recipe, double mouseX, double mouseY) {
        String costText = getFormattedCostString(recipe);
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        int stringWidth = fontRenderer.getStringWidth(costText);
        fontRenderer.drawStringWithShadow(costText, 42 - stringWidth / 2f, 35, 0xFFFFFF);
    }

    private String getFormattedCostString(IMarketEntry entry) {
        String color = TextFormatting.GREEN.toString();
        if (entry.getCostItem().getItem() == Items.DIAMOND) {
            color = TextFormatting.AQUA.toString();
        }
        return color + I18n.format("gui.farmingforblockheads:market.cost",
                entry.getCostItem().getCount(),
                entry.getCostItem().getDisplayName().getFormattedText());
    }
}
