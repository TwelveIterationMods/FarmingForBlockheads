package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;

public interface IMarketRegistryDefaultHandler {
    default void register(ItemStack defaultPayment) {
    }

    @Deprecated
    default void register(ItemStack defaultPayment, int unused) {
        register(defaultPayment);
    }

    boolean isEnabledByDefault();

    ItemStack getDefaultPayment();
}
