package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static Block market;
    public static Block chickenNest;
    public static Block feedingTrough;
    public static Block fertilizedFarmlandRich;
    public static Block fertilizedFarmlandRichStable;
    public static Block fertilizedFarmlandHealthy;
    public static Block fertilizedFarmlandHealthyStable;
    public static Block fertilizedFarmlandStable;

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                market = new BlockMarket().setRegistryName(BlockMarket.name),
                chickenNest = new BlockChickenNest().setRegistryName(BlockChickenNest.name),
                feedingTrough = new BlockFeedingTrough().setRegistryName(BlockFeedingTrough.name),
                fertilizedFarmlandRich = new BlockFertilizedFarmland(new BlockFertilizedFarmland.FarmlandRichTrait()).setRegistryName("fertilized_farmland_rich"),
                fertilizedFarmlandHealthy = new BlockFertilizedFarmland(new BlockFertilizedFarmland.FarmlandHealthyTrait()).setRegistryName("fertilized_farmland_healthy"),
                fertilizedFarmlandRichStable = new BlockFertilizedFarmland(
                        new BlockFertilizedFarmland.FarmlandRichTrait(),
                        new BlockFertilizedFarmland.FarmlandStableTrait()
                ).setRegistryName("fertilized_farmland_rich_stable"),
                fertilizedFarmlandHealthyStable = new BlockFertilizedFarmland(
                        new BlockFertilizedFarmland.FarmlandHealthyTrait(),
                        new BlockFertilizedFarmland.FarmlandStableTrait()
                ).setRegistryName("fertilized_farmland_healthy_stable"),
                fertilizedFarmlandStable = new BlockFertilizedFarmland(new BlockFertilizedFarmland.FarmlandStableTrait()).setRegistryName("fertilized_farmland_stable")
        );
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                new ItemBlock(market, itemProperties()).setRegistryName(BlockMarket.name),
                new ItemBlock(chickenNest, itemProperties()).setRegistryName(BlockChickenNest.name),
                new ItemBlock(feedingTrough, itemProperties()).setRegistryName(BlockFeedingTrough.name),
                new ItemBlock(fertilizedFarmlandRich, itemProperties()).setRegistryName("fertilized_farmland_rich"),
                new ItemBlock(fertilizedFarmlandHealthy, itemProperties()).setRegistryName("fertilized_farmland_healthy"),
                new ItemBlock(fertilizedFarmlandRichStable, itemProperties()).setRegistryName("fertilized_farmland_rich_stable"),
                new ItemBlock(fertilizedFarmlandHealthyStable, itemProperties()).setRegistryName("fertilized_farmland_healthy_stable"),
                new ItemBlock(fertilizedFarmlandStable, itemProperties()).setRegistryName("fertilized_farmland_stable")
        );
    }

    public static Item.Properties itemProperties() {
        return new Item.Properties().group(FarmingForBlockheads.itemGroup);
    }

}
