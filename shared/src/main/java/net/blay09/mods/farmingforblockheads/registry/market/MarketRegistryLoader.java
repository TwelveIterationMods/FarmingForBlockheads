package net.blay09.mods.farmingforblockheads.registry.market;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketRegistryDefaultHandler;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryReloadEvent;
import net.blay09.mods.farmingforblockheads.registry.MarketCategory;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.blay09.mods.farmingforblockheads.registry.json.ItemStackSerializer;
import net.blay09.mods.farmingforblockheads.registry.json.MarketRegistryDataSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MarketRegistryLoader implements ResourceManagerReloadListener {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
            .registerTypeAdapter(MarketRegistryData.class, new MarketRegistryDataSerializer())
            .create();

    private static final List<Exception> registryErrors = new ArrayList<>();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        try {
            MarketRegistry.INSTANCE.reset();
            registryErrors.clear();

            Balm.getEvents().fireEvent(new MarketRegistryReloadEvent.Pre());

            for (ResourceLocation resourceLocation : resourceManager.listResources("farmingforblockheads_compat", it -> it.endsWith(".json"))) {
                try (Resource resource = resourceManager.getResource(resourceLocation)) {
                    InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                    load(gson.fromJson(reader, MarketRegistryData.class));
                } catch (Exception e) {
                    FarmingForBlockheads.logger.error("Parsing error loading Farming for Blockheads data file at {}", resourceLocation, e);
                    registryErrors.add(e);
                }
            }

            File configDir = new File(Balm.getConfig().getConfigDir(), "farmingforblockheads");
            if (configDir.exists() || configDir.mkdirs()) {
                File configFile = new File(configDir, "MarketRegistry.json");
                if (configFile.exists()) {
                    try (FileReader reader = new FileReader(configFile)) {
                        load(gson.fromJson(reader, MarketRegistryData.class));
                    } catch (Exception e) {
                        FarmingForBlockheads.logger.error("Parsing error loading Farming for Blockheads data from MarketRegistry.json", e);
                        registryErrors.add(e);
                    }
                } else {
                    try (FileWriter writer = new FileWriter(configFile)) {
                        gson.toJson(new MarketRegistryData(), writer);
                    } catch (IOException ignored) {
                    }
                }
            }

            MarketRegistry.INSTANCE.registerDefaults();

            Balm.getEvents().fireEvent(new MarketRegistryReloadEvent.Post());
        } catch (Exception e) {
            FarmingForBlockheads.logger.error("Exception loading Farming for Blockheads data", e);
            registryErrors.add(e);
        }
    }

    private void load(@Nullable MarketRegistryData data) {
        if (data == null) {
            return;
        }

        if (data.getModId() != null && !data.getModId().equals("minecraft") && !Balm.isModLoaded(data.getModId())) {
            return;
        }

        if (data.getGroup() != null) {
            FarmingForBlockheadsAPI.registerMarketDefaultHandler(data.getGroup().getName(), new IMarketRegistryDefaultHandler() {
                @Override
                public void register(@Nullable ItemStack overridePayment, @Nullable Integer overrideCount) {
                    ItemStack effectiveDefaultPayment = data.getGroup().getDefaultPayment();
                    if (effectiveDefaultPayment == null) {
                        effectiveDefaultPayment = getDefaultPayment();
                    }

                    if (overridePayment != null) {
                        effectiveDefaultPayment = overridePayment;
                    }

                    loadMarketData(data, effectiveDefaultPayment, overrideCount);
                }

                @Override
                public boolean isEnabledByDefault() {
                    return data.getGroup().isEnabledByDefault();
                }

                @Override
                public ItemStack getDefaultPayment() {
                    return new ItemStack(Items.EMERALD);
                }
            });
        } else {
            loadMarketData(data, new ItemStack(Items.EMERALD), null);
        }
    }

    private void loadMarketData(MarketRegistryData data, ItemStack defaultPayment, @Nullable Integer overrideCount) {
        if (data.getCustomCategories() != null) {
            data.getCustomCategories().forEach((key, categoryData) -> {
                ResourceLocation resourceLocation = new ResourceLocation(key);
                MarketRegistry.INSTANCE.registerCategory(new MarketCategory(resourceLocation, categoryData.getName(), categoryData.getIcon(), categoryData.getSortIndex()));
            });
        }

        if (data.getEntryOverrides() != null) {
            data.getEntryOverrides().forEach(MarketRegistry.INSTANCE::registerEntryOverride);
        }

        if (data.getGroupOverrides() != null) {
            data.getGroupOverrides().forEach(MarketRegistry.INSTANCE::registerGroupOverride);
        }

        if (data.getCustomEntries() != null) {
            data.getCustomEntries().forEach(it -> {
                ResourceLocation categoryKey = it.getCategory();
                if (categoryKey == null) {
                    if (data.getGroup() != null) {
                        categoryKey = data.getGroup().getDefaultCategory();
                    } else {
                        categoryKey = new ResourceLocation("farmingforblockheads:other");
                    }
                }
                IMarketCategory category = MarketRegistry.getCategory(categoryKey);

                ItemStack effectivePayment = it.getPayment();
                if (effectivePayment == null) {
                    effectivePayment = defaultPayment;
                }

                ItemStack effectiveOutput = it.getOutput();
                if (overrideCount != null) {
                    effectiveOutput = ContainerUtils.copyStackWithSize(effectiveOutput, overrideCount);
                }
                MarketRegistry.INSTANCE.registerEntry(effectiveOutput, effectivePayment, category);
            });
        }
    }

    public static void onLogin(PlayerLoginEvent event) {
        if (!registryErrors.isEmpty()) {
            event.getPlayer().displayClientMessage(getErrorTextComponent("There were registry errors in the FarmingForBlockheads market data. See the log for full details."), false);
            for (Exception registryError : registryErrors) {
                event.getPlayer().displayClientMessage(getErrorTextComponent("- " + registryError.getMessage()), false);
            }
        }
    }

    private static TextComponent getErrorTextComponent(String message) {
        TextComponent result = new TextComponent(message);
        result.withStyle(ChatFormatting.RED);
        return result;
    }
}
