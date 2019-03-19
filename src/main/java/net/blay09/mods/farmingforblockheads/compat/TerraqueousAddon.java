package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryDefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class TerraqueousAddon {

    private static final String KEY_SAPLINGS = "Terraqueous Saplings";

    public TerraqueousAddon() {
        FarmingForBlockheadsAPI.registerMarketDefaultHandler(KEY_SAPLINGS, new MarketRegistryDefaultHandler() {
            @Override
            public void apply(ItemStack defaultPayment, int defaultAmount) {
                final String[] SAPLINGS = new String[]{"sapling"};

                for (String SAPLING : SAPLINGS) {
                    ResourceLocation location = new ResourceLocation(Compat.NATURA, SAPLING);
                    if (ForgeRegistries.BLOCKS.containsKey(location)) {
                        Block blockSapling = ForgeRegistries.BLOCKS.getValue(location);
                        for (int j = 0; j <= 9; j++) {
                            ItemStack saplingStack = new ItemStack(blockSapling, defaultAmount);
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
