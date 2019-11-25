package net.blay09.mods.farmingforblockheads.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.blay09.mods.farmingforblockheads.api.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class MarketRegistry {

    public static final MarketRegistry INSTANCE = new MarketRegistry();

    private final Map<ResourceLocation, IMarketCategory> indexedCategories = Maps.newHashMap();
    private final ArrayListMultimap<IMarketCategory, IMarketEntry> entries = ArrayListMultimap.create();

    private final Map<String, IMarketOverrideData> groupOverrides = Maps.newHashMap();
    private final Map<String, IMarketOverrideData> entryOverrides = Maps.newHashMap();
    private final Map<String, IMarketRegistryDefaultHandler> defaultHandlers = Maps.newHashMap();

    public void registerCategory(IMarketCategory category) {
        if (indexedCategories.containsKey(category.getRegistryName())) {
            throw new RuntimeException("Attempted to register duplicate market category " + category.getRegistryName());
        }

        indexedCategories.put(category.getRegistryName(), category);
    }

    public void registerGroupOverride(String key, IMarketOverrideData override) {
        groupOverrides.put(key, override);
    }

    public void registerEntryOverride(String key, IMarketOverrideData override) {
        entryOverrides.put(key, override);
    }

    public void registerEntry(ItemStack outputItem, ItemStack costItem, @Nullable IMarketCategory category) {
        String registryName = Objects.toString(outputItem.getItem().getRegistryName());
        IMarketOverrideData override = entryOverrides.get(registryName);
        if (category == null) {
            category = determineCategory(outputItem);
        }

        if (override == null || override.isEnabled()) {
            ItemStack payment = override != null ? override.getPayment() : costItem;
            ItemStack alteredOutputItem = override != null ? ItemHandlerHelper.copyStackWithSize(outputItem, override.getAmount()) : outputItem;
            entries.put(category, new MarketEntry(alteredOutputItem, payment, category));
        }
    }

    public void registerDefaults() {
        for (Map.Entry<String, IMarketRegistryDefaultHandler> entry : defaultHandlers.entrySet()) {
            IMarketOverrideData override = groupOverrides.get(entry.getKey());
            IMarketRegistryDefaultHandler defaultHandler = entry.getValue();
            boolean enabled = defaultHandler.isEnabledByDefault() && (override == null || override.isEnabled());
            if (enabled) {
                ItemStack payment = override != null ? override.getPayment() : defaultHandler.getDefaultPayment();
                int amount = override != null ? override.getAmount() : defaultHandler.getDefaultAmount();
                defaultHandler.register(payment, amount);
            }
        }
    }

    @Nullable
    public static IMarketEntry getEntryFor(ItemStack outputItem) {
        for (IMarketEntry entry : INSTANCE.entries.values()) {
            if (entry.getOutputItem().isItemEqual(outputItem) && ItemStack.areItemStackTagsEqual(entry.getOutputItem(), outputItem) && outputItem.getCount() == entry.getOutputItem().getCount()) {
                return entry;
            }
        }

        return null;
    }

    public static ArrayListMultimap<IMarketCategory, IMarketEntry> getGroupedEntries() {
        return INSTANCE.entries;
    }

    public static Collection<IMarketEntry> getEntries() {
        return INSTANCE.entries.values();
    }

    private static IMarketCategory determineCategory(ItemStack outputStack) {
        IMarketCategory category = FarmingForBlockheadsAPI.getMarketCategoryOther();
        ResourceLocation registryName = outputStack.getItem().getRegistryName();
        if (registryName != null) {
            if (registryName.getPath().contains("sapling")) {
                category = FarmingForBlockheadsAPI.getMarketCategorySaplings();
            } else if (registryName.getPath().contains("seed")) {
                category = FarmingForBlockheadsAPI.getMarketCategorySeeds();
            } else if (registryName.getPath().contains("flower")) {
                category = FarmingForBlockheadsAPI.getMarketCategoryFlowers();
            }
        }

        return category;
    }

    public static void registerDefaultHandler(String defaultKey, IMarketRegistryDefaultHandler handler) {
        if (INSTANCE.defaultHandlers.containsKey(defaultKey)) {
            throw new RuntimeException("Attempted to register duplicate default handler");
        }

        INSTANCE.defaultHandlers.put(defaultKey, handler);
    }

    public static Collection<IMarketCategory> getCategories() {
        return INSTANCE.indexedCategories.values();
    }

    @Nullable
    public static IMarketCategory getCategory(ResourceLocation id) {
        return INSTANCE.indexedCategories.get(id);
    }

    public void reset() {
        entryOverrides.clear();
        groupOverrides.clear();
        defaultHandlers.clear();
        indexedCategories.clear();
        entries.clear();
    }
}
