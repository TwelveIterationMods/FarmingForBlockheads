package net.blay09.mods.farmingforblockheads.registry;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.blay09.mods.farmingforblockheads.compat.HarvestcraftMarket;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarketRegistry extends AbstractRegistry {

	public static final MarketRegistry INSTANCE = new MarketRegistry();

	private static final Pattern ITEMSTACK_PATTERN = Pattern.compile("(?:([0-9]+)\\*)?(?:([\\w]+):)([\\w]+)(?::([0-9]+))?");

	private final List<MarketEntry> entries = Lists.newArrayList();

	public MarketRegistry() {
		super("Market");
	}

	public void registerEntry(ItemStack outputItem, ItemStack costItem, MarketEntry.EntryType type) {
		entries.add(new MarketEntry(outputItem, costItem, type));
	}

	@Nullable
	public static MarketEntry getEntryFor(ItemStack outputItem) {
		for(MarketEntry entry : INSTANCE.entries) {
			if(entry.getOutputItem().isItemEqual(outputItem)) {
				return entry;
			}
		}
		return null;
	}

	public static Collection<MarketEntry> getEntries() {
		return INSTANCE.entries;
	}

	@Override
	protected void clear() {
		entries.clear();
	}

	@Override
	protected JsonObject create() {
		JsonObject root = new JsonObject();

		JsonObject defaults = new JsonObject();
		defaults.addProperty("__comment", "You can disable defaults by setting these to false. Do *NOT* try to add additional entries here. You can add additional entries in the custom section.");
		root.add("defaults", defaults);

		JsonObject custom = new JsonObject();
		custom.addProperty("__comment", "You can define additional items to be sold by the Market here.");
		custom.addProperty("examplemod:example_item", "2*minecraft:emerald");
		root.add("custom", custom);

		return root;
	}

	@Override
	protected void loadCustom(String key, String value) {
		if(key.equals("__comment") || key.equals("examplemod:example_item")) {
			return;
		}

		Matcher matcherKey = ITEMSTACK_PATTERN.matcher(key);
		Matcher matcherValue = ITEMSTACK_PATTERN.matcher(value);
		if(!matcherKey.find()) {
			logError("Invalid Market entry %s, format mismatch in output", key);
			return;
		}

		if(!matcherValue.find()) {
			logError("Invalid Market entry %s, format mismatch in cost", value);
			return;
		}

		ResourceLocation outputLocation = new ResourceLocation(matcherKey.group(2), matcherKey.group(3));
		Item outputItem = Item.REGISTRY.getObject(outputLocation);
		if(outputItem == null) {
			logUnknownItem(outputLocation);
			return;
		}
		int outputCount = matcherKey.group(1) != null ? tryParseInt(matcherKey.group(1)) : 1;
		int outputMeta = matcherKey.group(4) != null ? tryParseInt(matcherKey.group(4)) : 0;
		ItemStack outputStack = new ItemStack(outputItem, outputCount, outputMeta);

		ResourceLocation costLocation = new ResourceLocation(matcherValue.group(2), matcherValue.group(3));
		Item costItem = Item.REGISTRY.getObject(costLocation);
		if(costItem == null) {
			logUnknownItem(costLocation);
			return;
		}
		int costCount = matcherValue.group(1) != null ? tryParseInt(matcherValue.group(1)) : 1;
		int costMeta = matcherValue.group(4) != null ? tryParseInt(matcherValue.group(4)) : 0;
		ItemStack costStack = new ItemStack(costItem, costCount, costMeta);

		MarketEntry.EntryType type = MarketEntry.EntryType.OTHER;
		if(outputLocation.getResourcePath().contains("sapling")) {
			type = MarketEntry.EntryType.SAPLINGS;
		} else if(outputLocation.getResourcePath().contains("seed")) {
			type = MarketEntry.EntryType.SEEDS;
		}

		registerEntry(outputStack, costStack, type);
	}

	private static final String[] ANIMALS = new String[] {
			"minecraft:pig", "minecraft:sheep", "minecraft:cow", "minecraft:chicken", "minecraft:horse"
	};

	@Override
	protected void registerDefaults(JsonObject defaults) {
		if(tryGetBoolean(defaults, "Vanilla Seeds", true)) {
			registerEntry(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.EMERALD), MarketEntry.EntryType.SEEDS);
			registerEntry(new ItemStack(Items.MELON_SEEDS), new ItemStack(Items.EMERALD), MarketEntry.EntryType.SEEDS);
			registerEntry(new ItemStack(Items.PUMPKIN_SEEDS), new ItemStack(Items.EMERALD), MarketEntry.EntryType.SEEDS);
			registerEntry(new ItemStack(Items.BEETROOT_SEEDS), new ItemStack(Items.EMERALD), MarketEntry.EntryType.SEEDS);
		}

		if(tryGetBoolean(defaults, "Vanilla Saplings", true)) {
			for(BlockPlanks.EnumType type : BlockSapling.TYPE.getAllowedValues()) {
				registerEntry(new ItemStack(Blocks.SAPLING, 1, type.getMetadata()), new ItemStack(Items.EMERALD), MarketEntry.EntryType.SAPLINGS);
			}
		}

		if(tryGetBoolean(defaults, "Bonemeal", true)) {
			registerEntry(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()), new ItemStack(Items.EMERALD), MarketEntry.EntryType.OTHER);
		}

		if(tryGetBoolean(defaults, "Animal Eggs", false)) {
			for(String animalName : ANIMALS) {
				ItemStack eggStack = new ItemStack(Items.SPAWN_EGG);
				ItemMonsterPlacer.applyEntityIdToItemStack(eggStack, animalName);
				registerEntry(eggStack, new ItemStack(Items.EMERALD), MarketEntry.EntryType.OTHER);
			}
		}

		if(tryGetBoolean(defaults, "Pams Harvestcraft Seeds", true)) {
			HarvestcraftMarket.registerSeeds(this);
		}

		if(tryGetBoolean(defaults, "Pams Saplings", true)) {
			HarvestcraftMarket.registerSaplings(this);
		}
	}

}
