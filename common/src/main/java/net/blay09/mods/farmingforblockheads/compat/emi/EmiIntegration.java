package net.blay09.mods.farmingforblockheads.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.client.gui.screen.MarketScreen;
import net.blay09.mods.farmingforblockheads.recipe.ModRecipes;
import net.blay09.mods.farmingforblockheads.registry.MarketPresetRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static net.blay09.mods.farmingforblockheads.FarmingForBlockheads.MOD_ID;

@EmiEntrypoint
public class EmiIntegration implements EmiPlugin {
    public static final EmiTexture TRADE_ICON = new EmiTexture(ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/market.png"),
            240, 240, 16, 16);
    public static final EmiTexture BACKGROUND = new EmiTexture(ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/market.png"),
            154, 208, 86, 48);

    public static final EmiStack MARKET = EmiStack.of(ModBlocks.market);
    public static final EmiRecipeCategory MARKET_CATEGORY =
            new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath("farmingforblockheads", "market"), MARKET, TRADE_ICON) {
                @Override
                public Component getName() {
                    return Component.translatable("jei.farmingforblockheads.market");
                }
            };

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(MARKET_CATEGORY);
        registry.addWorkstation(MARKET_CATEGORY, MARKET);

        final var marketRecipes = registry.getRecipeManager().getAllRecipesFor(ModRecipes.marketRecipeType);
        for (final var marketRecipe : marketRecipes) {
            if (MarketPresetRegistry.isRecipeEnabled(marketRecipe.value())) {
                registry.addRecipe(new MarketEmiRecipe(marketRecipe.id(), marketRecipe.value()));
            }
        }

        registry.addExclusionArea(MarketScreen.class, (screen, consumer) ->
                screen.getFilterButtons().forEach(b ->
                        consumer.accept(new Bounds(b.getX(), b.getY(), b.getWidth(), b.getHeight()))));
    }
}
