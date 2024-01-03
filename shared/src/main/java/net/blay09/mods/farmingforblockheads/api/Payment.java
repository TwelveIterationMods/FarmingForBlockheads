package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.world.item.crafting.Ingredient;

public interface Payment {
    Ingredient ingredient();
    int count();
}
