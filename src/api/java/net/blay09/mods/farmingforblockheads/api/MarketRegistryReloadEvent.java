package net.blay09.mods.farmingforblockheads.api;

import net.minecraftforge.eventbus.api.Event;

public abstract class MarketRegistryReloadEvent extends Event {
    public static class Pre extends MarketRegistryReloadEvent {
        public void registerMarketDefaultHandler(String key, IMarketRegistryDefaultHandler handler) {
            FarmingForBlockheadsAPI.registerMarketDefaultHandler(key, handler);
        }
    }

    public static class Post extends MarketRegistryReloadEvent {
    }
}
