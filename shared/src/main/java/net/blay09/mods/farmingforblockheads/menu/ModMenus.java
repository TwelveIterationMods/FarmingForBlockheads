package net.blay09.mods.farmingforblockheads.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

import java.util.HashSet;

public class ModMenus {
    public static DeferredObject<MenuType<MarketMenu>> market;

    public static void initialize(BalmMenus menus) {
        market = menus.registerMenu(id("market"), (windowId, playerInventory, data) -> {
            final var pos = data.readBlockPos();
            final var presetFilterSize = data.readInt();
            final var includeOnlyPresets = new HashSet<ResourceLocation>();
            for (int i = 0; i < presetFilterSize; i++) {
                includeOnlyPresets.add(data.readResourceLocation());
            }
            final var categoryFilterSize = data.readInt();
            final var includeOnlyCategories = new HashSet<ResourceLocation>();
            for (int i = 0; i < categoryFilterSize; i++) {
                includeOnlyCategories.add(data.readResourceLocation());
            }
            return new MarketMenu(windowId, playerInventory, pos, includeOnlyPresets, includeOnlyCategories);
        });
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(FarmingForBlockheads.MOD_ID, path);
    }
}
