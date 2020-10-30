package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IMarketRegistryDefaultHandler {
    default void register(@Nullable ItemStack overridePayment) {
    }

    @Deprecated
    default void register(@Nullable ItemStack overridePayment, int unused) {
        register(overridePayment);
    }

    boolean isEnabledByDefault();

    /**
     * @deprecated Not actually called anymore, instead register will only receive a payment ItemStack if payment has been overridden for this group.
     */
    @Deprecated
    ItemStack getDefaultPayment();
}
