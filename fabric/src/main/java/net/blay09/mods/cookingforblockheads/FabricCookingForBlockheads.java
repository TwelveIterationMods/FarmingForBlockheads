package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.fabricmc.api.ModInitializer;

public class FabricCookingForBlockheads implements ModInitializer {
    @Override
    public void onInitialize() {
        FarmingForBlockheads.initialize();
    }
}
