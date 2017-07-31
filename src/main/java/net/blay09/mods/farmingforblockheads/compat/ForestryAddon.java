package net.blay09.mods.farmingforblockheads.compat;

import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.TreeManager;
import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryDefaultHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ForestryAddon {

	private static final String KEY_SAPLINGS = "Forestry Saplings";

	public ForestryAddon() {
		FarmingForBlockheadsAPI.registerMarketDefaultHandler(KEY_SAPLINGS, new MarketRegistryDefaultHandler() {
			@Override
			public void apply(ItemStack defaultPayment) {
				for(ITree tree : TreeManager.treeRoot.getIndividualTemplates()) {
					ItemStack saplingStack = TreeManager.treeRoot.getMemberStack(tree, EnumGermlingType.SAPLING);
					FarmingForBlockheadsAPI.registerMarketEntry(saplingStack, defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySaplings());
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
