package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.balm.api.Balm;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FarmingForBlockheadsConfig {

    public static final Set<String> DEFAULT_INCLUDED_GROUPS = Set.of("selling.seeds", "selling.saplings", "selling.fertilizers.minecraft");

    public static final List<String> DEFAULT_MERCHANT_NAMES = List.of(
            "Swap-O-Matic",
            "Emerald Muncher",
            "Weathered Salesperson"
    );

    public static FarmingForBlockheadsConfigData getActive() {
        return Balm.getConfig().getActive(FarmingForBlockheadsConfigData.class);
    }

    public static void initialize() {
        Balm.getConfig().registerConfig(FarmingForBlockheadsConfigData.class, null);
    }
}
