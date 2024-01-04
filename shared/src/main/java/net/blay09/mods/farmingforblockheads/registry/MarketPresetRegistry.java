package net.blay09.mods.farmingforblockheads.registry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.farmingforblockheads.api.MarketPreset;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MarketPresetRegistry {

    private static final Codec<MarketPreset> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            PaymentImpl.CODEC.fieldOf("payment").forGetter(MarketPreset::payment),
            ExtraCodecs.strictOptionalField(Codec.BOOL, "enabled", true).forGetter(MarketPreset::enabledByDefault)
    ).apply(instance, MarketPresetImpl::new));

    public static final MarketPresetRegistry INSTANCE = new MarketPresetRegistry();

    private final Map<ResourceLocation, MarketPreset> presets = new HashMap<>();

    public void register(ResourceLocation id, MarketPreset preset) {
        presets.put(id, preset);
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

    public void loadAdditionally(ResourceLocation id, BufferedReader reader) {
        final var gson = new Gson();
        final var json = gson.fromJson(reader, JsonElement.class);
        final var category = Util.getOrThrow(CODEC.parse(JsonOps.INSTANCE, json), JsonParseException::new);
        register(id, category);
    }
}
