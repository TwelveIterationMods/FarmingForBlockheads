package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.IMarketRegistryDefaultHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AgriCraftAddon {

    private static final String KEY_SEEDS = "AgriCraft Seeds";

    public AgriCraftAddon() {
        FarmingForBlockheadsAPI.registerMarketDefaultHandler(KEY_SEEDS, new IMarketRegistryDefaultHandler() {
            @Override
            public void register(ItemStack defaultPayment, int defaultAmount) {
                // TODO Re-implement once AgriCraft gets ported
//				Item seedItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Compat.AGRICRAFT, "agri_seed"));
//				if(seedItem != null) {
//					ItemGroup agriCraftTab = Arrays.stream(ItemGroup.GROUPS).filter(tab -> tab.tabLabel.equals("agricraft_seeds")).findFirst().orElse(null);
//					if(agriCraftTab != null) {
//						NonNullList<ItemStack> stackList = NonNullList.create();
//						seedItem.getSubItems(agriCraftTab, stackList);
//						for (ItemStack itemStack : stackList) {
//							FarmingForBlockheadsAPI.registerMarketEntry(ItemHandlerHelper.copyStackWithSize(itemStack, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySeeds());
//						}
//					} else {
//						FarmingForBlockheads.logger.warn("Could not find AgriCraft Seeds creative tab. AgriCraft seeds will not be available in the market.");
//					}
//				} else {
//					FarmingForBlockheads.logger.warn("Could not find AgriCraft seed item. AgriCraft seeds will not be available in the market.");
//				}
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
