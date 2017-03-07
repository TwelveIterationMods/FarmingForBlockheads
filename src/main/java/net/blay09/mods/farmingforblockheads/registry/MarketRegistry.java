package net.blay09.mods.farmingforblockheads.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarketRegistry extends AbstractRegistry {

	public static final MarketRegistry INSTANCE = new MarketRegistry();

	private static final Pattern ITEMSTACK_PATTERN = Pattern.compile("(?:([0-9]+)\\*)?(?:([\\w]+):)([\\w]+)(?::([0-9]+))?(?:@(.+))?");

	private final List<MarketEntry> entries = Lists.newArrayList();

	private final Map<String, ItemStack> defaultPayments = Maps.newHashMap();
	private final Map<String, MarketRegistryDefaultHandler> defaultHandlers = Maps.newHashMap();

	public MarketRegistry() {
		super("Market");
	}

	public void registerEntry(ItemStack outputItem, ItemStack costItem, MarketEntry.EntryType type) {
		entries.add(new MarketEntry(outputItem, costItem, type));
	}

	@Nullable
	public static MarketEntry getEntryFor(ItemStack outputItem) {
		for(MarketEntry entry : INSTANCE.entries) {
			if(entry.getOutputItem().isItemEqual(outputItem) && ItemStack.areItemStackTagsEqual(entry.getOutputItem(), outputItem)) {
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

		JsonObject payment = new JsonObject();
		payment.addProperty("__comment", "You can define custom payment items for the default entries here.");
		payment.addProperty("Example: Vanilla Seeds", "minecraft:diamond");
		root.add("defaults payment", payment);

		JsonArray blacklist = new JsonArray();
		blacklist.add(new JsonPrimitive("examplemod:example_item"));
		root.add("defaults blacklist", blacklist);

		JsonObject custom = new JsonObject();
		custom.addProperty("__comment", "You can define additional items to be sold by the Market here. Defaults can be overridden. Prefix with ! to blacklist instead.");
		custom.addProperty("examplemod:example_item", "2*minecraft:emerald");
		root.add("custom entries", custom);

		return root;
	}

	@Override
	protected void load(JsonObject root) {
		JsonObject payments = tryGetObject(root, "defaults payment");
		loadDefaultPayments(payments);

		JsonObject defaults = tryGetObject(root, "defaults");
		registerDefaults(defaults);

		JsonArray blacklist = tryGetArray(root, "defaults blacklist");
		for(int i = 0; i < blacklist.size(); i++) {
			JsonElement element = blacklist.get(i);
			if(element.isJsonPrimitive()) {
				loadDefaultBlacklistEntry(element.getAsString());
			} else {
				logError("Failed to load %s registry: blacklist entries must be strings", registryName);
			}
		}

		JsonObject custom = tryGetObject(root, "custom entries");
		for(Map.Entry<String, JsonElement> entry : custom.entrySet()) {
			if(entry.getValue().isJsonPrimitive()) {
				loadMarketEntry(entry.getKey(), entry.getValue().getAsString());
			} else {
				logError("Failed to load %s registry: entries must be strings", registryName);
			}
		}
	}

	@Override
	protected boolean hasCustomLoader() {
		return true;
	}

	private void loadMarketEntry(String key, String value) {
		if(key.equals("__comment") || key.equals("examplemod:example_item")) {
			return;
		}

		ItemStack outputStack = parseItemStack(key);
		ItemStack costStack = parseItemStack(key);
		if(outputStack == null || costStack == null) {
			return;
		}

		tryRemoveEntry(outputStack);

		MarketEntry.EntryType type = MarketEntry.EntryType.OTHER;
		if(outputStack.getItem().getRegistryName().getResourcePath().contains("sapling")) {
			type = MarketEntry.EntryType.SAPLINGS;
		} else if(outputStack.getItem().getRegistryName().getResourcePath().contains("seed")) {
			type = MarketEntry.EntryType.SEEDS;
		}

		registerEntry(outputStack, costStack, type);
	}

	private void loadDefaultBlacklistEntry(String input) {
		if(input.equals("examplemod:example_item")) {
			return;
		}
		ItemStack itemStack = parseItemStack(input);
		if(itemStack != null) {
			if(!tryRemoveEntry(itemStack)) {
				logError("Could not find default entry for blacklisted item %s", input);
			}
		}
	}

	private void loadDefaultPayments(JsonObject entry) {
		for(String key : defaultHandlers.keySet()) {
			String value = tryGetString(entry, key, "");
			if(!value.isEmpty()) {
				ItemStack itemStack = parseItemStack(value);
				if(itemStack != null) {
					defaultPayments.put(key, itemStack);
				}
			}
		}
	}

	@Override
	protected void registerDefaults(JsonObject json) {
		for(Map.Entry<String, MarketRegistryDefaultHandler> entry : defaultHandlers.entrySet()) {
			if(tryGetBoolean(json, entry.getKey(), entry.getValue().isEnabledByDefault())) {
				entry.getValue().apply(this);
			}
		}
	}

	public static void registerDefaultHandler(String defaultKey, MarketRegistryDefaultHandler handler) {
		INSTANCE.defaultHandlers.put(defaultKey, handler);
	}

	public ItemStack getPaymentStackForDefault(String defaultKey, ItemStack defaultPayment) {
		ItemStack itemStack = INSTANCE.defaultPayments.get(defaultKey);
		if(itemStack == null) {
			return defaultPayment;
		}
		return itemStack;
	}

	private boolean tryRemoveEntry(ItemStack itemStack) {
		for(int i = entries.size(); i >= 0; i--) {
			MarketEntry entry = entries.get(i);
			if(entry.getOutputItem().isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(entry.getOutputItem(), itemStack)) {
				entries.remove(i);
				return true;
			}
		}
		return false;
	}

	@Nullable
	private ItemStack parseItemStack(String input) {
		Matcher matcher = ITEMSTACK_PATTERN.matcher(input);
		if(!matcher.find()) {
			logError("Invalid item %s, format mismatch", input);
			return null;
		}

		ResourceLocation resourceLocation = new ResourceLocation(matcher.group(2), matcher.group(3));
		Item item = Item.REGISTRY.getObject(resourceLocation);
		if(item == null) {
			logUnknownItem(resourceLocation);
			return null;
		}
		int count = matcher.group(1) != null ? tryParseInt(matcher.group(1)) : 1;
		int meta = matcher.group(4) != null ? tryParseInt(matcher.group(4)) : 0;
		String nbt = matcher.group(5);
		NBTTagCompound tagCompound = null;
		if(nbt != null) {
			try {
				tagCompound = JsonToNBT.getTagFromJson(nbt);
			} catch (NBTException e) {
				logError("Invalid nbt for item %s, %s", input, e.getMessage());
			}
		}
		ItemStack itemStack = new ItemStack(item, count, meta);
		if(tagCompound != null) {
			itemStack.setTagCompound(tagCompound);
		}
		return itemStack;
	}

}
