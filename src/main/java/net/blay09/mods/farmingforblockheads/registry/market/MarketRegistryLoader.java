package net.blay09.mods.farmingforblockheads.registry.market;

import com.google.gson.Gson;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class MarketRegistryLoader implements IResourceManagerReloadListener {

    private static final Gson gson = new Gson();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        for (ResourceLocation resourceLocation : resourceManager.getAllResourceLocations("cookingforblockheads_compat", it -> it.endsWith(".json"))) {
            try (IResource resource = resourceManager.getResource(resourceLocation)) {
                InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                load(gson.fromJson(reader, MarketRegistryData.class));
            } catch (IOException e) {
                FarmingForBlockheads.logger.error("Parsing error loading Farming for Blockheads data file at {}", resourceLocation, e);
            }
        }
    }

    private void load(MarketRegistryData data) {

    }

    private static Optional<ItemStack> findItemStack(ResourceLocation registryName) {
        Item item = ForgeRegistries.ITEMS.getValue(registryName);
        return item == null || item == Items.AIR ? Optional.empty() : Optional.of(new ItemStack(item));
    }
}
