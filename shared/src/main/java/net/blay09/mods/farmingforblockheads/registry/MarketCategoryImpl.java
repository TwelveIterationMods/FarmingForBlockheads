package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.farmingforblockheads.api.MarketCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record MarketCategoryImpl(ItemStack iconStack, int sortIndex, Component tooltip) implements MarketCategory {
}
