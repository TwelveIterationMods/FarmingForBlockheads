package net.blay09.mods.farmingforblockheads.fabric.datagen;

import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider<Item> {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ITEM, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(ModItemTags.RICH_FARMLAND).add(ModBlocks.fertilizedFarmlandRichStable.asItem(), ModBlocks.fertilizedFarmlandRich.asItem());
        getOrCreateTagBuilder(ModItemTags.HEALTHY_FARMLAND).add(ModBlocks.fertilizedFarmlandHealthyStable.asItem(),
                ModBlocks.fertilizedFarmlandHealthy.asItem());
        getOrCreateTagBuilder(ModItemTags.STABLE_FARMLAND).add(ModBlocks.fertilizedFarmlandHealthyStable.asItem(),
                ModBlocks.fertilizedFarmlandRichStable.asItem(),
                ModBlocks.fertilizedFarmlandStable.asItem());

        getOrCreateTagBuilder(ModItemTags.FERTILIZED_FARMLAND).add(ModBlocks.fertilizedFarmlandHealthyStable.asItem(),
                ModBlocks.fertilizedFarmlandRichStable.asItem(),
                ModBlocks.fertilizedFarmlandStable.asItem(),
                ModBlocks.fertilizedFarmlandHealthy.asItem(),
                ModBlocks.fertilizedFarmlandRich.asItem());
    }
}
