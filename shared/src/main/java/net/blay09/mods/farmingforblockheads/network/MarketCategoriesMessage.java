package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.api.MarketCategory;
import net.blay09.mods.farmingforblockheads.registry.MarketCategoryImpl;
import net.blay09.mods.farmingforblockheads.registry.MarketCategoryRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class MarketCategoriesMessage {

    private final List<MarketCategory> categories;

    public MarketCategoriesMessage(List<MarketCategory> categories) {
        this.categories = categories;
    }

    public static MarketCategoriesMessage decode(FriendlyByteBuf buf) {
        final var count = buf.readInt();
        final var categories = new ArrayList<MarketCategory>();
        for (int i = 0; i < count; i++) {
            final var registryName = buf.readResourceLocation();
            final var tooltip = buf.readComponent();
            final var iconStack = buf.readItem();
            final var sortIndex = buf.readInt();
            final var category = new MarketCategoryImpl(registryName, tooltip, iconStack, sortIndex);
            categories.add(category);
        }
        return new MarketCategoriesMessage(categories);
    }

    public static void encode(MarketCategoriesMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.categories.size());
        for (final var category : message.categories) {
            buf.writeResourceLocation(category.id());
            buf.writeComponent(category.tooltip());
            buf.writeItem(category.iconStack());
            buf.writeInt(category.sortIndex());
        }
    }

    public static void handle(Player player, MarketCategoriesMessage message) {
        MarketCategoryRegistry.INSTANCE.load(message.categories);
    }

}
