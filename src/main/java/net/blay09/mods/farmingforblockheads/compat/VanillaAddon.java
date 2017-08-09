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
import net.minecraft.nbt.NBTTagCompound;

public class VanillaAddon {

	private static final String[] ANIMALS = new String[] {
			"Pig", "Sheep", "Cow", "Chicken", "EntityHorse", "Ozelot", "Rabbit", "PolarBear", "Wolf"
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
				for (String animalName : ANIMALS) {
					ItemStack eggStack = new ItemStack(Items.SPAWN_EGG);

					// \o/ Praise SideOnly \o/
					NBTTagCompound tagCompound = eggStack.getTagCompound();
					if(tagCompound == null) {
						tagCompound = new NBTTagCompound();
					}
					NBTTagCompound entityTag = new NBTTagCompound();
					entityTag.setString("id", animalName);
					tagCompound.setTag("EntityTag", entityTag);
					eggStack.setTagCompound(tagCompound);

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
