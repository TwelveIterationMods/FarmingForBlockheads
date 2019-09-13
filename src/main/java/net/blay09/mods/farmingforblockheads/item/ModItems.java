package net.blay09.mods.farmingforblockheads.item;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static Item fertilizerGreen;
    public static Item fertilizerRed;
    public static Item fertilizerYellow;

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                fertilizerGreen = new FertilizerItem(FertilizerItem.FertilizerType.RICH).setRegistryName("green_fertilizer"),
                fertilizerRed = new FertilizerItem(FertilizerItem.FertilizerType.HEALTHY).setRegistryName("red_fertilizer"),
                fertilizerYellow = new FertilizerItem(FertilizerItem.FertilizerType.STABLE).setRegistryName("yellow_fertilizer")
        );
    }

}
