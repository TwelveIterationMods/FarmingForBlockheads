package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class HarvestcraftMarket {

	private static final String[] CROPS = new String[] {
			"blackberry", "blueberry", "candleberry", "raspberry", "strawberry", "cactusfruit", "asparagus", "barley", "oats", "rye", "corn", "bambooshoot", "cantaloupe", "cucumber", "wintersquash", "zucchini", "beet", "onion", "parsnip", "peanut", "radish", "rutabaga", "sweetpotato", "turnip", "rhubarb", "celery", "garlic", "ginger", "spiceleaf", "tealeaf", "coffeebean", "mustardseeds", "broccoli", "cauliflower", "leek", "lettuce", "scallion", "artichoke", "brusselsprout", "cabbage", "spinach", "whitemushroom", "bean", "soybean", "bellpepper", "chilipepper", "eggplant", "okra", "peas", "tomato", "cotton", "pineapple", "grape", "kiwi", "cranberry", "rice", "seaweed", "curryleaf", "sesameseeds", "waterchestnut",
	};

	private static final String[] SAPLINGS = new String[] {
			"apple", "almond", "apricot", "avocado", "banana", "cashew", "cherry", "chestnut", "coconut", "date", "dragonfruit", "durian", "fig", "gooseberry", "grapefruit", "lemon", "lime", "mango", "nutmeg", "olive", "orange", "papaya", "peach", "pear", "pecan", "peppercorn", "persimmon", "pistachio", "plum", "pomegranate", "starfruit", "vanillabean", "walnut", "cinnamon", "maple", "paperbark"
	};

	public static void registerSeeds(MarketRegistry registry) {
		for (String cropName : CROPS) {
			String seedName = cropName + "seedItem";
			Item seedItem = Item.REGISTRY.getObject(new ResourceLocation(Compat.HARVESTCRAFT, seedName));
			if (seedItem != null) {
				registry.registerEntry(new ItemStack(seedItem), new ItemStack(Items.EMERALD), MarketEntry.EntryType.SEEDS);
			}
		}
	}

	public static void registerSaplings(MarketRegistry registry) {
		for (String treeName : SAPLINGS) {
			String saplingName = treeName + "_sapling";
			Item saplingItem = Item.REGISTRY.getObject(new ResourceLocation(Compat.HARVESTCRAFT, saplingName));
			if (saplingItem != null) {
				registry.registerEntry(new ItemStack(saplingItem), new ItemStack(Items.EMERALD), MarketEntry.EntryType.SAPLINGS);
			}
		}
	}
}
