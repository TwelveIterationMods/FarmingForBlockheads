package net.blay09.mods.farmingforblockheads.fabric.datagen;

import net.blay09.mods.balm.api.tag.BalmItemTags;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        shaped(RecipeCategory.MISC, ModItems.yellowFertilizer, 4)
                .pattern("GGG")
                .pattern("NSN")
                .pattern("DDD")
                .define('G', BalmItemTags.YELLOW_DYES)
                .define('N', BalmItemTags.GOLD_NUGGETS)
                .define('S', Items.WHEAT_SEEDS)
                .define('D', Items.DIRT)
                .unlockedBy("has_wheat_seeds", has(Items.WHEAT_SEEDS))
                .save(exporter);

        shaped(RecipeCategory.MISC, ModItems.redFertilizer, 4)
                .pattern("RRR")
                .pattern("NSN")
                .pattern("BBB")
                .define('R', BalmItemTags.RED_DYES)
                .define('N', Items.GOLD_NUGGET)
                .define('S', Items.WHEAT_SEEDS)
                .define('B', Items.BONE_MEAL)
                .unlockedBy("has_wheat_seeds", has(Items.WHEAT_SEEDS))
                .save(exporter);

        shaped(RecipeCategory.MISC, ModItems.greenFertilizer, 4)
                .pattern("GGG")
                .pattern("NSN")
                .pattern("WWW")
                .define('G', BalmItemTags.GREEN_DYES)
                .define('N', Items.GOLD_NUGGET)
                .define('S', Items.WHEAT_SEEDS)
                .define('W', Items.WHEAT)
                .unlockedBy("has_wheat_seeds", has(Items.WHEAT_SEEDS))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.chickenNest)
                .pattern("PHP")
                .define('H', Items.HAY_BLOCK)
                .define('P', ItemTags.PLANKS)
                .unlockedBy("has_wheat", has(Items.WHEAT))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.market)
                .pattern("PCP")
                .pattern("W W")
                .pattern("WWW")
                .define('C', Items.RED_WOOL)
                .define('P', ItemTags.PLANKS)
                .define('W', ItemTags.LOGS)
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(exporter);

        shaped(RecipeCategory.DECORATIONS, ModBlocks.feedingTrough)
                .pattern("PCP")
                .pattern("PHP")
                .define('H', Items.HAY_BLOCK)
                .define('P', ItemTags.PLANKS)
                .define('C', Items.GOLDEN_CARROT)
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                .save(exporter);
    }

}
