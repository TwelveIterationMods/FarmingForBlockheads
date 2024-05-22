package net.blay09.mods.farmingforblockheads.menu;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {
    public static final DeferredObject<MenuType<MarketMenu>> market = Balm.getMenus().registerMenu(id("market"), new BalmMenuFactory<MarketMenu, MarketMenu.Data>() {
        @Override
        public MarketMenu create(int windowId, Inventory inventory, MarketMenu.Data data) {
            return new MarketMenu(windowId, inventory, data.pos(), data.presetFilters(), data.categoryFilters());
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MarketMenu.Data> getStreamCodec() {
            return MarketMenu.STREAM_CODEC;
        }
    });

    public static void initialize(BalmMenus menus) {
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(FarmingForBlockheads.MOD_ID, path);
    }
}
