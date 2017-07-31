package net.blay09.mods.farmingforblockheads;

import net.minecraftforge.common.config.Config;

@Config(modid = FarmingForBlockheads.MOD_ID, category = "")
public class ModConfig {

	private static final String[] DEFAULT_MERCHANT_NAMES = new String[] {
			"Swap-O-Matic",
			"Emerald Muncher",
			"Weathered Salesperson"
	};

	public static General general = new General();
	public static Client client = new Client();

	public static class General {
		@Config.Name("Merchant Names")
		@Config.Comment("List of names the merchant can have.")
		public String[] merchantNames = DEFAULT_MERCHANT_NAMES;

		@Config.Name("Feeding Trough Range")
		@Config.Comment("The range within animals can be fed by the feeding trough")
		public int feedingTroughRange = 8;

		@Config.Name("Feeding Trough Max Animals")
		@Config.Comment("The maximum amount of animals (per type) until the feeding trough stops feeding")
		public int feedingTroughMaxAnimals = 8;

		@Config.Name("Chicken Nest Range")
		@Config.Comment("The range at which the chicken nest picks up laid eggs")
		public int chickenNestRange = 8;

		@Config.Name("Fertilizer Bonus Crop Chance")
		@Config.Comment("The chance to get a bonus crop when using Green Fertilizer.")
		public float fertilizerBonusCropChance = 1f;

		@Config.Name("Fertilizer Bonus Growth Chance")
		@Config.Comment("The chance to get a bonus growth when using Red Fertilizer.")
		public float fertilizerBonusGrowthChance = 1f;

		@Config.Name("Fertilizer Regression Chance")
		@Config.Comment("The chance for Fertilized Farmland to turn back into regular Farmland (per provided bonus)")
		public float fertilizerRegressionChance = 0f;
	}

	public static class Client {
		@Config.Name("Show Registry Warnings")
		@Config.Comment("Set this to true if you're a modpack dev to see Farming for Blockheads registry warnings in chat. Errors will always display.")
		public boolean showRegistryWarnings = false;
	}

	public static void validate() {
		if(general.merchantNames.length == 0) {
			general.merchantNames = DEFAULT_MERCHANT_NAMES;
		}
	}

}
