package net.blay09.mods.farmingforblockheads.menu;

import net.blay09.mods.balm.api.container.DefaultContainer;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public class MarketPaymentContainer extends DefaultContainer implements StackedContentsCompatible {
    public MarketPaymentContainer(int size) {
        super(size);
    }

    public RecipeInput asRecipeInput() {
        return new SingleRecipeInput(getItem(0));
    }

    @Override
    public void fillStackedContents(StackedItemContents stackedItemContents) {
        for (final var itemStack : getItems()) {
            stackedItemContents.accountSimpleStack(itemStack);
        }
    }
}
