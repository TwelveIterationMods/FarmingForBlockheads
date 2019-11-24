package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;

public interface IMarketRegistryDefaultHandler {
    void register(ItemStack defaultPayment, int defaultAmount);

    boolean isEnabledByDefault();

    ItemStack getDefaultPayment();

    default int getDefaultAmount() {
        return 1;
    }
}
