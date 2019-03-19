package net.blay09.mods.farmingforblockheads.item;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static Item fertilizerGreen;
    public static Item fertilizerRed;
    public static Item fertilizerYellow;

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                fertilizerGreen = new ItemFertilizer(ItemFertilizer.FertilizerType.RICH).setRegistryName("fertilizer_green"),
                fertilizerRed = new ItemFertilizer(ItemFertilizer.FertilizerType.HEALTHY).setRegistryName("fertilizer_red"),
                fertilizerYellow = new ItemFertilizer(ItemFertilizer.FertilizerType.STABLE).setRegistryName("fertilizer_yellow")
        );
    }

}
