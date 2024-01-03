package net.blay09.mods.farmingforblockheads.menu;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.farmingforblockheads.api.MarketCategory;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.network.MarketSelectMessage;
import net.blay09.mods.farmingforblockheads.recipe.MarketRecipe;
import net.blay09.mods.farmingforblockheads.registry.MarketCategoryRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MarketMenu extends AbstractContainerMenu {

    private final Player player;
    private final BlockPos pos;
    private final DefaultContainer marketInputBuffer = new DefaultContainer(1);
    private final DefaultContainer marketOutputBuffer = new DefaultContainer(1);
    protected final List<MarketListingSlot> marketSlots = new ArrayList<>();
    private final int playerInventoryStart;
    private final MarketPaymentSlot paymentSlot;

    protected RecipeHolder<MarketRecipe> selectedRecipe;


    private final List<MarketCategory> categoryList = Lists.newArrayList();
    private final List<RecipeHolder<MarketRecipe>> itemList = Lists.newArrayList();
    private final List<RecipeHolder<MarketRecipe>> filteredItems = Lists.newArrayList();

    private final Comparator<MarketRecipe> comparator = Comparator.comparingInt(o -> MarketCategoryRegistry.INSTANCE.get(o.getCategory())
            .map(MarketCategory::sortIndex)
            .orElse(0));

    private String currentSearch;
    private MarketCategory currentCategory;
    private boolean isDirty;
    private int scrollOffset;

    public MarketMenu(int windowId, Inventory playerInventory, BlockPos pos) {
        super(ModMenus.market.get(), windowId);
        this.player = playerInventory.player;
        this.pos = pos;

        addSlot(paymentSlot = new MarketPaymentSlot(marketInputBuffer, 0, 23, 39));
        addSlot(new MarketBasketSlot(this, marketOutputBuffer, 0, 61, 39));

        Container fakeInventory = new DefaultContainer(4 * 3);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                MarketListingSlot slot = new MarketListingSlot(fakeInventory, j + i * 3, 102 + j * 18, 11 + i * 18);
                marketSlots.add(slot);
                addSlot(slot);
            }
        }

        playerInventoryStart = slots.size();
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
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (index == 1) {
                if (!isReadyToBuy()) {
                    return ItemStack.EMPTY;
                }
                if (!moveItemStackTo(slotStack, 14, 50, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, itemStack);
            } else if (index == 0) {
                if (!moveItemStackTo(slotStack, 14, 50, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (isValidPaymentItemForSelection(slotStack)) {
                if (!moveItemStackTo(slotStack, 0, 1, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 41 && index < 50) {
                if (!moveItemStackTo(slotStack, 14, 41, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 14 && index < 41) {
                if (!moveItemStackTo(slotStack, 41, 50, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }
        return itemStack;
    }

    private boolean isValidPaymentItemForSelection(ItemStack itemStack) {
        return (selectedRecipe == null && itemStack.getItem() == Items.EMERALD)
                || (selectedRecipe != null && selectedRecipe.value().getPaymentOrDefault().ingredient().test(itemStack));
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            ItemStack itemStack = this.marketInputBuffer.removeItemNoUpdate(0);
            if (!itemStack.isEmpty() && !player.addItem(itemStack)) {
                player.drop(itemStack, false);
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.level().getBlockState(pos).getBlock() == ModBlocks.market && player.distanceToSqr(pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5) <= 64;
    }

    @Override
    public void slotsChanged(Container container) {
        if (selectedRecipe != null) {
            marketOutputBuffer.setItem(0, selectedRecipe.value().assemble(new TransientCraftingContainer(this, 0, 0), RegistryAccess.EMPTY));
        } else {
            marketOutputBuffer.setItem(0, ItemStack.EMPTY);
        }
    }

    public void selectMarketEntry(ResourceLocation recipeId, boolean stack) {
        final var recipeManager = player.getServer().getRecipeManager(); // TODO
        selectedRecipe = (RecipeHolder<MarketRecipe>) recipeManager.byKey(recipeId).orElse(null);
        if (selectedRecipe != null) {
            final var payment = selectedRecipe.value().getPaymentOrDefault();
            ItemStack currentInput = marketInputBuffer.getItem(0);
            // TODO if (!payment.ingredient().test(currentInput)) {
            // TODO     quickMoveStack(player, 0);
            // TODO     quickMoveCost(payment, stack ? 64 : 1);
            // TODO } else if (stack && currentInput.getCount() < 64) {
            // TODO     quickMoveCost(payment, 64);
            // TODO } else if (!stack && currentInput.getCount() > 1) {
            // TODO     quickMoveStack(player, 0);
            // TODO     quickMoveCost(payment, 1);
            // TODO }
        }
        slotsChanged(marketInputBuffer);
    }

    private void quickMoveCost(ItemStack costItem, int desiredCount) {
        paymentSlot.setMaxStackSizeOverride(desiredCount);
        for (int i = playerInventoryStart; i < slots.size(); i++) {
            ItemStack slotStack = slots.get(i).getItem();
            if (ItemStack.isSameItem(slotStack, costItem)) {
                quickMoveStack(player, i);
            }

            if (paymentSlot.getItem().getCount() >= desiredCount) {
                break;
            }
        }
        paymentSlot.resetMaxStackSizeOverride();
    }

    @Nullable
    public RecipeHolder<MarketRecipe> getSelectedRecipe() {
        return selectedRecipe;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        return slot.container != this.marketOutputBuffer && super.canTakeItemForPickAll(itemStack, slot);
    }

    public boolean isReadyToBuy() {
        return isValidPaymentProvided();
    }

    public boolean isValidPaymentProvided() {
        ItemStack payment = marketInputBuffer.getItem(0);
        return selectedRecipe != null && !payment.isEmpty() && isValidPaymentItemForSelection(payment) && payment.getCount() >= selectedRecipe.value().getPaymentOrDefault().count();
    }

    public void onItemBought() {
        if (selectedRecipe != null) {
            marketInputBuffer.removeItem(0, selectedRecipe.value().getPaymentOrDefault().count());
            slotsChanged(marketInputBuffer);
        }
    }

    @Override
    public void clicked(int slotNumber, int dragType, ClickType clickType, Player player) {
        if (slotNumber >= 0 && slotNumber < slots.size()) {
            Slot slot = slots.get(slotNumber);
            if (player.level().isClientSide) {
                if (slot instanceof MarketListingSlot marketSlot) {
                    final var entry = marketSlot.getRecipe();
                    if (entry != null) {
                        selectedRecipe = entry;
                        Balm.getNetworking().sendToServer(new MarketSelectMessage(entry.id(), clickType == ClickType.QUICK_MOVE));
                    }
                }
            }
        }
    }

    public void search(@Nullable String term) {
        this.currentSearch = term;
        applyFilters();
    }

    public void setFilterCategory(@Nullable MarketCategory category) {
        this.currentCategory = category;
        applyFilters();
    }

    private void applyFilters() {
        this.scrollOffset = 0;
        filteredItems.clear();
        boolean hasSearchFilter = currentSearch != null && !currentSearch.trim().isEmpty();
        if (currentCategory == null && !hasSearchFilter) {
            filteredItems.addAll(itemList);
        } else {
            for (final var recipe : itemList) {
                String itemName = recipe.value().getResultItem(RegistryAccess.EMPTY).getDisplayName().getString();
                if (hasSearchFilter && !itemName.toLowerCase(Locale.ENGLISH).contains(currentSearch.toLowerCase())) {
                    continue;
                }

                if (currentCategory != null && !recipe.value().getCategory().equals(currentCategory.id())) {
                    continue;
                }

                filteredItems.add(recipe);
            }
        }

        // TODO filteredItems.sort(comparator);
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
        for (MarketListingSlot slot : marketSlots) {
            if (i < filteredItems.size()) {
                slot.setRecipe(filteredItems.get(i));
                i++;
            } else {
                slot.setRecipe(null);
            }
        }
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public void setCategoryList(Collection<MarketCategory> categoryList) {
        this.categoryList.clear();
        this.categoryList.addAll(categoryList);

        setDirty(true);
    }

    public void setEntryList(Collection<MarketRecipe> entryList) {
        this.itemList.clear();
        // TODO this.itemList.addAll(entryList);

        // Re-apply the filters to populate filteredItems
        applyFilters();

        // Updates the items inside the entry slots
        populateMarketSlots();

        setDirty(true);
    }

    @Nullable
    public MarketCategory getCurrentCategory() {
        return currentCategory;
    }

    public Collection<MarketCategory> getCategories() {
        return categoryList;
    }

}
