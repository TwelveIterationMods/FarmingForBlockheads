package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;

public interface MarketRegistryDefaultHandler {
    /**
     * @deprecated use the version accepting a default amount as second parameter
     */
    @Deprecated
    void apply(ItemStack defaultPayment);

    default void apply(ItemStack defaultPayment, int defaultAmount) {
        apply(defaultPayment);
    }

    boolean isEnabledByDefault();

    ItemStack getDefaultPayment();

    default int getDefaultAmount() {
        return 1;
    }
}
