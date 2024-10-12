package net.blay09.mods.farmingforblockheads.menu;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MarketListingSlot extends FakeSlot {

    private final Level level;
    private RecipeDisplayEntry recipe;

    public MarketListingSlot(Container container, int slotId, int x, int y, Level level) {
        super(container, slotId, x, y);
        this.level = level;
    }

    @Override
    public ItemStack getItem() {
        if (recipe != null) {
            return recipe.display().result().resolveForFirstStack(SlotDisplayContext.fromLevel(level));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean hasItem() {
        return recipe != null;
    }

    @Override
    public boolean isActive() {
        return recipe != null;
    }

    public void setRecipe(@Nullable RecipeDisplayEntry recipe) {
        this.recipe = recipe;
    }

    @Nullable
    public RecipeDisplayEntry getRecipe() {
        return recipe;
    }

}
