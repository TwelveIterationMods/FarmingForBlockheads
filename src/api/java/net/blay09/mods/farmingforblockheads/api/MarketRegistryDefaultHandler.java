package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;

public interface MarketRegistryDefaultHandler {
    void apply(ItemStack defaultPayment, int defaultAmount);

    boolean isEnabledByDefault();

    ItemStack getDefaultPayment();

    default int getDefaultAmount() {
        return 1;
    }
}
