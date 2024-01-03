package net.blay09.mods.farmingforblockheads.registry;

import net.blay09.mods.farmingforblockheads.api.MarketCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record MarketCategoryImpl(ResourceLocation id, Component tooltip, ItemStack iconStack, int sortIndex) implements MarketCategory {
}
