package net.blay09.mods.farmingforblockheads.fabric.datagen;

import net.blay09.mods.balm.api.tag.BalmItemTags;
import net.blay09.mods.farmingforblockheads.api.MarketCategories;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

import static net.blay09.mods.farmingforblockheads.fabric.datagen.MarketRecipeBuilder.market;
import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
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

        final var vanillaSaplings = ResourceLocation.withDefaultNamespace("saplings");
        market(MarketCategories.SAPLINGS, vanillaSaplings, Items.OAK_SAPLING).save(exporter);
        market(MarketCategories.SAPLINGS, vanillaSaplings, Items.SPRUCE_SAPLING).save(exporter);
        market(MarketCategories.SAPLINGS, vanillaSaplings, Items.BIRCH_SAPLING).save(exporter);
        market(MarketCategories.SAPLINGS, vanillaSaplings, Items.JUNGLE_SAPLING).save(exporter);
        market(MarketCategories.SAPLINGS, vanillaSaplings, Items.ACACIA_SAPLING).save(exporter);
        market(MarketCategories.SAPLINGS, vanillaSaplings, Items.DARK_OAK_SAPLING).save(exporter);
        market(MarketCategories.SAPLINGS, vanillaSaplings, Items.CHERRY_SAPLING).save(exporter);

        final var vanillaExoticSaplings = ResourceLocation.withDefaultNamespace("exotic_saplings");
        market(MarketCategories.SAPLINGS, vanillaExoticSaplings, Items.AZALEA).save(exporter);
        market(MarketCategories.SAPLINGS, vanillaExoticSaplings, Items.FLOWERING_AZALEA).save(exporter);
        market(MarketCategories.SAPLINGS, vanillaExoticSaplings, Items.MANGROVE_PROPAGULE).save(exporter);

        final var vanillaSeeds = ResourceLocation.withDefaultNamespace("seeds");
        market(MarketCategories.SEEDS, vanillaSeeds, Items.WHEAT_SEEDS).save(exporter);
        market(MarketCategories.SEEDS, vanillaSeeds, Items.BEETROOT_SEEDS).save(exporter);
        market(MarketCategories.SEEDS, vanillaSeeds, Items.MELON_SEEDS).save(exporter);
        market(MarketCategories.SEEDS, vanillaSeeds, Items.PUMPKIN_SEEDS).save(exporter);

        final var vanillaSeedCrops = ResourceLocation.withDefaultNamespace("seed_crops");
        market(MarketCategories.SEEDS, vanillaSeedCrops, Items.POTATO).save(exporter);
        market(MarketCategories.SEEDS, vanillaSeedCrops, Items.CARROT).save(exporter);

        final var vanillaFlowers = ResourceLocation.withDefaultNamespace("flowers");
        market(MarketCategories.FLOWERS, vanillaFlowers, Items.DANDELION).save(exporter);
        market(MarketCategories.FLOWERS, vanillaFlowers, Items.POPPY).save(exporter);
        market(MarketCategories.FLOWERS, vanillaFlowers, Items.BLUE_ORCHID).save(exporter);
        market(MarketCategories.FLOWERS, vanillaFlowers, Items.ALLIUM).save(exporter);
        market(MarketCategories.FLOWERS, vanillaFlowers, Items.AZURE_BLUET).save(exporter);
        market(MarketCategories.FLOWERS, vanillaFlowers, Items.RED_TULIP).save(exporter);
        market(MarketCategories.FLOWERS, vanillaFlowers, Items.ORANGE_TULIP).save(exporter);
        market(MarketCategories.FLOWERS, vanillaFlowers, Items.WHITE_TULIP).save(exporter);
        market(MarketCategories.FLOWERS, vanillaFlowers, Items.PINK_TULIP).save(exporter);
        market(MarketCategories.FLOWERS, vanillaFlowers, Items.OXEYE_DAISY) .save(exporter);
        market(MarketCategories.FLOWERS, vanillaFlowers, Items.CORNFLOWER).save(exporter);
        market(MarketCategories.FLOWERS, vanillaFlowers, Items.LILY_OF_THE_VALLEY).save(exporter);

        final var vanillaMushrooms = ResourceLocation.withDefaultNamespace("mushrooms");
        market(MarketCategories.OTHER, vanillaMushrooms, Items.BROWN_MUSHROOM).save(exporter);
        market(MarketCategories.OTHER, vanillaMushrooms, Items.RED_MUSHROOM).save(exporter);
        market(MarketCategories.OTHER, vanillaMushrooms, Items.CRIMSON_FUNGUS).save(exporter);
        market(MarketCategories.OTHER, vanillaMushrooms, Items.WARPED_FUNGUS).save(exporter);

        final var vanillaSoil = ResourceLocation.withDefaultNamespace("soil");
        market(MarketCategories.OTHER, vanillaSoil, Items.GRASS_BLOCK).save(exporter);
        market(MarketCategories.OTHER, vanillaSoil, Items.PODZOL).save(exporter);
        market(MarketCategories.OTHER, vanillaSoil, Items.MYCELIUM).save(exporter);
        market(MarketCategories.OTHER, vanillaSoil, Items.CRIMSON_NYLIUM).save(exporter);
        market(MarketCategories.OTHER, vanillaSoil, Items.WARPED_NYLIUM).save(exporter);
        market(MarketCategories.OTHER, vanillaSoil, Items.SOUL_SAND).save(exporter);

        final var vanillaCrops = ResourceLocation.withDefaultNamespace("crops");
        market(MarketCategories.OTHER, vanillaCrops, Items.SWEET_BERRIES).save(exporter);
        market(MarketCategories.OTHER, vanillaCrops, Items.SUGAR_CANE).save(exporter);
        market(MarketCategories.OTHER, vanillaCrops, Items.CACTUS).save(exporter);
        market(MarketCategories.OTHER, vanillaCrops, Items.BAMBOO).save(exporter);
        market(MarketCategories.OTHER, vanillaCrops, Items.KELP).save(exporter);
        market(MarketCategories.OTHER, vanillaCrops, Items.NETHER_WART).save(exporter);

        final var vanillaBoneMeal = ResourceLocation.withDefaultNamespace("bone_meal");
        market(MarketCategories.OTHER, vanillaBoneMeal, Items.BONE_MEAL).save(exporter);

        final var vanillaAnimalEggs = ResourceLocation.withDefaultNamespace("animal_eggs");
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.CHICKEN_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.COW_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.PIG_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.SHEEP_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.WOLF_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.CAT_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.FOX_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.PANDA_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.BEE_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.HORSE_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.DONKEY_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.MULE_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.LLAMA_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.POLAR_BEAR_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.TURTLE_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.SQUID_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.RABBIT_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.OCELOT_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.MOOSHROOM_SPAWN_EGG).save(exporter);
        market(MarketCategories.OTHER, vanillaAnimalEggs, Items.DOLPHIN_SPAWN_EGG).save(exporter);
    }

}
