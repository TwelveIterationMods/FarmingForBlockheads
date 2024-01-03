package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.*;
import java.nio.file.Files;

public class MarketCategoryLoader implements ResourceManagerReloadListener {

    private static final FileToIdConverter MARKET_CATEGORIES = FileToIdConverter.json("market_categories");

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        final var registry = MarketCategoryRegistry.INSTANCE;
        registry.clear();

        for (final var entry : MARKET_CATEGORIES.listMatchingResources(resourceManager).entrySet()) {
            try (final var reader = entry.getValue().openAsReader()) {
                registry.loadAdditionally(reader);
            } catch (Exception e) {
                FarmingForBlockheads.logger.error("Error loading Farming for Blockheads market category file at {}", entry.getKey(), e);
            }
        }

        final var configDir = new File(Balm.getConfig().getConfigDir(), "market_categories");
        if (configDir.exists() || configDir.mkdirs()) {
            try (final var files = Files.walk(configDir.toPath())) {
                files.filter(it -> it.getFileName().endsWith(".json")).forEach(it -> {
                    try (final var reader = Files.newBufferedReader(it)) {
                        registry.loadAdditionally(reader);
                    } catch (Exception e) {
                        FarmingForBlockheads.logger.error("Error loading Farming for Blockheads market category file at {}", it, e);
                    }
                });
            } catch (IOException e) {
                FarmingForBlockheads.logger.error("Error loading Farming for Blockheads market category files from {}", configDir, e);
            }
        }
    }

    public void onLogin(PlayerLoginEvent event) {

    }
}
