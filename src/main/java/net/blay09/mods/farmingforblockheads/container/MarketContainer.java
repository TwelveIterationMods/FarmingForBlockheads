package net.blay09.mods.farmingforblockheads.container;

import com.google.common.collect.Lists;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.network.MarketListMessage;
import net.blay09.mods.farmingforblockheads.network.NetworkHandler;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MarketContainer extends Container {

    private final PlayerEntity player;
    private final BlockPos pos;
    private final Inventory marketInputBuffer = new Inventory(1);
    private final Inventory marketOutputBuffer = new Inventory(1);
    protected final List<MarketFakeSlot> marketSlots = Lists.newArrayList();

    private boolean sentItemList;
    protected IMarketEntry selectedEntry;

    public MarketContainer(int windowId, PlayerInventory playerInventory, BlockPos pos) {
        super(ModContainers.market, windowId);
        this.player = playerInventory.player;
        this.pos = pos;

        addSlot(new Slot(marketInputBuffer, 0, 23, 39));
        addSlot(new MarketBuySlot(this, marketOutputBuffer, 0, 61, 39));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                MarketFakeSlot slot = new MarketFakeSlot(j + i * 3, 102 + j * 18, 11 + i * 18);
                marketSlots.add(slot);
                addSlot(slot);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 150));
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if (index == 1) {
                if (!isReadyToBuy()) {
                    return ItemStack.EMPTY;
                }
                if (!mergeItemStack(slotStack, 14, 50, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, itemStack);
            } else if (index == 0) {
                if (!mergeItemStack(slotStack, 14, 50, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (isPaymentItem(slotStack)) {
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

    private boolean isPaymentItem(ItemStack itemStack) {
        return (selectedEntry == null && itemStack.getItem() == Items.EMERALD)
                || (selectedEntry != null && selectedEntry.getCostItem().isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(selectedEntry.getCostItem(), itemStack));
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if (!player.world.isRemote && !sentItemList) {
            NetworkHandler.sendTo(new MarketListMessage(MarketRegistry.getGroupedEntries()), player);
            sentItemList = true;
        }
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
        if (!player.world.isRemote) {
            ItemStack itemStack = this.marketInputBuffer.removeStackFromSlot(0);
            if (!itemStack.isEmpty()) {
                player.dropItem(itemStack, false);
            }
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return player.world.getBlockState(pos).getBlock() == ModBlocks.market && player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        if (selectedEntry != null) {
            marketOutputBuffer.setInventorySlotContents(0, selectedEntry.getOutputItem().copy());
        } else {
            marketOutputBuffer.setInventorySlotContents(0, ItemStack.EMPTY);
        }
    }

    public void selectMarketEntry(UUID entryId) {
        selectedEntry = MarketRegistry.getEntryById(entryId);
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
        return selectedEntry != null && !payment.isEmpty() && isPaymentItem(payment) && payment.getCount() >= selectedEntry.getCostItem().getCount();
    }

    public void onItemBought() {
        if (selectedEntry != null) {
            marketInputBuffer.decrStackSize(0, selectedEntry.getCostItem().getCount());
            onCraftMatrixChanged(marketInputBuffer);
        }
    }

}
