package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.IMarketRegistryDefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class TerraqueousAddon {

    private static final String KEY_SAPLINGS = "Terraqueous Saplings";

    public TerraqueousAddon() {
        FarmingForBlockheadsAPI.registerMarketDefaultHandler(KEY_SAPLINGS, new IMarketRegistryDefaultHandler() {
            @Override
            public void register(@Nullable ItemStack overridePayment, int unused) {
                final String[] SAPLINGS = new String[]{"sapling"};

                ItemStack effectivePayment = overridePayment;
                if(effectivePayment == null) {
                    effectivePayment = getDefaultPayment();
                }

                for (String SAPLING : SAPLINGS) {
                    ResourceLocation location = new ResourceLocation(Compat.NATURA, SAPLING);
                    if (ForgeRegistries.BLOCKS.containsKey(location)) {
                        Block blockSapling = ForgeRegistries.BLOCKS.getValue(location);
                        for (int j = 0; j <= 9; j++) {
                            ItemStack saplingStack = new ItemStack(blockSapling, unused);
                            FarmingForBlockheadsAPI.registerMarketEntry(saplingStack, effectivePayment, FarmingForBlockheadsAPI.getMarketCategorySaplings());
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
