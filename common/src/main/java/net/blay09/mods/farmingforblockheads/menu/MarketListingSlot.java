package net.blay09.mods.farmingforblockheads.menu;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MarketListingSlot extends FakeSlot {

    private final Level level;
    private RecipeDisplayEntry recipeDisplayEntry;

    public MarketListingSlot(Container container, int slotId, int x, int y, Level level) {
        super(container, slotId, x, y);
        this.level = level;
    }

    @Override
    public ItemStack getItem() {
        if (recipeDisplayEntry != null) {
            return recipeDisplayEntry.display().result().resolveForFirstStack(SlotDisplayContext.fromLevel(level));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean hasItem() {
        return recipeDisplayEntry != null;
    }

    @Override
    public boolean isActive() {
        return recipeDisplayEntry != null;
    }

    public void setRecipeDisplayEntry(@Nullable RecipeDisplayEntry recipeDisplayEntry) {
        this.recipeDisplayEntry = recipeDisplayEntry;
    }

    @Nullable
    public RecipeDisplayEntry getRecipeDisplayEntry() {
        return recipeDisplayEntry;
    }

    @Override
    public boolean isFake() {
        return true;
    }
}
