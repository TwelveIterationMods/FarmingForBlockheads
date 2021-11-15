package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.fabricmc.api.ModInitializer;

public class FabricFarmingForBlockheads implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initialize(FarmingForBlockheads.MOD_ID, FarmingForBlockheads::initialize);
    }
}
