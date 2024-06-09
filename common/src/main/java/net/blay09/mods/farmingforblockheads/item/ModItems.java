package net.blay09.mods.farmingforblockheads.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItems {

    public static Item greenFertilizer;
    public static Item redFertilizer;
    public static Item yellowFertilizer;

    public static TagKey<Item> EGGS_TAG;

    public static void initialize(BalmItems items) {
        items.registerItem(() -> greenFertilizer = new FertilizerItem(FertilizerItem.FertilizerType.RICH), id("green_fertilizer"));
        items.registerItem(() -> redFertilizer = new FertilizerItem(FertilizerItem.FertilizerType.HEALTHY), id("red_fertilizer"));
        items.registerItem(() -> yellowFertilizer = new FertilizerItem(FertilizerItem.FertilizerType.STABLE), id("yellow_fertilizer"));

        items.registerCreativeModeTab(() -> new ItemStack(ModBlocks.market), id("farmingforblockheads"));

        EGGS_TAG = Balm.getRegistries().getItemTag(ResourceLocation.fromNamespaceAndPath("c", "eggs"));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(FarmingForBlockheads.MOD_ID, path);
    }

}
