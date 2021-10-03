package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.farmingforblockheads.client.FarmingForBlockheadsClient;
import net.fabricmc.api.ClientModInitializer;

public class FabricCookingForBlockheadsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FarmingForBlockheadsClient.initialize();
    }
}
