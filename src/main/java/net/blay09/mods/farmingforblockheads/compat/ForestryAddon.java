package net.blay09.mods.farmingforblockheads.compat;

import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.TreeManager;
import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistryDefaultHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ForestryAddon {

	private static final String KEY_SAPLINGS = "Forestry Saplings";

	public ForestryAddon() {
		MarketRegistry.registerDefaultHandler(KEY_SAPLINGS, new MarketRegistryDefaultHandler() {
			@Override
			public void apply(MarketRegistry registry) {
				ItemStack payment = registry.getPaymentStackForDefault(KEY_SAPLINGS, new ItemStack(Items.EMERALD, 2));
				for(ITree tree : TreeManager.treeRoot.getIndividualTemplates()) {
					ItemStack saplingStack = TreeManager.treeRoot.getMemberStack(tree, EnumGermlingType.SAPLING);
					registry.registerEntry(saplingStack, payment, MarketEntry.EntryType.SAPLINGS);
				}
			}

			@Override
			public boolean isEnabledByDefault() {
				return false;
			}
		});
	}

}
