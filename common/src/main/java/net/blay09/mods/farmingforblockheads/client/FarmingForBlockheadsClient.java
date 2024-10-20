package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.balm.api.client.BalmClient;

public class FarmingForBlockheadsClient {

    public static void initialize() {
        ModScreens.initialize(BalmClient.getScreens());
        ModRenderers.initialize(BalmClient.getRenderers());
    }

}
