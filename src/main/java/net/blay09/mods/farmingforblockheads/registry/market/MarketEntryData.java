package net.blay09.mods.farmingforblockheads.registry.market;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class MarketEntryData {
    private ItemStack output;
    private ItemStack payment;
    private ResourceLocation category;

    public ItemStack getOutput() {
        return output;
    }

    public void setOutput(ItemStack output) {
        this.output = output;
    }

    @Nullable
    public ItemStack getPayment() {
        return payment;
    }

    public void setPayment(ItemStack payment) {
        this.payment = payment;
    }

    @Nullable
    public ResourceLocation getCategory() {
        return category;
    }

    public void setCategory(ResourceLocation category) {
        this.category = category;
    }
}
