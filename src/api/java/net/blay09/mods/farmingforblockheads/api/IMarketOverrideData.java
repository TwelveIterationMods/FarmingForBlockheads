package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.item.ItemStack;

public interface IMarketOverrideData {

    boolean isEnabled();

    ItemStack getPayment();

    int getAmount();
}
