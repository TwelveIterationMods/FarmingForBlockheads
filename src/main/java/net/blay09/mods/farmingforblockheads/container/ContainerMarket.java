package net.blay09.mods.farmingforblockheads.container;

import com.google.common.collect.Lists;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.network.MessageMarketList;
import net.blay09.mods.farmingforblockheads.network.NetworkHandler;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class ContainerMarket extends Container {

	private final EntityPlayer player;
	private final BlockPos pos;
	private final InventoryBasic marketInputBuffer = new InventoryBasic("container.farmingforblockheads:market", false, 1);
	private final InventoryBasic marketOutputBuffer = new InventoryBasic("container.farmingforblockheads:market", false, 1);
	protected final List<FakeSlotMarket> marketSlots = Lists.newArrayList();

	private boolean sentItemList;
	protected IMarketEntry selectedEntry;

	public ContainerMarket(EntityPlayer player, BlockPos pos) {
		this.player = player;
		this.pos = pos;

		addSlotToContainer(new Slot(marketInputBuffer, 0, 23, 39));
		addSlotToContainer(new SlotMarketBuy(this, marketOutputBuffer, 0, 61, 39));

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				FakeSlotMarket slot = new FakeSlotMarket(j + i * 3, 102 + j * 18, 11 + i * 18);
				marketSlots.add(slot);
				addSlotToContainer(slot);
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 150));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			itemStack = slotStack.copy();
			if (index == 1) {
				if (!mergeItemStack(slotStack, 14, 50, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(slotStack, itemStack);
			} else if (index == 0) {
				if (!mergeItemStack(slotStack, 14, 50, true)) {
					return ItemStack.EMPTY;
				}
			} else if ((selectedEntry == null && slotStack.getItem() == Items.EMERALD) || (selectedEntry != null && selectedEntry.getCostItem().isItemEqual(slotStack))) {
				if (!mergeItemStack(slotStack, 0, 1, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 41 && index < 50) {
				if (!mergeItemStack(slotStack, 14, 41, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 14 && index < 41) {
				if (!mergeItemStack(slotStack, 41, 50, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (slotStack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (slotStack.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, slotStack);
		}
		return itemStack;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if (!player.world.isRemote && !sentItemList) {
			NetworkHandler.instance.sendTo(new MessageMarketList(MarketRegistry.getEntries()), (EntityPlayerMP) player);
			sentItemList = true;
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (!player.world.isRemote) {
			ItemStack itemStack = this.marketInputBuffer.removeStackFromSlot(0);
			if (!itemStack.isEmpty()) {
				player.dropItem(itemStack, false);
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return player.world.getBlockState(pos).getBlock() == ModBlocks.market && player.getDistanceSq( pos.getX() + 0.5,  pos.getY() + 0.5,  pos.getZ() + 0.5) <= 64;
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		if (selectedEntry != null) {
			marketOutputBuffer.setInventorySlotContents(0, selectedEntry.getOutputItem().copy());
		} else {
			marketOutputBuffer.setInventorySlotContents(0, ItemStack.EMPTY);
		}
	}

	public void selectMarketEntry(ItemStack outputItem) {
		selectedEntry = MarketRegistry.getEntryFor(outputItem);
		onCraftMatrixChanged(marketInputBuffer);
	}

	@Nullable
	public IMarketEntry getSelectedEntry() {
		return selectedEntry;
	}

	@Override
	public boolean canMergeSlot(ItemStack itemStack, Slot slot) {
		return slot.inventory != this.marketOutputBuffer && super.canMergeSlot(itemStack, slot);
	}

	public boolean isReadyToBuy() {
		ItemStack payment = marketInputBuffer.getStackInSlot(0);
		return !payment.isEmpty() && !(selectedEntry == null || !payment.isItemEqual(selectedEntry.getCostItem()) || payment.getCount() < selectedEntry.getCostItem().getCount());
	}

	public void onItemBought() {
		if(selectedEntry != null) {
			marketInputBuffer.decrStackSize(0, selectedEntry.getCostItem().getCount());
			onCraftMatrixChanged(marketInputBuffer);
		}
	}
}
