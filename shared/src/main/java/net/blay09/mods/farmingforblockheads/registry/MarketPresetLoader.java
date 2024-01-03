package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MarketPresetLoader implements ResourceManagerReloadListener {

    private static final FileToIdConverter MARKET_CATEGORIES = FileToIdConverter.json("market_presets");

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        final var registry = MarketCategoryRegistry.INSTANCE;
        registry.clear();

        for (final var entry : MARKET_CATEGORIES.listMatchingResources(resourceManager).entrySet()) {
            try (final var reader = entry.getValue().openAsReader()) {
                registry.loadAdditionally(reader);
            } catch (Exception e) {
                FarmingForBlockheads.logger.error("Error loading Farming for Blockheads market preset file at {}", entry.getKey(), e);
            }
        }

        final var configDir = new File(Balm.getConfig().getConfigDir(), "market_presets");
        if (configDir.exists() || configDir.mkdirs()) {
            try (final var files = Files.walk(configDir.toPath())) {
                files.filter(it -> it.getFileName().endsWith(".json")).forEach(it -> {
                    try (final var reader = Files.newBufferedReader(it)) {
                        registry.loadAdditionally(reader);
                    } catch (Exception e) {
                        FarmingForBlockheads.logger.error("Error loading Farming for Blockheads market preset file at {}", it, e);
                    }
                });
            } catch (IOException e) {
                FarmingForBlockheads.logger.error("Error loading Farming for Blockheads market preset files from {}", configDir, e);
            }
        }
    }

}
