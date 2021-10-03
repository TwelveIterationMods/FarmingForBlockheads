package net.blay09.mods.farmingforblockheads.client;

import net.fabricmc.api.ClientModInitializer;

public class FabricFarmingForBlockheadsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FarmingForBlockheadsClient.initialize();
    }
}
