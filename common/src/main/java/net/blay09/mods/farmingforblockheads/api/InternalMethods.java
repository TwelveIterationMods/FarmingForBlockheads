package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Map;
import java.util.Optional;

public interface InternalMethods {
    Optional<MarketCategory> getMarketCategory(ResourceLocation id);

    RecipeType<?> getMarketRecipeType();

    Map<ResourceLocation, MarketCategory> getMarketCategories();
}
