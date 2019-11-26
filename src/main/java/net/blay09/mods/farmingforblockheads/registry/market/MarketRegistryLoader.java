package net.blay09.mods.farmingforblockheads.registry.market;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryReloadEvent;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.blay09.mods.farmingforblockheads.registry.json.ItemStackSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.io.InputStreamReader;

public class MarketRegistryLoader implements IResourceManagerReloadListener {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
            .create();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        MarketRegistry.INSTANCE.reset();

        MinecraftForge.EVENT_BUS.post(new MarketRegistryReloadEvent.Pre());

        for (ResourceLocation resourceLocation : resourceManager.getAllResourceLocations("farmingforblockheads_compat", it -> it.endsWith(".json"))) {
            try (IResource resource = resourceManager.getResource(resourceLocation)) {
                InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                load(gson.fromJson(reader, MarketRegistryData.class));
            } catch (IOException e) {
                FarmingForBlockheads.logger.error("Parsing error loading Farming for Blockheads data file at {}", resourceLocation, e);
            }
        }

        MarketRegistry.INSTANCE.registerDefaults();

        MinecraftForge.EVENT_BUS.post(new MarketRegistryReloadEvent.Post());
    }

    private void load(MarketRegistryData data) {
        if (data.getEntryOverrides() != null) {
            data.getEntryOverrides().forEach(MarketRegistry.INSTANCE::registerEntryOverride);
        }

        if (data.getGroupOverrides() != null) {
            data.getGroupOverrides().forEach(MarketRegistry.INSTANCE::registerGroupOverride);
        }

        if (data.getCustomEntries() != null) {
            data.getCustomEntries().forEach(it -> {
                IMarketCategory category = MarketRegistry.getCategory(it.getCategory());
                MarketRegistry.INSTANCE.registerEntry(it.getOutput(), it.getPayment(), category);
            });
        }
    }

}
