package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistryDefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BiomesOPlentyAddon {

	private static final String KEY_SAPLINGS = "BiomesOPlenty Saplings";
	private static final String KEY_SACRED_OAK = "BiomesOPlenty Sacred Oak Sapling";

	private static final int SACRED_OAK_PAGE = 1;
	private static final int SACRED_OAK_META = 7;

	public BiomesOPlentyAddon() {
		MarketRegistry.registerDefaultHandler(KEY_SAPLINGS, new MarketRegistryDefaultHandler() {
			@Override
			public void apply(MarketRegistry registry, ItemStack defaultPayment) {
				for(int i = 0; i <= 2; i++) {

					ResourceLocation location = new ResourceLocation(Compat.BIOMESOPLENTY, "sapling_" + i);
					if(Block.REGISTRY.containsKey(location)) {
						Block blockSapling = Block.REGISTRY.getObject(location);
						for (int j = 0; j < 8; j++) {
							if(i == SACRED_OAK_PAGE && j == SACRED_OAK_META) {
								// Sacred Oak Sapling. Done below.
								continue;
							}
							ItemStack saplingStack = new ItemStack(blockSapling, 1, j);
							registry.registerEntry(saplingStack, defaultPayment, MarketEntry.EntryType.SAPLINGS);
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

		MarketRegistry.registerDefaultHandler(KEY_SACRED_OAK, new MarketRegistryDefaultHandler() {
			@Override
			public void apply(MarketRegistry registry, ItemStack defaultPayment) {
					ResourceLocation location = new ResourceLocation(Compat.BIOMESOPLENTY, "sapling_" + SACRED_OAK_PAGE);
					if(Block.REGISTRY.containsKey(location)) {
						Block blockSapling = Block.REGISTRY.getObject(location);
						ItemStack saplingStack = new ItemStack(blockSapling, 1, SACRED_OAK_META);
						registry.registerEntry(saplingStack, defaultPayment, MarketEntry.EntryType.SAPLINGS);
					}
			}

			@Override
			public boolean isEnabledByDefault() {
				return false;
			}

			@Override
			public ItemStack getDefaultPayment() {
				return new ItemStack(Items.EMERALD, 1, 8);
			}
		});
	}

}
