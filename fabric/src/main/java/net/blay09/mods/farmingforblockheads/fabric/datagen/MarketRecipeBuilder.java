package net.blay09.mods.farmingforblockheads.fabric.datagen;

import net.blay09.mods.farmingforblockheads.api.Payment;
import net.blay09.mods.farmingforblockheads.recipe.MarketRecipe;
import net.blay09.mods.farmingforblockheads.registry.PaymentImpl;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class MarketRecipeBuilder implements RecipeBuilder {

    private final ResourceLocation category;
    private final ResourceLocation preset;
    private final Item result;
    private final int count;
    private Payment payment;

    public MarketRecipeBuilder(ResourceLocation category, ResourceLocation preset, ItemLike result, int count) {
        this.category = category;
        this.preset = preset;
        this.result = result.asItem();
        this.count = count;
    }

    @Override
    public RecipeBuilder unlockedBy(String string, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String string) {
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        final var marketRecipe = new MarketRecipe(new ItemStack(result, count), category, preset, payment);
        recipeOutput.accept(resourceLocation, marketRecipe, null);
    }

    @Override
    public void save(RecipeOutput recipeOutput) {
        save(recipeOutput, getDefaultRecipeId(getResult()));
    }

    @Override
    public void save(RecipeOutput recipeOutput, String string) {
        ResourceLocation defaultRecipeId = getDefaultRecipeId(this.getResult());
        ResourceLocation recipeId = new ResourceLocation(string);
        if (recipeId.equals(defaultRecipeId)) {
            throw new IllegalStateException("Recipe " + string + " should remove its 'save' argument as it is equal to default one");
        } else {
            save(recipeOutput, recipeId);
        }
    }

    private static ResourceLocation getDefaultRecipeId(ItemLike itemLike) {
        final var itemId = BuiltInRegistries.ITEM.getKey(itemLike.asItem());
        return new ResourceLocation(itemId.getNamespace(), "market/" + itemId.getNamespace() + "/" + itemId.getPath());
    }

    public static MarketRecipeBuilder market(ResourceLocation category, ResourceLocation preset, ItemLike result) {
        return market(category, preset, result, 1);
    }

    public static MarketRecipeBuilder market(ResourceLocation category, ResourceLocation preset, ItemLike result, int count) {
        return new MarketRecipeBuilder(category, preset, result, count);
    }

    public RecipeBuilder costs(ItemLike payment, int count) {
        this.payment = new PaymentImpl(Ingredient.of(payment), count);
        return this;
    }
}
