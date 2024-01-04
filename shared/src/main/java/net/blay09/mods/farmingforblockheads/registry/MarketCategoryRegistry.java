package net.blay09.mods.farmingforblockheads.registry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.farmingforblockheads.api.*;
import net.blay09.mods.farmingforblockheads.network.MarketCategoriesMessage;
import net.minecraft.Util;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

import java.io.BufferedReader;
import java.util.*;

public class MarketCategoryRegistry {

    private static final Codec<MarketCategory> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ItemStack.ADVANCEMENT_ICON_CODEC.fieldOf("icon").forGetter(MarketCategory::iconStack),
            ExtraCodecs.strictOptionalField(ExtraCodecs.POSITIVE_INT, "sortIndex", 0).forGetter(MarketCategory::sortIndex),
            ComponentSerialization.CODEC.fieldOf("tooltip").forGetter(MarketCategory::tooltip)
    ).apply(instance, MarketCategoryImpl::new));

    public static final MarketCategoryRegistry INSTANCE = new MarketCategoryRegistry();

    private final Map<ResourceLocation, MarketCategory> categories = new HashMap<>();

    public void register(ResourceLocation id, MarketCategory category) {
        categories.put(id, category);
    }

    public Map<ResourceLocation, MarketCategory> getAll() {
        return INSTANCE.categories;
    }

    public Optional<MarketCategory> get(ResourceLocation id) {
        return Optional.ofNullable(INSTANCE.categories.get(id));
    }

    public void clear() {
        categories.clear();
    }

    public void loadAdditionally(ResourceLocation id, BufferedReader reader) {
        final var gson = new Gson();
        final var json = gson.fromJson(reader, JsonElement.class);
        final var category = Util.getOrThrow(CODEC.parse(JsonOps.INSTANCE, json), JsonParseException::new);
        register(id, category);
    }

    public void load(Map<ResourceLocation, MarketCategory> categories) {
        this.categories.clear();
        categories.forEach(this::register);
    }

    public void onLogin(PlayerLoginEvent event) {
        Balm.getNetworking().sendTo(event.getPlayer(), new MarketCategoriesMessage(categories));
    }
}
