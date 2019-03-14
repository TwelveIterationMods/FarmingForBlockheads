package net.blay09.mods.farmingforblockheads.item;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static final Item fertilizer = Items.AIR;

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                new ItemFertilizer().setRegistryName(ItemFertilizer.name)
        );
    }

}
