package net.blay09.mods.farmingforblockheads.compat;

import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistryDefaultHandler;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;

public class VanillaAddon {

	private static final String[] ANIMALS = new String[]{
			"minecraft:pig", "minecraft:sheep", "minecraft:cow", "minecraft:chicken", "minecraft:horse"
	};

	public VanillaAddon() {
		MarketRegistry.INSTANCE.registerDefaultHandler("Vanilla Seeds", new MarketRegistryDefaultHandler() {
			@Override
			public void apply(MarketRegistry registry) {
				registry.registerEntry(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.EMERALD), MarketEntry.EntryType.SEEDS);
				registry.registerEntry(new ItemStack(Items.MELON_SEEDS), new ItemStack(Items.EMERALD), MarketEntry.EntryType.SEEDS);
				registry.registerEntry(new ItemStack(Items.PUMPKIN_SEEDS), new ItemStack(Items.EMERALD), MarketEntry.EntryType.SEEDS);
				registry.registerEntry(new ItemStack(Items.BEETROOT_SEEDS), new ItemStack(Items.EMERALD), MarketEntry.EntryType.SEEDS);
			}

			@Override
			public boolean isEnabledByDefault() {
				return true;
			}
		});

		MarketRegistry.INSTANCE.registerDefaultHandler("Vanilla Saplings", new MarketRegistryDefaultHandler() {
			@Override
			public void apply(MarketRegistry registry) {
				for (BlockPlanks.EnumType type : BlockSapling.TYPE.getAllowedValues()) {
					registry.registerEntry(new ItemStack(Blocks.SAPLING, 1, type.getMetadata()), new ItemStack(Items.EMERALD), MarketEntry.EntryType.SAPLINGS);
				}
			}

			@Override
			public boolean isEnabledByDefault() {
				return true;
			}
		});

		MarketRegistry.INSTANCE.registerDefaultHandler("Bonemeal", new MarketRegistryDefaultHandler() {
			@Override
			public void apply(MarketRegistry registry) {
				registry.registerEntry(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()), new ItemStack(Items.EMERALD), MarketEntry.EntryType.OTHER);
			}

			@Override
			public boolean isEnabledByDefault() {
				return true;
			}
		});

		MarketRegistry.INSTANCE.registerDefaultHandler("Animal Eggs", new MarketRegistryDefaultHandler() {
			@Override
			public void apply(MarketRegistry registry) {
				for (String animalName : ANIMALS) {
					ItemStack eggStack = new ItemStack(Items.SPAWN_EGG);
					ItemMonsterPlacer.applyEntityIdToItemStack(eggStack, animalName);
					registry.registerEntry(eggStack, new ItemStack(Items.EMERALD), MarketEntry.EntryType.OTHER);
				}
			}

			@Override
			public boolean isEnabledByDefault() {
				return false;
			}
		});
	}

}
