package net.blay09.mods.farmingforblockheads.tag;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModItemTags {
    public static final TagKey<Item> RICH_FARMLAND = TagKey.create(Registries.ITEM, new ResourceLocation(FarmingForBlockheads.MOD_ID, "rich_farmland"));
    public static final TagKey<Item> HEALTHY_FARMLAND = TagKey.create(Registries.ITEM, new ResourceLocation(FarmingForBlockheads.MOD_ID, "healthy_farmland"));
    public static final TagKey<Item> STABLE_FARMLAND = TagKey.create(Registries.ITEM, new ResourceLocation(FarmingForBlockheads.MOD_ID, "stable_farmland"));
}
