package net.blay09.mods.farmingforblockheads.fabric;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.fabricmc.api.ModInitializer;

public class FabricFarmingForBlockheads implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initialize(FarmingForBlockheads.MOD_ID, FarmingForBlockheads::initialize);
    }
}
