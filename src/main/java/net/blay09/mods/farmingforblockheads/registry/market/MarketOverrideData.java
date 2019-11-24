package net.blay09.mods.farmingforblockheads.registry.market;

import net.blay09.mods.farmingforblockheads.api.IMarketOverrideData;
import net.minecraft.item.ItemStack;

public class MarketOverrideData implements IMarketOverrideData {
    private boolean enabled;
    private ItemStack payment;
    private int amount;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public ItemStack getPayment() {
        return payment;
    }

    public void setPayment(ItemStack payment) {
        this.payment = payment;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
