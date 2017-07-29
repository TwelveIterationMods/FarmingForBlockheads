package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistryDefaultHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class AgriCraftAddon {

	private static final String KEY_SEEDS = "AgriCraft Seeds";

	public AgriCraftAddon() {
		MarketRegistry.registerDefaultHandler(KEY_SEEDS, new MarketRegistryDefaultHandler() {
			@Override
			public void apply(MarketRegistry registry, ItemStack defaultPayment) {
				Item seedItem = Item.REGISTRY.getObject(new ResourceLocation(Compat.AGRICRAFT, "agri_seed"));
				if(seedItem != null) {
					CreativeTabs agriCraftTab = Arrays.stream(CreativeTabs.CREATIVE_TAB_ARRAY).filter(tab -> tab.tabLabel.equals("agricraft_seeds")).findFirst().orElse(null);
					if(agriCraftTab != null) {
						NonNullList<ItemStack> stackList = NonNullList.create();
						seedItem.getSubItems(seedItem, agriCraftTab, stackList);
						for (ItemStack itemStack : stackList) {
							registry.registerEntry(itemStack, defaultPayment, MarketEntry.EntryType.SEEDS);
						}
					} else {
						FarmingForBlockheads.logger.warn("Could not find AgriCraft Seeds creative tab. AgriCraft seeds will not be available in the market.");
					}
				} else {
					FarmingForBlockheads.logger.warn("Could not find AgriCraft seed item. AgriCraft seeds will not be available in the market.");
				}
			}

			@Override
			public boolean isEnabledByDefault() {
				return false;
			}

			@Override
			public ItemStack getDefaultPayment() {
				return new ItemStack(Items.EMERALD, 2);
			}
		});
	}

}
