package net.blay09.mods.farmingforblockheads.fabric.datagen;

import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.world.level.block.Blocks;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected ModBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.market);
        dropSelf(ModBlocks.chickenNest);
        dropSelf(ModBlocks.feedingTrough);
        dropOther(ModBlocks.fertilizedFarmlandHealthy, Blocks.DIRT);
        dropOther(ModBlocks.fertilizedFarmlandRich, Blocks.DIRT);
        dropOther(ModBlocks.fertilizedFarmlandStable, Blocks.DIRT);
        dropOther(ModBlocks.fertilizedFarmlandHealthyStable, Blocks.DIRT);
        dropOther(ModBlocks.fertilizedFarmlandRichStable, Blocks.DIRT);
    }
}
