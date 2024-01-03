package net.blay09.mods.farmingforblockheads.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.blay09.mods.farmingforblockheads.recipe.MarketRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.blay09.mods.farmingforblockheads.compat.emi.EmiIntegration.BACKGROUND;
import static net.blay09.mods.farmingforblockheads.compat.emi.EmiIntegration.TRADE_ICON;

public class MarketEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;
    public MarketEmiRecipe(ResourceLocation id, MarketRecipe recipe) {
        this.id = id;
        this.input = List.of(EmiIngredient.of(recipe.getPaymentOrDefault().ingredient(), recipe.getPaymentOrDefault().count()));
        this.output = List.of(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiIntegration.MARKET_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return 86;
    }

    @Override
    public int getDisplayHeight() {
        return 48;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND, 0, 0);
        widgets.addSlot(this.input.get(0), 15, 12);
        widgets.addSlot(this.output.get(0), 53, 12).recipeContext(this);
        widgets.addTexture(TRADE_ICON, 35, 13);
    }

}
