package net.blay09.mods.farmingforblockheads;

import net.minecraftforge.common.config.Configuration;

public class ModConfig {

	public static boolean showRegistryWarnings;

	public static void preInit(Configuration config) {
		showRegistryWarnings = config.getBoolean("Show Registry Warnings", "client", false, "Set this to true if you're a modpack dev to see Farming for Blockheads registry warnings in chat. Errors will always display.");
	}

}
