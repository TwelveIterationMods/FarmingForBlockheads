package net.blay09.mods.farmingforblockheads.tile;

import net.blay09.mods.farmingforblockheads.block.ChickenNestBlock;
import net.blay09.mods.farmingforblockheads.block.FeedingTroughBlock;
import net.blay09.mods.farmingforblockheads.block.MarketBlock;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ModTileEntities {

    public static TileEntityType<ChickenNestTileEntity> chickenNest;
    public static TileEntityType<FeedingTroughTileEntity> feedingTrough;
    public static TileEntityType<MarketTileEntity> market;

    public static void register(IForgeRegistry<TileEntityType<?>> registry) {
        registry.registerAll(
                chickenNest = build(ChickenNestTileEntity::new, ChickenNestBlock.registryName, ModBlocks.chickenNest),
                feedingTrough = build(FeedingTroughTileEntity::new, FeedingTroughBlock.registryName, ModBlocks.feedingTrough),
                market = build(MarketTileEntity::new, MarketBlock.registryName, ModBlocks.market)
        );
    }

    @SuppressWarnings("unchecked")
    private static <T extends TileEntity> TileEntityType<T> build(Supplier<T> factory, ResourceLocation registryName, Block block) {
        //noinspection ConstantConditions
        return (TileEntityType<T>) TileEntityType.Builder.create(factory, block).build(null).setRegistryName(registryName);
    }

}
