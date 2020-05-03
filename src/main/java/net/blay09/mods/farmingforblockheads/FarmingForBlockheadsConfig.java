package net.blay09.mods.farmingforblockheads;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FarmingForBlockheadsConfig {

    private static final List<String> DEFAULT_MERCHANT_NAMES = Arrays.asList(
            "Swap-O-Matic",
            "Emerald Muncher",
            "Weathered Salesperson"
    );

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> merchantNames;
        public final ForgeConfigSpec.IntValue feedingTroughRange;
        public final ForgeConfigSpec.IntValue feedingTroughMaxAnimals;
        public final ForgeConfigSpec.IntValue chickenNestRange;
        public final ForgeConfigSpec.DoubleValue fertilizerBonusCropChance;
        public final ForgeConfigSpec.DoubleValue fertilizerBonusGrowthChance;
        public final ForgeConfigSpec.DoubleValue fertilizerRegressionChance;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Common config for Farming for Blockheads").push("common");

            merchantNames = builder.comment("List of names the merchant can have.")
                    .defineList("merchantNames", DEFAULT_MERCHANT_NAMES, it -> it instanceof String);

            feedingTroughRange = builder.comment("The range within animals can be fed by the feeding trough.")
                    .defineInRange("feedingTroughRange", 8, 1, 16);

            feedingTroughMaxAnimals = builder.comment("The maximum amount of animals (per type) until the feeding trough stops feeding.")
                    .defineInRange("feedingTroughMaxAnimals", 8, 1, Integer.MAX_VALUE);

            chickenNestRange = builder.comment("The range at which the chicken nest picks up laid eggs.")
                    .defineInRange("chickenNestRange", 8, 1, 16);

            fertilizerBonusCropChance = builder.comment("The chance to get a bonus crop when using Green Fertilizer.")
                    .defineInRange("fertilizerBonusCropChance", 1f, 0f, 1f);

            fertilizerBonusGrowthChance = builder.comment("The chance to get a bonus growth when using Red Fertilizer.")
                    .defineInRange("fertilizerBonusGrowthChance", 1f, 0f, 1f);

            fertilizerRegressionChance = builder.comment("The chance for Fertilized Farmland to turn back into regular Farmland (per provided bonus).")
                    .defineInRange("fertilizerRegressionChance", 0f, 0f, 1f);
        }
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue showRegistryWarnings;

        Client(ForgeConfigSpec.Builder builder) {
            showRegistryWarnings = builder.comment("Set this to true if you're a modpack dev to see Farming for Blockheads registry warnings in chat. Errors will always display.")
                    .define("showRegistryWarnings", false);
        }
    }

    static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }


    public static String getRandomMerchantName(Random rand) {
        List<? extends String> candidates = COMMON.merchantNames.get();
        if (candidates.isEmpty()) {
            candidates = DEFAULT_MERCHANT_NAMES;
        }

        return candidates.get(rand.nextInt(candidates.size()));
    }

}
