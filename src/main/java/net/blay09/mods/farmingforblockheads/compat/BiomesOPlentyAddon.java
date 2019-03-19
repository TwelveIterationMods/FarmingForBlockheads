package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryDefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomesOPlentyAddon {

    private static final String KEY_SAPLINGS = "BiomesOPlenty Saplings";
    private static final String KEY_SACRED_OAK = "BiomesOPlenty Sacred Oak Sapling";

    public BiomesOPlentyAddon() {
        FarmingForBlockheadsAPI.registerMarketDefaultHandler(KEY_SAPLINGS, new MarketRegistryDefaultHandler() {
            @Override
            public void apply(ItemStack defaultPayment, int defaultAmount) {
                for (int i = 0; i <= 2; i++) {
                    ResourceLocation location = new ResourceLocation(Compat.BIOMESOPLENTY, "sapling_" + i);
                    if (ForgeRegistries.BLOCKS.containsKey(location)) {
                        Block blockSapling = ForgeRegistries.BLOCKS.getValue(location);
                        ItemStack saplingStack = new ItemStack(blockSapling, defaultAmount);
                        FarmingForBlockheadsAPI.registerMarketEntry(saplingStack, defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySaplings());
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

        FarmingForBlockheadsAPI.registerMarketDefaultHandler(KEY_SACRED_OAK, new MarketRegistryDefaultHandler() {
            @Override
            public void apply(ItemStack defaultPayment, int defaultAmount) {
                ResourceLocation location = new ResourceLocation(Compat.BIOMESOPLENTY, "sacred_oak_sapling");
                if (ForgeRegistries.BLOCKS.containsKey(location)) {
                    Block blockSapling = ForgeRegistries.BLOCKS.getValue(location);
                    ItemStack saplingStack = new ItemStack(blockSapling, defaultAmount);
                    FarmingForBlockheadsAPI.registerMarketEntry(saplingStack, defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySaplings());
                }
            }

            @Override
            public boolean isEnabledByDefault() {
                return false;
            }

            @Override
            public ItemStack getDefaultPayment() {
                return new ItemStack(Items.EMERALD, 8);
            }
        });
    }

}
