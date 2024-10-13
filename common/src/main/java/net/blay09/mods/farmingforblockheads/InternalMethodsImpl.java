package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.api.MarketCategory;
import net.blay09.mods.farmingforblockheads.api.InternalMethods;
import net.blay09.mods.farmingforblockheads.recipe.ModRecipes;
import net.blay09.mods.farmingforblockheads.registry.MarketCategoryRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Map;
import java.util.Optional;

public class InternalMethodsImpl implements InternalMethods {
    @Override
    public Optional<MarketCategory> getMarketCategory(ResourceLocation registryName) {
        return MarketCategoryRegistry.INSTANCE.get(registryName);
    }

    @Override
    public RecipeType<?> getMarketRecipeType() {
        return ModRecipes.marketRecipeType;
    }

    @Override
    public Map<ResourceLocation, MarketCategory> getMarketCategories() {
        return MarketCategoryRegistry.INSTANCE.getAll();
    }
}
