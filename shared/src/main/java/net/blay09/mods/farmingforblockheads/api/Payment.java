package net.blay09.mods.farmingforblockheads.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Optional;

public interface Payment {
    Ingredient ingredient();
    int count();

    Optional<Component> tooltip();
}
