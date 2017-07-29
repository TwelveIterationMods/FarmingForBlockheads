package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.tile.TileMarket;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(FarmingForBlockheads.MOD_ID)
public class ModBlocks {

	@GameRegistry.ObjectHolder(BlockMarket.name)
	public static final Block market = Blocks.AIR;

	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				new BlockMarket().setRegistryName(BlockMarket.name)
		);
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				new ItemBlock(ModBlocks.market).setRegistryName(BlockMarket.name)
		);
	}

	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(market), 0, new ModelResourceLocation(BlockMarket.registryName, "inventory"));
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileMarket.class, BlockMarket.registryName.toString());
	}
}
