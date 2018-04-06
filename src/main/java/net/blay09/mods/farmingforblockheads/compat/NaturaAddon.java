package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryDefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NaturaAddon {

	private static final String KEY_SAPLINGS = "Natura Saplings";

	public NaturaAddon() {
		FarmingForBlockheadsAPI.registerMarketDefaultHandler(KEY_SAPLINGS, new MarketRegistryDefaultHandler() {
			@Override
			public void apply(ItemStack defaultPayment) {
				apply(defaultPayment, 1);
			}

			@Override
			public void apply(ItemStack defaultPayment, int defaultAmount) {
				final String[] SAPLINGS = new String[] { "overworld_sapling", "overworld_sapling2", "redwood_sapling" };

				for(int i = 0; i < SAPLINGS.length; i++) {
					ResourceLocation location = new ResourceLocation(Compat.NATURA, SAPLINGS[i]);
					if(Block.REGISTRY.containsKey(location)) {
						Block blockSapling = Block.REGISTRY.getObject(location);
						for (int j = 0; j < (i == 2 ? 1 : 4); j++) {
							ItemStack saplingStack = new ItemStack(blockSapling, defaultAmount, j);
							FarmingForBlockheadsAPI.registerMarketEntry(saplingStack, defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySaplings());
						}
					}
				}
			}

			@Override
			public boolean isEnabledByDefault() {
				return true;
			}

			@Override
			public ItemStack getDefaultPayment() {
				return new ItemStack(Items.EMERALD);
			}
		});
	}

}
