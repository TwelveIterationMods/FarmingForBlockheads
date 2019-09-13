package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryDefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class VanillaAddon {

    public VanillaAddon() {
        FarmingForBlockheadsAPI.registerMarketDefaultHandler("Vanilla Seeds", new MarketRegistryDefaultHandler() {
            @Override
            public void apply(ItemStack defaultPayment, int defaultAmount) {
                FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(Items.WHEAT_SEEDS, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySeeds());
                FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(Items.MELON_SEEDS, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySeeds());
                FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(Items.PUMPKIN_SEEDS, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySeeds());
                FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(Items.BEETROOT_SEEDS, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySeeds());
            }

            @Override
            public boolean isEnabledByDefault() {
                return true;
            }

            @Override
            public ItemStack getDefaultPayment() {
                return new ItemStack(Items.EMERALD);
            }
        });

        FarmingForBlockheadsAPI.registerMarketDefaultHandler("Vanilla Flowers", new MarketRegistryDefaultHandler() {
            @Override
            public void apply(ItemStack defaultPayment, int defaultAmount) {
                final Block[] FLOWERS = new Block[]{
                        Blocks.DANDELION,
                        Blocks.POPPY,
                        Blocks.BLUE_ORCHID,
                        Blocks.ALLIUM,
                        Blocks.AZURE_BLUET,
                        Blocks.RED_TULIP,
                        Blocks.ORANGE_TULIP,
                        Blocks.WHITE_TULIP,
                        Blocks.PINK_TULIP,
                        Blocks.OXEYE_DAISY
                };

                for (Block flower : FLOWERS) {
                    FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(flower, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategoryFlowers());
                }
            }

            @Override
            public boolean isEnabledByDefault() {
                return false;
            }

            @Override
            public ItemStack getDefaultPayment() {
                return new ItemStack(Items.EMERALD);
            }
        });

        FarmingForBlockheadsAPI.registerMarketDefaultHandler("Vanilla Mushrooms", new MarketRegistryDefaultHandler() {
            @Override
            public void apply(ItemStack defaultPayment, int defaultAmount) {
                FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(Blocks.BROWN_MUSHROOM, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategoryOther());
                FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(Blocks.RED_MUSHROOM, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategoryOther());
            }

            @Override
            public boolean isEnabledByDefault() {
                return false;
            }

            @Override
            public ItemStack getDefaultPayment() {
                return new ItemStack(Items.EMERALD);
            }
        });

        FarmingForBlockheadsAPI.registerMarketDefaultHandler("Vanilla Saplings", new MarketRegistryDefaultHandler() {
            @Override
            public void apply(ItemStack defaultPayment, int defaultAmount) {
                final Block[] SAPLINGS = new Block[]{
                        Blocks.OAK_SAPLING,
                        Blocks.SPRUCE_SAPLING,
                        Blocks.BIRCH_SAPLING,
                        Blocks.JUNGLE_SAPLING,
                        Blocks.ACACIA_SAPLING,
                        Blocks.DARK_OAK_SAPLING
                };

                for (Block sapling : SAPLINGS) {
                    FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(sapling, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySaplings());
                }
            }

            @Override
            public boolean isEnabledByDefault() {
                return true;
            }

            @Override
            public ItemStack getDefaultPayment() {
                return new ItemStack(Items.EMERALD);
            }
        });

        FarmingForBlockheadsAPI.registerMarketDefaultHandler("Bone Meal", new MarketRegistryDefaultHandler() {
            @Override
            public void apply(ItemStack defaultPayment, int defaultAmount) {
                FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(Items.BONE_MEAL, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategoryOther());
            }

            @Override
            public boolean isEnabledByDefault() {
                return true;
            }

            @Override
            public ItemStack getDefaultPayment() {
                return new ItemStack(Items.EMERALD);
            }
        });

        FarmingForBlockheadsAPI.registerMarketDefaultHandler("Animal Eggs", new MarketRegistryDefaultHandler() {
            @Override
            public void apply(ItemStack defaultPayment, int defaultAmount) {
                final Item[] SPAWN_EGGS = new Item[]{
                        Items.CHICKEN_SPAWN_EGG,
                        Items.COD_SPAWN_EGG,
                        Items.COW_SPAWN_EGG,
                        Items.DOLPHIN_SPAWN_EGG,
                        Items.DONKEY_SPAWN_EGG,
                        Items.HORSE_SPAWN_EGG,
                        Items.LLAMA_SPAWN_EGG,
                        Items.MOOSHROOM_SPAWN_EGG,
                        Items.MULE_SPAWN_EGG,
                        Items.OCELOT_SPAWN_EGG,
                        Items.PARROT_SPAWN_EGG,
                        Items.PIG_SPAWN_EGG,
                        Items.POLAR_BEAR_SPAWN_EGG,
                        Items.PUFFERFISH_SPAWN_EGG,
                        Items.RABBIT_SPAWN_EGG,
                        Items.SALMON_SPAWN_EGG,
                        Items.SHEEP_SPAWN_EGG,
                        Items.SQUID_SPAWN_EGG,
                        Items.TROPICAL_FISH_SPAWN_EGG,
                        Items.TURTLE_SPAWN_EGG,
                        Items.WOLF_SPAWN_EGG
                };

                for (Item spawnEgg : SPAWN_EGGS) {
                    FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(spawnEgg, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategoryOther());
                }
            }

            @Override
            public boolean isEnabledByDefault() {
                return false;
            }

            @Override
            public ItemStack getDefaultPayment() {
                return new ItemStack(Items.EMERALD);
            }
        });
    }

}
