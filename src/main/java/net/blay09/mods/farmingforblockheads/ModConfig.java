package net.blay09.mods.farmingforblockheads;

import net.minecraftforge.common.config.Configuration;

public class ModConfig {

	private static final String[] DEFAULT_MERCHANT_NAMES = new String[] {
			"Swap-O-Matic",
			"Emerald Muncher",
			"Weathered Salesperson"
	};

	public static boolean showRegistryWarnings;
	public static String[] merchantNames;

	public static void preInit(Configuration config) {
		merchantNames = config.getStringList("Merchant Names", "general", DEFAULT_MERCHANT_NAMES, "List of names the merchant can have.");
		if(merchantNames.length == 0) {
			merchantNames = DEFAULT_MERCHANT_NAMES;
		}
		showRegistryWarnings = config.getBoolean("Show Registry Warnings", "client", false, "Set this to true if you're a modpack dev to see Farming for Blockheads registry warnings in chat. Errors will always display.");
	}

}
