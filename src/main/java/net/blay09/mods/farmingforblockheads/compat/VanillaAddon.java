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
import net.minecraft.util.ResourceLocation;

public class VanillaAddon {

	private static final ResourceLocation[] ANIMALS = new ResourceLocation[] { // TODO add missing animals and test
			new ResourceLocation("minecraft", "pig"),
			new ResourceLocation("minecraft", "sheep"),
			new ResourceLocation("minecraft", "cow"),
			new ResourceLocation("minecraft", "chicken"),
			new ResourceLocation("minecraft", "horse"),
			new ResourceLocation("minecraft", "ozelot"),
			new ResourceLocation("minecraft", "rabbit"),
			new ResourceLocation("minecraft", "polarbear"),
			new ResourceLocation("minecraft", "wolf"),
	};

	public VanillaAddon() {
		MarketRegistry.registerDefaultHandler("Vanilla Seeds", new MarketRegistryDefaultHandler() {
			@Override
			public void apply(MarketRegistry registry, ItemStack defaultPayment) {
				registry.registerEntry(new ItemStack(Items.WHEAT_SEEDS), defaultPayment, MarketEntry.EntryType.SEEDS);
				registry.registerEntry(new ItemStack(Items.MELON_SEEDS), defaultPayment, MarketEntry.EntryType.SEEDS);
				registry.registerEntry(new ItemStack(Items.PUMPKIN_SEEDS), defaultPayment, MarketEntry.EntryType.SEEDS);
				registry.registerEntry(new ItemStack(Items.BEETROOT_SEEDS), defaultPayment, MarketEntry.EntryType.SEEDS);
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

		MarketRegistry.registerDefaultHandler("Vanilla Saplings", new MarketRegistryDefaultHandler() {
			@Override
			public void apply(MarketRegistry registry, ItemStack defaultPayment) {
				for (BlockPlanks.EnumType type : BlockSapling.TYPE.getAllowedValues()) {
					registry.registerEntry(new ItemStack(Blocks.SAPLING, 1, type.getMetadata()), defaultPayment, MarketEntry.EntryType.SAPLINGS);
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

		MarketRegistry.registerDefaultHandler("Bonemeal", new MarketRegistryDefaultHandler() {
			@Override
			public void apply(MarketRegistry registry, ItemStack defaultPayment) {
				registry.registerEntry(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()), defaultPayment, MarketEntry.EntryType.OTHER);
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

		MarketRegistry.registerDefaultHandler("Animal Eggs", new MarketRegistryDefaultHandler() {
			@Override
			public void apply(MarketRegistry registry, ItemStack defaultPayment) {
				for (ResourceLocation animalName : ANIMALS) {
					ItemStack eggStack = new ItemStack(Items.SPAWN_EGG);
					ItemMonsterPlacer.applyEntityIdToItemStack(eggStack, animalName);
					registry.registerEntry(eggStack, defaultPayment, MarketEntry.EntryType.OTHER);
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
