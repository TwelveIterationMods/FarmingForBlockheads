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
