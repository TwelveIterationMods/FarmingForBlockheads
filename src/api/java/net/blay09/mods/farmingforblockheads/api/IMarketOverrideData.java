package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IMarketOverrideData {

    boolean isEnabled();

    @Nullable
    ItemStack getPayment();

    int getCount();
}
