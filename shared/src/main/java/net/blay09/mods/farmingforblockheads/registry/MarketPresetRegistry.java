package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.farmingforblockheads.api.MarketPreset;
import net.minecraft.resources.ResourceLocation;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MarketPresetRegistry {

    public static final MarketPresetRegistry INSTANCE = new MarketPresetRegistry();

    private final Map<ResourceLocation, MarketPreset> presets = new HashMap<>();

    public void register(MarketPreset preset) {
        presets.put(preset.id(), preset);
    }

    public Collection<MarketPreset> getAll() {
        return INSTANCE.presets.values();
    }

    public Optional<MarketPreset> get(ResourceLocation id) {
        return Optional.ofNullable(INSTANCE.presets.get(id));
    }

    public void clear() {
        presets.clear();
    }

    public void loadAdditionally(BufferedReader reader) {
        // TODO load from codec
    }
}
