package net.blay09.mods.farmingforblockheads.registry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheadsConfig;
import net.blay09.mods.farmingforblockheads.api.MarketDefault;
import net.blay09.mods.farmingforblockheads.api.Payment;
import net.blay09.mods.farmingforblockheads.recipe.MarketRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.io.BufferedReader;
import java.util.*;

public class MarketDefaultsRegistry {

    private static final Codec<MarketDefault> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("category").forGetter(MarketDefault::category),
            PaymentImpl.CODEC.optionalFieldOf("payment").forGetter(MarketDefault::payment)
    ).apply(instance, MarketDefaultImpl::new));

    public static final MarketDefaultsRegistry INSTANCE = new MarketDefaultsRegistry();

    private final Map<ResourceLocation, MarketDefault> presets = new HashMap<>();

    public void register(ResourceLocation id, MarketDefault preset) {
        presets.put(id, preset);
    }

    public Collection<MarketDefault> getAll() {
        return INSTANCE.presets.values();
    }

    public Optional<MarketDefault> get(ResourceLocation id) {
        return Optional.ofNullable(INSTANCE.presets.get(id));
    }

    public void clear() {
        presets.clear();
    }

    public void loadAdditionally(ResourceLocation id, BufferedReader reader) {
        final var gson = new Gson();
        final var json = gson.fromJson(reader, JsonElement.class);
        final var category = CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
        register(id, category);
    }

    public static MarketDefault resolveExactDefault(String defaults) {
        // TODO
        return new MarketDefaultImpl(Optional.empty(), Optional.empty());
    }

    public static List<String> flattenDefaults(String defaults) {
        final var result = new ArrayList<String>();
        final var parts = defaults.split("\\.");
        for (int i = 0; i < parts.length; i++) {
            final var sb = new StringBuilder();
            for (int j = 0; j <= i; j++) {
                if (j > 0) {
                    sb.append('.');
                }
                sb.append(parts[j]);
            }
            final var key = sb.toString();
            result.add(key);
        }
        return result;
    }

    public static MarketDefault resolveDefaults(MarketRecipe recipe) {
        final var result = flattenDefaults(recipe.getDefaults()).stream().map(MarketDefaultsRegistry::resolveExactDefault).toList();
        return new CompositeMarketDefault(result);
    }

    public static ResourceLocation resolveCategory(MarketRecipe recipe) {
        final var defaults = resolveDefaults(recipe);
        return recipe.getCategory().orElse(defaults.category().orElseGet(MarketDefaultsRegistry::defaultCategory));
    }

    public static Payment resolvePayment(MarketRecipe recipe) {
        final var defaults = resolveDefaults(recipe);
        return recipe.getPayment().orElse(defaults.payment().orElseGet(MarketDefaultsRegistry::defaultPayment));
    }

    public static boolean isEnabled(MarketRecipe recipe) {
        final var recipeGroups = flattenDefaults(recipe.getDefaults());
        final var includedGroups = FarmingForBlockheadsConfig.getActive().includedGroups;
        final var excludedGroups = FarmingForBlockheadsConfig.getActive().excludedGroups;
        final var useDefaultIncludedGroups = includedGroups.contains("default") && !excludedGroups.contains("default");
        var enabled = false;
        for (final var group : recipeGroups.reversed()) {
            if (excludedGroups.contains(group)) {
                break;
            } else if (includedGroups.contains(group)) {
                enabled = true;
                break;
            } else if (useDefaultIncludedGroups && FarmingForBlockheadsConfig.DEFAULT_INCLUDED_GROUPS.contains(group)) {
                enabled = true;
                break;
            }
        }

        return enabled;
    }

    private static ResourceLocation defaultCategory() {
        return ResourceLocation.fromNamespaceAndPath(FarmingForBlockheads.MOD_ID, "other");
    }

    private static Payment defaultPayment() {
        return new PaymentImpl(Ingredient.of(Items.EMERALD), 1, Optional.empty());
    }
}
