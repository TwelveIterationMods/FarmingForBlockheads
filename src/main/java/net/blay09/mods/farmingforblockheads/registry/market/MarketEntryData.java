package net.blay09.mods.farmingforblockheads.registry.market;

import net.minecraft.item.ItemStack;

public class MarketEntryData {
    private ItemStack output;
    private ItemStack payment;
    private int amount;

    public ItemStack getOutput() {
        return output;
    }

    public void setOutput(ItemStack output) {
        this.output = output;
    }

    public ItemStack getPayment() {
        return payment;
    }

    public void setPayment(ItemStack payment) {
        this.payment = payment;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
