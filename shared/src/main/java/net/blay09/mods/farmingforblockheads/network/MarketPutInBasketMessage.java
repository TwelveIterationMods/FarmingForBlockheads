package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.menu.MarketMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class MarketPutInBasketMessage {

    private final ResourceLocation recipeId;
    private final boolean stack;

    public MarketPutInBasketMessage(ResourceLocation recipeId, boolean stack) {
        this.recipeId = recipeId;
        this.stack = stack;
    }

    public static void encode(MarketPutInBasketMessage message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.recipeId);
        buf.writeBoolean(message.stack);
    }

    public static MarketPutInBasketMessage decode(FriendlyByteBuf buf) {
        final var recipeId = buf.readResourceLocation();
        boolean stack = buf.readBoolean();
        return new MarketPutInBasketMessage(recipeId, stack);
    }

    public static void handle(ServerPlayer player, MarketPutInBasketMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof MarketMenu marketMenu) {
            marketMenu.selectMarketEntry(message.recipeId, message.stack);
        }
    }

}
