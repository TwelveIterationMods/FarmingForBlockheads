package net.blay09.mods.farmingforblockheads.container;

import com.google.common.collect.Lists;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.network.MessageMarketSelect;
import net.blay09.mods.farmingforblockheads.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ContainerMarketClient extends ContainerMarket {

	private final List<IMarketEntry> itemList = Lists.newArrayList();
	private final List<IMarketEntry> filteredItems = Lists.newArrayList();

	private final Comparator<IMarketEntry> comparator = Comparator.comparingInt(o -> o.getCategory().getSortIndex());

	private String currentSearch;
	private IMarketCategory currentCategory;
	private boolean isDirty;
	private int scrollOffset;

	public ContainerMarketClient(EntityPlayer player, BlockPos pos) {
		super(player, pos);
	}

	@Override
	public ItemStack slotClick(int slotNumber, int dragType, ClickType clickType, EntityPlayer player) {
		if (slotNumber >= 0 && slotNumber < inventorySlots.size()) {
			Slot slot = inventorySlots.get(slotNumber);
			if (player.world.isRemote) {
				if (slot instanceof FakeSlotMarket) {
					FakeSlotMarket slotMarket = (FakeSlotMarket) slot;
					IMarketEntry entry = slotMarket.getEntry();
					if (entry != null) {
						selectedEntry = entry;
						NetworkHandler.instance.sendToServer(new MessageMarketSelect(entry.getOutputItem()));
					}
				}
			}
		}
		return super.slotClick(slotNumber, dragType, clickType, player);
	}

	public void search(@Nullable String term) {
		this.currentSearch = term;
		applyFilters();
	}

	public void setFilterCategory(@Nullable IMarketCategory category) {
		this.currentCategory = category;
		applyFilters();
	}

	public void applyFilters() {
		this.scrollOffset = 0;
		filteredItems.clear();
		boolean hasSearchFilter = currentSearch != null && !currentSearch.trim().isEmpty();
		if (currentCategory == null && !hasSearchFilter) {
			filteredItems.addAll(itemList);
		} else {
			for (IMarketEntry entry : itemList) {
				if(hasSearchFilter && !entry.getOutputItem().getDisplayName().toLowerCase().contains(currentSearch.toLowerCase())) {
					continue;
				}
				if(currentCategory != null && !currentCategory.passes(entry)) {
					continue;
				}
				filteredItems.add(entry);
			}
		}

		filteredItems.sort(comparator);
	}

	public int getFilteredListCount() {
		return filteredItems.size();
	}

	public void setScrollOffset(int scrollOffset) {
		this.scrollOffset = scrollOffset;
		populateMarketSlots();
	}

	public void populateMarketSlots() {
		int i = scrollOffset * 3;
		for (FakeSlotMarket slot : marketSlots) {
			if (i < filteredItems.size()) {
				slot.setEntry(filteredItems.get(i));
				i++;
			} else {
				slot.setEntry(null);
			}
		}
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean dirty) {
		isDirty = dirty;
	}

	public void setEntryList(Collection<IMarketEntry> entryList) {
		this.itemList.clear();
		this.itemList.addAll(entryList);

		// Re-apply the filters to populate filteredItems
		applyFilters();

		// Updates the items inside the entry slots
		populateMarketSlots();

		setDirty(true);
	}

	@Nullable
	public IMarketCategory getCurrentCategory() {
		return currentCategory;
	}
}
