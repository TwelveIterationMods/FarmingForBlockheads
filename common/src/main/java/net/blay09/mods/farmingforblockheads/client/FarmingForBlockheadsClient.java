package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.client.ItemTooltipEvent;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.api.Payment;
import net.blay09.mods.farmingforblockheads.client.gui.screen.MarketScreen;
import net.blay09.mods.farmingforblockheads.menu.MarketBasketSlot;
import net.blay09.mods.farmingforblockheads.menu.MarketListingSlot;
import net.blay09.mods.farmingforblockheads.recipe.MarketRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.RecipeHolder;

public class FarmingForBlockheadsClient {

    public static void initialize() {
        ModScreens.initialize(BalmClient.getScreens());
        ModRenderers.initialize(BalmClient.getRenderers());

        Balm.getEvents().onEvent(ItemTooltipEvent.class, event -> {
            if (Minecraft.getInstance().screen instanceof MarketScreen screen) {
                Slot hoverSlot = ((AbstractContainerScreenAccessor) screen).getHoveredSlot();
                if (hoverSlot != null && event.getItemStack() == hoverSlot.getItem()) {
                    RecipeHolder<MarketRecipe> hoverRecipe = null;

                    if (hoverSlot instanceof MarketListingSlot marketListingSlot) {
                        hoverRecipe = marketListingSlot.getRecipe();
                    } else if (hoverSlot instanceof MarketBasketSlot) {
                        hoverRecipe = screen.getMenu().getSelectedRecipe();
                    }

                    if (hoverRecipe != null) {
                        final var payment = hoverRecipe.value().getPaymentOrDefault();
                        final var paymentComponent = payment.tooltip().orElseGet(() -> FarmingForBlockheads.getDefaultPaymentComponent(payment));
                        final var tooltipComponent = Component.translatable("tooltip.farmingforblockheads.payment", paymentComponent)
                                .withStyle(ChatFormatting.GREEN);
                        event.getToolTip().add(tooltipComponent);
                    }
                }
            }
        });
    }

}
