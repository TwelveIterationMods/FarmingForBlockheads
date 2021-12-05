package net.blay09.mods.farmingforblockheads.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItems {

    public static final CreativeModeTab creativeModeTab = Balm.getItems().createCreativeModeTab(id("farmingforblockheads"), () -> new ItemStack(ModBlocks.market));

    public static Item fertilizerGreen = new FertilizerItem(FertilizerItem.FertilizerType.RICH);
    public static Item fertilizerRed = new FertilizerItem(FertilizerItem.FertilizerType.HEALTHY);
    public static Item fertilizerYellow = new FertilizerItem(FertilizerItem.FertilizerType.STABLE);

    public static void initialize(BalmItems items) {
        items.registerItem(() -> fertilizerGreen, id("green_fertilizer"));
        items.registerItem(() -> fertilizerRed, id("red_fertilizer"));
        items.registerItem(() -> fertilizerYellow, id("yellow_fertilizer"));
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(FarmingForBlockheads.MOD_ID, path);
    }

}
