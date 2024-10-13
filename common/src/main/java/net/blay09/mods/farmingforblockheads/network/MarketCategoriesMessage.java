package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.api.MarketCategory;
import net.blay09.mods.farmingforblockheads.menu.MarketMenu;
import net.blay09.mods.farmingforblockheads.registry.MarketCategoryImpl;
import net.blay09.mods.farmingforblockheads.registry.SimpleHolder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MarketCategoriesMessage implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MarketCategoriesMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(
            FarmingForBlockheads.MOD_ID,
            "market_categories"));

    private final Map<ResourceLocation, MarketCategory> categories;

    public MarketCategoriesMessage(Map<ResourceLocation, MarketCategory> categories) {
        this.categories = categories;
    }

    public static MarketCategoriesMessage decode(RegistryFriendlyByteBuf buf) {
        final var count = buf.readInt();
        final var categories = new HashMap<ResourceLocation, MarketCategory>();
        for (int i = 0; i < count; i++) {
            final var id = buf.readResourceLocation();
            final var iconStack = ItemStack.STREAM_CODEC.decode(buf);
            final var sortIndex = buf.readInt();
            final var tooltip = ComponentSerialization.TRUSTED_STREAM_CODEC.decode(buf);
            final var category = new MarketCategoryImpl(iconStack, sortIndex, tooltip);
            categories.put(id, category);
        }
        return new MarketCategoriesMessage(categories);
    }

    public static void encode(RegistryFriendlyByteBuf buf, MarketCategoriesMessage message) {
        buf.writeInt(message.categories.size());
        message.categories.forEach((id, category) -> {
            buf.writeResourceLocation(id);
            ItemStack.STREAM_CODEC.encode(buf, category.iconStack());
            buf.writeInt(category.sortIndex());
            ComponentSerialization.TRUSTED_STREAM_CODEC.encode(buf, category.tooltip());
        });
    }

    public static void handle(Player player, MarketCategoriesMessage message) {
        if (player.containerMenu instanceof MarketMenu marketMenu) {
            marketMenu.setCategories(message.categories.entrySet().stream().map(SimpleHolder::new).toList());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
