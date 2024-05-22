package net.blay09.mods.farmingforblockheads.block.entity;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static DeferredObject<BlockEntityType<ChickenNestBlockEntity>> chickenNest = Balm.getBlockEntities()
            .registerBlockEntity(id("chicken_nest"), ChickenNestBlockEntity::new, () -> new Block[]{ModBlocks.chickenNest});
    public static DeferredObject<BlockEntityType<FeedingTroughBlockEntity>> feedingTrough = Balm.getBlockEntities()
            .registerBlockEntity(id("feeding_trough"), FeedingTroughBlockEntity::new, () -> new Block[]{ModBlocks.feedingTrough});
    public static DeferredObject<BlockEntityType<MarketBlockEntity>> market = Balm.getBlockEntities()
            .registerBlockEntity(id("market"), MarketBlockEntity::new, () -> new Block[]{ModBlocks.market});

    public static void initialize(BalmBlockEntities blockEntities) {
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(FarmingForBlockheads.MOD_ID, path);
    }

}
