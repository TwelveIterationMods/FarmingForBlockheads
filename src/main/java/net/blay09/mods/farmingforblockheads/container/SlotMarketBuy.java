package net.blay09.mods.farmingforblockheads.container;

import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMarketBuy extends Slot {

	private final ContainerMarket container;

	public SlotMarketBuy(ContainerMarket container, IInventory inventory, int index, int xPosition, int yPosition) {
		super(inventory, index, xPosition, yPosition);
		this.container = container;
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return container.isReadyToBuy();
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
		MarketEntry entry = container.getSelectedEntry();
		if (entry != null) {
			container.onItemBought();
		}
		return stack;
	}

}
