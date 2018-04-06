package net.blay09.mods.farmingforblockheads.compat;

import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.TreeManager;
import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryDefaultHandler;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class ForestryAddon {

	private static final String KEY_SAPLINGS = "Forestry Saplings";

	public ForestryAddon() {
		FarmingForBlockheadsAPI.registerMarketDefaultHandler(KEY_SAPLINGS, new MarketRegistryDefaultHandler() {
			@Override
			public void apply(ItemStack defaultPayment) {
				apply(defaultPayment, 1);
			}

			@Override
			public void apply(ItemStack defaultPayment, int defaultAmount) {
				for(ITree tree : TreeManager.treeRoot.getIndividualTemplates()) {
					ItemStack saplingStack = TreeManager.treeRoot.getMemberStack(tree, EnumGermlingType.SAPLING);
					FarmingForBlockheadsAPI.registerMarketEntry(ItemHandlerHelper.copyStackWithSize(saplingStack, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySaplings());
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
