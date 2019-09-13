package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
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
                market = new MarketBlock().setRegistryName(MarketBlock.name),
                chickenNest = new ChickenNestBlock().setRegistryName(ChickenNestBlock.name),
                feedingTrough = new FeedingTroughBlock().setRegistryName(FeedingTroughBlock.name),
                fertilizedFarmlandRich = new FertilizedFarmlandBlock(new FertilizedFarmlandBlock.FarmlandRichTrait()).setRegistryName("fertilized_farmland_rich"),
                fertilizedFarmlandHealthy = new FertilizedFarmlandBlock(new FertilizedFarmlandBlock.FarmlandHealthyTrait()).setRegistryName("fertilized_farmland_healthy"),
                fertilizedFarmlandRichStable = new FertilizedFarmlandBlock(
                        new FertilizedFarmlandBlock.FarmlandRichTrait(),
                        new FertilizedFarmlandBlock.FarmlandStableTrait()
                ).setRegistryName("fertilized_farmland_rich_stable"),
                fertilizedFarmlandHealthyStable = new FertilizedFarmlandBlock(
                        new FertilizedFarmlandBlock.FarmlandHealthyTrait(),
                        new FertilizedFarmlandBlock.FarmlandStableTrait()
                ).setRegistryName("fertilized_farmland_healthy_stable"),
                fertilizedFarmlandStable = new FertilizedFarmlandBlock(new FertilizedFarmlandBlock.FarmlandStableTrait()).setRegistryName("fertilized_farmland_stable")
        );
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                new BlockItem(market, itemProperties()).setRegistryName(MarketBlock.name),
                new BlockItem(chickenNest, itemProperties()).setRegistryName(ChickenNestBlock.name),
                new BlockItem(feedingTrough, itemProperties()).setRegistryName(FeedingTroughBlock.name),
                new BlockItem(fertilizedFarmlandRich, itemProperties()).setRegistryName("fertilized_farmland_rich"),
                new BlockItem(fertilizedFarmlandHealthy, itemProperties()).setRegistryName("fertilized_farmland_healthy"),
                new BlockItem(fertilizedFarmlandRichStable, itemProperties()).setRegistryName("fertilized_farmland_rich_stable"),
                new BlockItem(fertilizedFarmlandHealthyStable, itemProperties()).setRegistryName("fertilized_farmland_healthy_stable"),
                new BlockItem(fertilizedFarmlandStable, itemProperties()).setRegistryName("fertilized_farmland_stable")
        );
    }

    public static Item.Properties itemProperties() {
        return new Item.Properties().group(FarmingForBlockheads.itemGroup);
    }

}
