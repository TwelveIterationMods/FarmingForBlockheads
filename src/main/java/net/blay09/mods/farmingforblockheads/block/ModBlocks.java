package net.blay09.mods.farmingforblockheads.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    private static final String STABLE_SUFFIX = "_stable";
    public static final Block market = Blocks.AIR;
    public static final Block chickenNest = Blocks.AIR;
    public static final Block feedingTrough = Blocks.AIR;
    public static final Block fertilizedFarmlandRich = Blocks.AIR;
    public static final Block fertilizedFarmlandRichStable = Blocks.AIR;
    public static final Block fertilizedFarmlandHealthy = Blocks.AIR;
    public static final Block fertilizedFarmlandHealthyStable = Blocks.AIR;
    public static final Block fertilizedFarmlandStable = Blocks.AIR;

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                new BlockMarket().setRegistryName(BlockMarket.name),
                new BlockChickenNest().setRegistryName(BlockChickenNest.name),
                new BlockFeedingTrough().setRegistryName(BlockFeedingTrough.name),
                new BlockFertilizedFarmlandRich(false).setRegistryName(BlockFertilizedFarmlandRich.name),
                new BlockFertilizedFarmlandHealthy(false).setRegistryName(BlockFertilizedFarmlandHealthy.name),
                new BlockFertilizedFarmlandRich(true).setRegistryName(BlockFertilizedFarmlandRich.name + STABLE_SUFFIX),
                new BlockFertilizedFarmlandHealthy(true).setRegistryName(BlockFertilizedFarmlandHealthy.name + STABLE_SUFFIX),
                new BlockFertilizedFarmlandStable().setRegistryName(BlockFertilizedFarmlandStable.name)
        );
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                new ItemBlock(market).setRegistryName(BlockMarket.name),
                new ItemBlock(chickenNest).setRegistryName(BlockChickenNest.name),
                new ItemBlock(feedingTrough).setRegistryName(BlockFeedingTrough.name),
                new ItemBlock(fertilizedFarmlandRich).setRegistryName(BlockFertilizedFarmlandRich.name),
                new ItemBlock(fertilizedFarmlandHealthy).setRegistryName(BlockFertilizedFarmlandHealthy.name),
                new ItemBlock(fertilizedFarmlandRichStable).setRegistryName(BlockFertilizedFarmlandRich.name + STABLE_SUFFIX),
                new ItemBlock(fertilizedFarmlandHealthyStable).setRegistryName(BlockFertilizedFarmlandHealthy.name + STABLE_SUFFIX),
                new ItemBlock(fertilizedFarmlandStable).setRegistryName(BlockFertilizedFarmlandStable.name)
        );
    }

}
