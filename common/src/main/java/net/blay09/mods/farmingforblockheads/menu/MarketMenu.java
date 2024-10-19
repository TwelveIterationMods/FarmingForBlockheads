package net.blay09.mods.farmingforblockheads.menu;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.farmingforblockheads.api.MarketCategory;
import net.blay09.mods.farmingforblockheads.api.Payment;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.network.MarketPutInBasketMessage;
import net.blay09.mods.farmingforblockheads.recipe.MarketRecipe;
import net.blay09.mods.farmingforblockheads.recipe.MarketRecipeDisplay;
import net.blay09.mods.farmingforblockheads.registry.MarketDefaultsRegistry;
import net.blay09.mods.farmingforblockheads.registry.SimpleHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MarketMenu extends AbstractContainerMenu {

    private final Player player;
    private final BlockPos pos;

    private final DefaultContainer marketInputBuffer = new DefaultContainer(1);
    private final DefaultContainer marketOutputBuffer = new DefaultContainer(1);
    private final List<MarketListingSlot> marketSlots = new ArrayList<>();
    private final MarketPaymentSlot paymentSlot;

    private List<SimpleHolder<MarketCategory>> categories = List.of();
    private List<RecipeDisplayEntry> recipes = List.of();

    private String currentSearch;
    private SimpleHolder<MarketCategory> currentCategory;

    private boolean scrollOffsetDirty;
    private int scrollOffset;

    private final List<RecipeDisplayEntry> filteredRecipes = new ArrayList<>();
    private RecipeDisplayEntry selectedRecipe;

    private final DataSlot canBuy = DataSlot.standalone();

    public MarketMenu(int windowId, Inventory playerInventory, BlockPos pos) {
        super(ModMenus.market.get(), windowId);
        this.player = playerInventory.player;
        this.pos = pos;

        addSlot(paymentSlot = new MarketPaymentSlot(marketInputBuffer, 0, 23, 39));
        addSlot(new MarketBasketSlot(this, marketOutputBuffer, 0, 61, 39));

        final var fakeInventory = new DefaultContainer(4 * 3);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                MarketListingSlot slot = new MarketListingSlot(fakeInventory, j + i * 3, 102 + j * 18, 11 + i * 18, player.level());
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

        addDataSlot(canBuy);

        updateFilteredRecipes();
        setScrollOffset(0);
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
            } else if (isValidPayment(slotStack)) {
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

    private boolean isValidPayment(ItemStack itemStack) {
        if (selectedRecipe != null) {
            if (selectedRecipe.display() instanceof MarketRecipeDisplay marketRecipeDisplay) {
                final var contextMap = SlotDisplayContext.fromLevel(player.level());
                final var paymentStacks = marketRecipeDisplay.payment().resolveForStacks(contextMap);
                for (final var paymentStack : paymentStacks) {
                    if (ItemStack.isSameItemSameComponents(paymentStack, itemStack)) {
                        return true;
                    }
                }
            }
        }
        return false;
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
        return player.level().getBlockState(pos).getBlock() == ModBlocks.market
                && player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        return slot.container != this.marketOutputBuffer && super.canTakeItemForPickAll(itemStack, slot);
    }

    public void selectMarketEntry(RecipeDisplayId recipeDisplayId, boolean stack) {
        selectedRecipe = recipes.stream().filter(it -> it.id().equals(recipeDisplayId)).findFirst().orElse(null);

        final var recipe = resolveRecipe(selectedRecipe);
        if (recipe != null) {
            marketOutputBuffer.setItem(0, recipe.assemble(new TransientCraftingContainer(this, 0, 0).asCraftInput(), RegistryAccess.EMPTY));
            quickMovePayment(recipe, stack);
        }
    }

    private MarketRecipe resolveRecipe(@Nullable RecipeDisplayEntry recipeDisplayEntry) {
        if (recipeDisplayEntry == null) {
            return null;
        }

        final var recipeManager = player.getServer().getRecipeManager();
        var serverDisplayInfo = recipeManager.getRecipeFromDisplay(recipeDisplayEntry.id());
        if (serverDisplayInfo != null) {
            return (MarketRecipe) serverDisplayInfo.parent().value();
        }

        return null;
    }

    public void quickMovePayment(MarketRecipe recipe, boolean stack) {
        final var payment = MarketDefaultsRegistry.resolvePayment(recipe);
        final var currentInput = marketInputBuffer.getItem(0);
        if (!payment.ingredient().test(currentInput)) {
            quickMoveStack(player, 0);
            quickMoveCost(payment, stack ? 64 : 1);
        } else if (stack && currentInput.getCount() < 64) {
            quickMoveCost(payment, 64);
        } else if (!stack && currentInput.getCount() > 1) {
            quickMoveStack(player, 0);
            quickMoveCost(payment, 1);
        }
    }

    private void quickMoveCost(Payment payment, int desiredCount) {
        paymentSlot.setMaxStackSizeOverride(desiredCount);
        for (int i = 14; i < slots.size(); i++) {
            final var slotStack = slots.get(i).getItem();
            if (payment.ingredient().test(slotStack)) {
                quickMoveStack(player, i);
            }

            if (paymentSlot.getItem().getCount() >= desiredCount) {
                break;
            }
        }
        paymentSlot.resetMaxStackSizeOverride();
    }

    @Nullable
    public RecipeDisplayEntry getSelectedRecipe() {
        return selectedRecipe;
    }

    public boolean isReadyToBuy() {
        return canBuy.get() == 1;
    }

    public boolean verifyPayment() {
        if (selectedRecipe != null) {
            final var recipe = resolveRecipe(selectedRecipe);
            final var payment = MarketDefaultsRegistry.resolvePayment(recipe);
            final var currentInput = marketInputBuffer.getItem(0);
            if (currentInput.isEmpty()) {
                return false;
            }
            return payment.ingredient().test(currentInput) && currentInput.getCount() >= payment.count();
        } else {
            return false;
        }
    }

    @Override
    public void slotsChanged(Container container) {
        canBuy.set(verifyPayment() ? 1 : 0);
    }

    public void onItemBought() {
        final var recipe = resolveRecipe(selectedRecipe);
        if (recipe != null) {
            final var payment = MarketDefaultsRegistry.resolvePayment(recipe);
            marketInputBuffer.removeItem(0, payment.count());
            slotsChanged(marketInputBuffer);
        }
    }

    @Override
    public void clicked(int slotNumber, int dragType, ClickType clickType, Player player) {
        if (slotNumber >= 0 && slotNumber < slots.size()) {
            final var slot = slots.get(slotNumber);
            if (player.level().isClientSide) {
                if (slot instanceof MarketListingSlot marketSlot) {
                    final var recipe = marketSlot.getRecipe();
                    if (recipe != null) {
                        selectedRecipe = recipe;
                        Balm.getNetworking().sendToServer(new MarketPutInBasketMessage(recipe.id(), clickType == ClickType.QUICK_MOVE));
                    }
                }
            }
        }

        super.clicked(slotNumber, dragType, clickType, player);
    }

    public void setSearch(@Nullable String term) {
        this.currentSearch = term;
        updateFilteredRecipes();
        setScrollOffset(0);
    }

    public void setCategory(@Nullable SimpleHolder<MarketCategory> category) {
        this.currentCategory = category;
        updateFilteredRecipes();
        setScrollOffset(0);
    }

    private void updateFilteredRecipes() {
        filteredRecipes.clear();
        for (final var recipe : recipes) {
            if (searchMatches(recipe) && categoryMatches(recipe)) {
                filteredRecipes.add(recipe);
            }
        }
        filteredRecipes.sort(sorting(player.level()));
    }

    private boolean searchMatches(RecipeDisplayEntry recipeDisplayEntry) {
        if (currentSearch == null || currentSearch.trim().isEmpty()) {
            return true;
        }

        final var contextMap = SlotDisplayContext.fromLevel(player.level());
        final var resultItem = recipeDisplayEntry.display().result().resolveForFirstStack(contextMap);
        final var lowerCaseSearch = currentSearch.toLowerCase();
        if (resultItem.getDisplayName().getString().toLowerCase(Locale.ENGLISH).contains(lowerCaseSearch)) {
            return true;
        } else {
            final var tooltips = resultItem.getTooltipLines(Item.TooltipContext.EMPTY, player, TooltipFlag.Default.NORMAL);
            for (final var tooltip : tooltips) {
                if (tooltip.getString().toLowerCase(Locale.ENGLISH).contains(lowerCaseSearch)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean categoryMatches(RecipeDisplayEntry recipeDisplayEntry) {
        if (currentCategory == null) {
            return true;
        }

        final var display = recipeDisplayEntry.display();
        if (display instanceof MarketRecipeDisplay marketRecipeDisplay) {
            final var marketCategory = marketRecipeDisplay.category();
            return marketCategory.equals(currentCategory.id());
        }
        return true;
    }

    public int getFilteredListCount() {
        return filteredRecipes.size();
    }

    public void setScrollOffset(int scrollOffset) {
        this.scrollOffset = scrollOffset;
        updateListingSlots();
    }

    public void updateListingSlots() {
        int i = scrollOffset * 3;
        for (final var slot : marketSlots) {
            if (i < filteredRecipes.size()) {
                slot.setRecipe(filteredRecipes.get(i));
                i++;
            } else {
                slot.setRecipe(null);
            }
        }
    }

    public boolean isScrollOffsetDirty() {
        return scrollOffsetDirty;
    }

    public void setScrollOffsetDirty(boolean scrollOffsetDirty) {
        this.scrollOffsetDirty = scrollOffsetDirty;
    }

    public Optional<SimpleHolder<MarketCategory>> getCurrentCategory() {
        return Optional.ofNullable(currentCategory);
    }

    public List<SimpleHolder<MarketCategory>> getCategories() {
        return categories;
    }

    public void setRecipes(List<RecipeDisplayEntry> recipes) {
        this.recipes = recipes;
        updateFilteredRecipes();
        setScrollOffset(0);
    }

    public void setCategories(List<SimpleHolder<MarketCategory>> categories) {
        this.categories = categories;
        setScrollOffsetDirty(true);
    }

    private Optional<MarketCategory> resolveMarketCategory(ResourceLocation identifier) {
        return categories.stream().filter(it -> it.id().equals(identifier)).findFirst().map(SimpleHolder::value);
    }

    private Comparator<RecipeDisplayEntry> sorting(Level level) {
        final var contextMap = SlotDisplayContext.fromLevel(player.level());
        return Comparator.comparingInt(
                        (RecipeDisplayEntry recipe) -> recipe.display() instanceof MarketRecipeDisplay marketRecipeDisplay ? resolveMarketCategory(marketRecipeDisplay.category())
                                .map(MarketCategory::sortIndex)
                                .orElse(0) : 0)
                .thenComparing(recipe -> recipe.display().result().resolveForFirstStack(contextMap)
                        .getDisplayName()
                        .getString());
    }
}
