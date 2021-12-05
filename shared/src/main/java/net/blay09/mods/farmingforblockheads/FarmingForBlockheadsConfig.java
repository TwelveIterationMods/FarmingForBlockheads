package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.balm.api.Balm;

public class FarmingForBlockheadsConfig {

    public static FarmingForBlockheadsConfigData getActive() {
        return Balm.getConfig().getActive(FarmingForBlockheadsConfigData.class);
    }

    public static void initialize() {
        Balm.getConfig().registerConfig(FarmingForBlockheadsConfigData.class, null);
    }
}
