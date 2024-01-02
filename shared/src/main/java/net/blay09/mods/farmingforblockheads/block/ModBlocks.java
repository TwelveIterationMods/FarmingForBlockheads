package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {

    public static Block market;
    public static Block chickenNest;
    public static Block feedingTrough;
    public static Block fertilizedFarmlandRich;
    public static Block fertilizedFarmlandRichStable;
    public static Block fertilizedFarmlandHealthy;
    public static Block fertilizedFarmlandHealthyStable;
    public static Block fertilizedFarmlandStable;

    public static void initialize(BalmBlocks blocks) {
        blocks.register(() -> market = new MarketBlock(defaultProperties()), () -> new BlockItem(market, itemProperties()), id("market"));
        blocks.register(() -> chickenNest = new ChickenNestBlock(defaultProperties()), () -> new BlockItem(chickenNest, itemProperties()), id("chicken_nest"));
        blocks.register(() -> feedingTrough = new FeedingTroughBlock(defaultProperties()), () -> new BlockItem(feedingTrough, itemProperties()), id("feeding_trough"));
        blocks.register(() -> fertilizedFarmlandRich = new FertilizedFarmlandBlock(defaultProperties()), () -> new BlockItem(fertilizedFarmlandRich, itemProperties()), id("fertilized_farmland_rich"));
        blocks.register(() -> fertilizedFarmlandRichStable = new FertilizedFarmlandBlock(defaultProperties()), () -> new BlockItem(fertilizedFarmlandRichStable, itemProperties()), id("fertilized_farmland_rich_stable"));
        blocks.register(() -> fertilizedFarmlandHealthy = new FertilizedFarmlandBlock(defaultProperties()), () -> new BlockItem(fertilizedFarmlandHealthy, itemProperties()), id("fertilized_farmland_healthy"));
        blocks.register(() -> fertilizedFarmlandHealthyStable = new FertilizedFarmlandBlock(defaultProperties()), () -> new BlockItem(fertilizedFarmlandHealthyStable, itemProperties()), id("fertilized_farmland_healthy_stable"));
        blocks.register(() -> fertilizedFarmlandStable = new FertilizedFarmlandBlock(defaultProperties()), () -> new BlockItem(fertilizedFarmlandStable, itemProperties()), id("fertilized_farmland_stable"));
    }

    public static Item.Properties itemProperties() {
        return new Item.Properties();
    }

    private static BlockBehaviour.Properties defaultProperties() {
        return Balm.getBlocks().blockProperties();
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(FarmingForBlockheads.MOD_ID, path);
    }
}
