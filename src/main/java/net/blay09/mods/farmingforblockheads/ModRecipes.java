package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

	public static void init() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.market), "PCP", "WEW", "WWW", 'C', new ItemStack(Blocks.WOOL, 1, EnumDyeColor.RED.getMetadata()), 'P', "plankWood", 'E', Items.EMERALD, 'W', "logWood"));
	}

}
