package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryDefaultHandler;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class VanillaAddon {

    private static final ResourceLocation[] ANIMALS = new ResourceLocation[]{
            new ResourceLocation("minecraft", "pig"),
            new ResourceLocation("minecraft", "sheep"),
            new ResourceLocation("minecraft", "cow"),
            new ResourceLocation("minecraft", "chicken"),
            new ResourceLocation("minecraft", "horse"),
            new ResourceLocation("minecraft", "ocelot"),
            new ResourceLocation("minecraft", "rabbit"),
            new ResourceLocation("minecraft", "polar_bear"),
            new ResourceLocation("minecraft", "wolf"),
            new ResourceLocation("minecraft", "llama"),
            new ResourceLocation("minecraft", "parrot"),
    };

    public VanillaAddon() {
        FarmingForBlockheadsAPI.registerMarketDefaultHandler("Vanilla Seeds", new MarketRegistryDefaultHandler() {
            @Override
            public void apply(ItemStack defaultPayment) {
                apply(defaultPayment, 1);
            }

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
            public void apply(ItemStack defaultPayment) {
                apply(defaultPayment, 1);
            }

            @Override
            public void apply(ItemStack defaultPayment, int defaultAmount) {
                FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(Blocks.DANDELION, defaultAmount), defaultPayment, FarmingForBlockheadsAPI.getMarketCategoryFlowers());
                for (int i = 0; i <= 8; i++) {
                    FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(Blocks.POPPY, defaultAmount, i), defaultPayment, FarmingForBlockheadsAPI.getMarketCategoryFlowers());
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
            public void apply(ItemStack defaultPayment) {
                apply(defaultPayment, 1);
            }

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
            public void apply(ItemStack defaultPayment) {
                for (BlockPlanks.EnumType type : BlockSapling.TYPE.getAllowedValues()) {
                    FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(Blocks.SAPLING, 1, type.getMetadata()), defaultPayment, FarmingForBlockheadsAPI.getMarketCategorySaplings());
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

        FarmingForBlockheadsAPI.registerMarketDefaultHandler("Bonemeal", new MarketRegistryDefaultHandler() {
            @Override
            public void apply(ItemStack defaultPayment) {
                FarmingForBlockheadsAPI.registerMarketEntry(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()), defaultPayment, FarmingForBlockheadsAPI.getMarketCategoryOther());
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
            public void apply(ItemStack defaultPayment) {
                for (ResourceLocation animalName : ANIMALS) {
                    ItemStack eggStack = new ItemStack(Items.SPAWN_EGG);
                    ItemMonsterPlacer.applyEntityIdToItemStack(eggStack, animalName);
                    FarmingForBlockheadsAPI.registerMarketEntry(eggStack, defaultPayment, FarmingForBlockheadsAPI.getMarketCategoryOther());
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
