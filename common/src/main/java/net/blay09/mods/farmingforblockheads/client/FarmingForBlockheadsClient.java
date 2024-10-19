package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.client.ItemTooltipEvent;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.farmingforblockheads.client.gui.screen.MarketScreen;
import net.blay09.mods.farmingforblockheads.menu.MarketResultSlot;
import net.blay09.mods.farmingforblockheads.menu.MarketListingSlot;
import net.blay09.mods.farmingforblockheads.recipe.MarketRecipeDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;

public class FarmingForBlockheadsClient {

    public static void initialize() {
        ModScreens.initialize(BalmClient.getScreens());
        ModRenderers.initialize(BalmClient.getRenderers());

        Balm.getEvents().onEvent(ItemTooltipEvent.class, event -> {
            // TODO if (Minecraft.getInstance().screen instanceof MarketScreen screen) {
            // TODO     Slot hoverSlot = ((AbstractContainerScreenAccessor) screen).getHoveredSlot();
            // TODO     if (hoverSlot != null && event.getItemStack() == hoverSlot.getItem()) {
            // TODO         RecipeDisplayEntry hoverRecipe = null;
// TODO
            // TODO         if (hoverSlot instanceof MarketListingSlot marketListingSlot) {
            // TODO             hoverRecipe = marketListingSlot.getRecipeDisplayEntry();
            // TODO         } else if (hoverSlot instanceof MarketResultSlot) {
            // TODO             hoverRecipe = screen.getMenu().getSelectedRecipe();
            // TODO         }
// TODO
            // TODO         if (hoverRecipe != null && hoverRecipe.display() instanceof MarketRecipeDisplay marketRecipeDisplay) {
            // TODO             // TODO final var paymentComponent = payment.tooltip().orElseGet(() -> FarmingForBlockheads.getDefaultPaymentComponent(payment));
            // TODO             // TODO final var tooltipComponent = Component.translatable("tooltip.farmingforblockheads.payment", paymentComponent)
            // TODO             // TODO         .withStyle(ChatFormatting.GREEN);
            // TODO             // TODO event.getToolTip().add(tooltipComponent);
            // TODO         }
            // TODO     }
            // TODO }
        });
    }

}
