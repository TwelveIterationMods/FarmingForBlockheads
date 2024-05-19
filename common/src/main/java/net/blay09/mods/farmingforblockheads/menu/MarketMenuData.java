package net.blay09.mods.farmingforblockheads.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public record MarketMenuData(BlockPos pos, Set<ResourceLocation> presetFilters, Set<ResourceLocation> categoryFilters) {
}
