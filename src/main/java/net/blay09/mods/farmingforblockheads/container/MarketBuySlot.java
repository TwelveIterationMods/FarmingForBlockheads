package net.blay09.mods.farmingforblockheads.container;

import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class MarketBuySlot extends Slot {

    private final MarketContainer container;

    public MarketBuySlot(MarketContainer container, IInventory inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.container = container;
    }

    @Override
    public boolean canTakeStack(PlayerEntity player) {
        return container.isReadyToBuy();
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        IMarketEntry entry = container.getSelectedEntry();
        if (entry != null) {
            container.onItemBought();
        }
        return stack;
    }

}
