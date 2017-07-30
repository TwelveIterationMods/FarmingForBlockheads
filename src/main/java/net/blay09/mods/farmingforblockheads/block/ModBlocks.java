package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.tile.TileChickenNest;
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

	@GameRegistry.ObjectHolder(BlockChickenNest.name)
	public static final Block chickenNest = Blocks.AIR;

	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				new BlockMarket().setRegistryName(BlockMarket.name),
				new BlockChickenNest().setRegistryName(BlockChickenNest.name)
		);
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				new ItemBlock(market).setRegistryName(BlockMarket.name),
				new ItemBlock(chickenNest).setRegistryName(BlockChickenNest.name)
		);
	}

	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(market), 0, new ModelResourceLocation(BlockMarket.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(chickenNest), 0, new ModelResourceLocation(BlockChickenNest.registryName, "inventory"));
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileMarket.class, BlockMarket.registryName.toString());
		GameRegistry.registerTileEntity(TileChickenNest.class, BlockChickenNest.registryName.toString());
	}
}
