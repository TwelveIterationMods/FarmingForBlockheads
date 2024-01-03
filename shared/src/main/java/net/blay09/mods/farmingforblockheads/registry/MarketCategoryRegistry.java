package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.farmingforblockheads.api.*;
import net.minecraft.resources.ResourceLocation;

import java.io.BufferedReader;
import java.util.*;

public class MarketCategoryRegistry {

    public static final MarketCategoryRegistry INSTANCE = new MarketCategoryRegistry();

    private final Map<ResourceLocation, MarketCategory> categories = new HashMap<>();

    public void register(MarketCategory category) {
        categories.put(category.id(), category);
    }

    public Collection<MarketCategory> getAll() {
        return INSTANCE.categories.values();
    }

    public Optional<MarketCategory> get(ResourceLocation id) {
        return Optional.ofNullable(INSTANCE.categories.get(id));
    }

    public void clear() {
        categories.clear();
    }

    public void loadAdditionally(BufferedReader reader) {
        // TODO load from codec
    }

    public void load(List<MarketCategory> categories) {
        this.categories.clear();
        categories.forEach(this::register);
    }

    public void onLogin(PlayerLoginEvent event) {

    }
}
