package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.api.MarketCategory;
import net.blay09.mods.farmingforblockheads.registry.MarketCategoryImpl;
import net.blay09.mods.farmingforblockheads.registry.MarketCategoryRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class MarketCategoriesMessage {

    private final Map<ResourceLocation, MarketCategory> categories;

    public MarketCategoriesMessage(Map<ResourceLocation, MarketCategory> categories) {
        this.categories = categories;
    }

    public static MarketCategoriesMessage decode(FriendlyByteBuf buf) {
        final var count = buf.readInt();
        final var categories = new HashMap<ResourceLocation, MarketCategory>();
        for (int i = 0; i < count; i++) {
            final var id = buf.readResourceLocation();
            final var iconStack = buf.readItem();
            final var sortIndex = buf.readInt();
            final var tooltip = buf.readComponent();
            final var category = new MarketCategoryImpl(iconStack, sortIndex, tooltip);
            categories.put(id, category);
        }
        return new MarketCategoriesMessage(categories);
    }

    public static void encode(MarketCategoriesMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.categories.size());
        message.categories.forEach((id, category) -> {
            buf.writeResourceLocation(id);
            buf.writeItem(category.iconStack());
            buf.writeInt(category.sortIndex());
            buf.writeComponent(category.tooltip());
        });
    }

    public static void handle(Player player, MarketCategoriesMessage message) {
        MarketCategoryRegistry.INSTANCE.load(message.categories);
    }

}
