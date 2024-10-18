package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.menu.MarketMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;

import java.util.ArrayList;
import java.util.List;

public class MarketRecipesMessage implements CustomPacketPayload {

    public static final Type<MarketRecipesMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(
            FarmingForBlockheads.MOD_ID,
            "market_recipes"));

    private final List<RecipeDisplayEntry> recipes;

    public MarketRecipesMessage(List<RecipeDisplayEntry> recipes) {
        this.recipes = recipes;
    }

    public static MarketRecipesMessage decode(RegistryFriendlyByteBuf buf) {
        final var count = buf.readInt();
        final var recipes = new ArrayList<RecipeDisplayEntry>();
        for (int i = 0; i < count; i++) {
            final var entry = RecipeDisplayEntry.STREAM_CODEC.decode(buf);
            recipes.add(entry);
        }
        return new MarketRecipesMessage(recipes);
    }

    public static void encode(RegistryFriendlyByteBuf buf, MarketRecipesMessage message) {
        buf.writeInt(message.recipes.size());
        message.recipes.forEach((recipe) -> RecipeDisplayEntry.STREAM_CODEC.encode(buf, recipe));
    }

    public static void handle(Player player, MarketRecipesMessage message) {
        if (player.containerMenu instanceof MarketMenu marketMenu) {
            marketMenu.setRecipes(message.recipes);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
