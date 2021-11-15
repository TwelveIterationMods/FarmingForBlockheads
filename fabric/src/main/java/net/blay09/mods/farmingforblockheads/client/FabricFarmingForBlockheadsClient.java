package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.fabricmc.api.ClientModInitializer;

public class FabricFarmingForBlockheadsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BalmClient.initialize(FarmingForBlockheads.MOD_ID, FarmingForBlockheadsClient::initialize);
    }
}
