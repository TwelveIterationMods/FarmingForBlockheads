package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryDefaultHandler;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class XLFoodModAddon {

    private static final String KEY_SEEDS = "XL Food Mod Seeds";
    private static final String[] SEEDS = new String[]{
            "rice", "pepper", "corn", "cucumber", "lettuce", "tomato", "strawberry"
    };

    public XLFoodModAddon() {
        FarmingForBlockheadsAPI.registerMarketDefaultHandler(KEY_SEEDS, new MarketRegistryDefaultHandler() {
            @Override
            public void apply(ItemStack defaultPayment) {
                apply(defaultPayment, 1);
            }

            @Override
            public void apply(ItemStack defaultPayment, int defaultAmount) {
                for (String cropName : SEEDS) {
                    String seedName = cropName + "_seeds";
                    Item seedItem = Item.REGISTRY.getObject(new ResourceLocation(Compat.XLFOODMOD, seedName));
                    if (seedItem != null) {
                        FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(seedItem, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySeeds());
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
