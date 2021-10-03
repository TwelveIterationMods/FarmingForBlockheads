package net.blay09.mods.farmingforblockheads.block.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static DeferredObject<BlockEntityType<ChickenNestBlockEntity>> chickenNest;
    public static DeferredObject<BlockEntityType<FeedingTroughBlockEntity>> feedingTrough;
    public static DeferredObject<BlockEntityType<MarketBlockEntity>> market;

    public static void initialize(BalmBlockEntities blockEntities) {
        chickenNest = blockEntities.registerBlockEntity(id("chicken_nest"), ChickenNestBlockEntity::new, ModBlocks.chickenNest);
        feedingTrough = blockEntities.registerBlockEntity(id("feeding_trough"), FeedingTroughBlockEntity::new, ModBlocks.feedingTrough);
        market = blockEntities.registerBlockEntity(id("market"), MarketBlockEntity::new, ModBlocks.market);
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(FarmingForBlockheads.MOD_ID, path);
    }

}
