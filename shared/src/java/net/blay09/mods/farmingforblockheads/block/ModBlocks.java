package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModBlocks {

    public static Block market = new MarketBlock();
    public static Block chickenNest = new ChickenNestBlock();
    public static Block feedingTrough = new FeedingTroughBlock();
    public static Block fertilizedFarmlandRich = new FertilizedFarmlandBlock(new FertilizedFarmlandBlock.FarmlandRichTrait());
    public static Block fertilizedFarmlandRichStable = new FertilizedFarmlandBlock(new FertilizedFarmlandBlock.FarmlandRichTrait(), new FertilizedFarmlandBlock.FarmlandStableTrait());
    public static Block fertilizedFarmlandHealthy = new FertilizedFarmlandBlock(new FertilizedFarmlandBlock.FarmlandHealthyTrait());
    public static Block fertilizedFarmlandHealthyStable = new FertilizedFarmlandBlock(new FertilizedFarmlandBlock.FarmlandHealthyTrait(), new FertilizedFarmlandBlock.FarmlandStableTrait());
    public static Block fertilizedFarmlandStable = new FertilizedFarmlandBlock(new FertilizedFarmlandBlock.FarmlandStableTrait());

    public static void initialize(BalmBlocks blocks) {
        blocks.register(() -> market, () -> new BlockItem(market, itemProperties()), id("market"));
        blocks.register(() -> chickenNest, () -> new BlockItem(chickenNest, itemProperties()), id("chicken_nest"));
        blocks.register(() -> feedingTrough, () -> new BlockItem(feedingTrough, itemProperties()), id("feeding_trough"));
        blocks.register(() -> fertilizedFarmlandRich, () -> new BlockItem(fertilizedFarmlandRich, itemProperties()), id("fertilized_farmland_rich"));
        blocks.register(() -> fertilizedFarmlandRichStable, () -> new BlockItem(fertilizedFarmlandRichStable, itemProperties()), id("fertilized_farmland_rich_stable"));
        blocks.register(() -> fertilizedFarmlandHealthy, () -> new BlockItem(fertilizedFarmlandHealthy, itemProperties()), id("fertilized_farmland_healthy"));
        blocks.register(() -> fertilizedFarmlandHealthyStable, () -> new BlockItem(fertilizedFarmlandHealthyStable, itemProperties()), id("fertilized_farmland_healthy_stable"));
        blocks.register(() -> fertilizedFarmlandStable, () -> new BlockItem(fertilizedFarmlandStable, itemProperties()), id("fertilized_farmland_stable"));
    }

    public static Item.Properties itemProperties() {
        return new Item.Properties().tab(ModItems.creativeModeTab);
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(FarmingForBlockheads.MOD_ID, path);
    }
}
