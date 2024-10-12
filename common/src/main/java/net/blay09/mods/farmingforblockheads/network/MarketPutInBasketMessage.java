package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.menu.MarketMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;

public class MarketPutInBasketMessage implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MarketPutInBasketMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(
            FarmingForBlockheads.MOD_ID,
            "market_put_in_basket"));

    private final RecipeDisplayId recipeDisplayId;
    private final boolean stack;

    public MarketPutInBasketMessage(RecipeDisplayId recipeDisplayId, boolean stack) {
        this.recipeDisplayId = recipeDisplayId;
        this.stack = stack;
    }

    public static void encode(FriendlyByteBuf buf, MarketPutInBasketMessage message) {
        RecipeDisplayId.STREAM_CODEC.encode(buf, message.recipeDisplayId);
        buf.writeBoolean(message.stack);
    }

    public static MarketPutInBasketMessage decode(FriendlyByteBuf buf) {
        final var recipeDisplayId = RecipeDisplayId.STREAM_CODEC.decode(buf);
        boolean stack = buf.readBoolean();
        return new MarketPutInBasketMessage(recipeDisplayId, stack);
    }

    public static void handle(ServerPlayer player, MarketPutInBasketMessage message) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof MarketMenu marketMenu) {
            marketMenu.selectMarketEntry(message.recipeDisplayId, message.stack);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
