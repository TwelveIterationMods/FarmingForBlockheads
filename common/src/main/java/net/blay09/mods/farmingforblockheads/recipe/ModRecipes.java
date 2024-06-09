package net.blay09.mods.farmingforblockheads.recipe;

import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {

    public static final String MARKET_RECIPE_GROUP = "market";
    public static final ResourceLocation MARKET_RECIPE_TYPE = ResourceLocation.fromNamespaceAndPath(FarmingForBlockheads.MOD_ID, MARKET_RECIPE_GROUP);

    public static RecipeType<MarketRecipe> marketRecipeType;
    public static RecipeSerializer<MarketRecipe> marketRecipeSerializer;

    public static void initialize(BalmRecipes registry) {
        registry.registerRecipeType(() -> marketRecipeType = new RecipeType<>() {
                    @Override
                    public String toString() {
                        return MARKET_RECIPE_GROUP;
                    }
                },
                () -> marketRecipeSerializer = new MarketRecipe.Serializer(), MARKET_RECIPE_TYPE);
    }
}
