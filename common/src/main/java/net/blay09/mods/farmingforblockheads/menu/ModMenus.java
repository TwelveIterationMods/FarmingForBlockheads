package net.blay09.mods.farmingforblockheads.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {
    public static DeferredObject<MenuType<MarketMenu>> market;

    public static void initialize(BalmMenus menus) {
        market = menus.registerMenu(id("market"), new BalmMenuFactory<MarketMenu, MarketMenuData>() {
            @Override
            public MarketMenu create(int windowId, Inventory inventory, MarketMenuData data) {
                return new MarketMenu(windowId, inventory, data.pos(), data.presetFilters(), data.categoryFilters());
            }

            @Override
            public StreamCodec<FriendlyByteBuf, MarketMenuData> getCodec() {
                return MarketMenu.CODEC;
            }
        });
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(FarmingForBlockheads.MOD_ID, path);
    }
}
