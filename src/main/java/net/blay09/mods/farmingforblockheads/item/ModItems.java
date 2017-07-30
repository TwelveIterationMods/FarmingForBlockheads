package net.blay09.mods.farmingforblockheads.item;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(FarmingForBlockheads.MOD_ID)
public class ModItems {

	@GameRegistry.ObjectHolder(ItemFertilizer.name)
	public static final Item fertilizer = Items.AIR;

	public static void register(IForgeRegistry<Item> registry) {
		registry.registerAll(
				new ItemFertilizer().setRegistryName(ItemFertilizer.name)
		);
	}

	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(fertilizer, 0, new ModelResourceLocation("farmingforblockheads:fertilizer_healthy", "inventory"));
		ModelLoader.setCustomModelResourceLocation(fertilizer, 1, new ModelResourceLocation("farmingforblockheads:fertilizer_rich", "inventory"));
		ModelLoader.setCustomModelResourceLocation(fertilizer, 2, new ModelResourceLocation("farmingforblockheads:fertilizer_stable", "inventory"));
	}

}
