package net.blay09.mods.farmingforblockheads.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.recipe.MarketRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class JeiMarketRecipeCategory implements IRecipeCategory<MarketRecipe> {

    public static final ResourceLocation UID = new ResourceLocation("farmingforblockheads:market");
    public static final RecipeType<MarketRecipe> TYPE = new RecipeType<>(UID, MarketRecipe.class);

    private static final ResourceLocation TEXTURE = new ResourceLocation(FarmingForBlockheads.MOD_ID, "textures/gui/jei_market.png");

    private final IDrawable icon;
    private final IDrawableStatic background;

    public JeiMarketRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.market));
        background = guiHelper.createDrawable(TEXTURE, 0, 0, 86, 48);
    }

    @Override
    public RecipeType<MarketRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei." + UID);
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
    public void setRecipe(IRecipeLayoutBuilder builder, MarketRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 13)
                .addIngredients(recipe.getPaymentOrDefault().ingredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 54, 13)
                .addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem(RegistryAccess.EMPTY));
    }

    @Override
    public void draw(MarketRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Component costText = getFormattedCostString(recipe);
        Font font = Minecraft.getInstance().font;
        int stringWidth = font.width(costText);
        guiGraphics.drawString(font, costText.getVisualOrderText(), 42 - stringWidth / 2, 35, 0xFFFFFF, true);
    }

    private Component getFormattedCostString(MarketRecipe recipe) {
        return Component.translatable("gui.farmingforblockheads.market.cost", recipe.getPaymentOrDefault().count(), recipe.getPaymentOrDefault().ingredient())
                .withStyle(ChatFormatting.GREEN);
    }
}
