package net.blay09.mods.farmingforblockheads.container;

import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class FakeSlotMarket extends FakeSlot {

	private MarketEntry entry;

	public FakeSlotMarket(int slotId, int x, int y) {
		super(slotId, x, y);
	}

	@Override
	public ItemStack getStack() {
		return entry != null ? entry.getOutputItem() : ItemStack.EMPTY;
	}

	@Override
	public boolean getHasStack() {
		return entry != null;
	}

	@Override
	public boolean canBeHovered() {
		return entry != null;
	}

	public void setEntry(@Nullable MarketEntry entry) {
		this.entry = entry;
	}

	@Nullable
	public MarketEntry getEntry() {
		return entry;
	}

}
