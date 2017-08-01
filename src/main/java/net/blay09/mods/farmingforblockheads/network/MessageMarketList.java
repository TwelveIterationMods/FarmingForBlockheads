package net.blay09.mods.farmingforblockheads.network;

import com.google.common.collect.ArrayListMultimap;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.List;

public class MessageMarketList implements IMessage {

	private ArrayListMultimap<IMarketCategory, IMarketEntry> entryMap;

	public MessageMarketList() {}

	public MessageMarketList(ArrayListMultimap<IMarketCategory, IMarketEntry> entryMap) {
		this.entryMap = entryMap;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entryMap = ArrayListMultimap.create();
		int categoryCount = buf.readByte();
		for(int i = 0; i < categoryCount; i++) {
			ResourceLocation categoryId = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
			IMarketCategory category = FarmingForBlockheadsAPI.getMarketCategory(categoryId);
			if(category == null) {
				category = FarmingForBlockheadsAPI.getMarketCategoryOther();
			}
			int entryCount = buf.readShort();
			for(int j = 0; j < entryCount; j++) {
				entryMap.put(category, readEntry(buf, category));
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(entryMap.keySet().size());
		for(IMarketCategory category : entryMap.keySet()) {
			ByteBufUtils.writeUTF8String(buf, category.getRegistryName().toString());
			List<IMarketEntry> entries = entryMap.get(category);
			buf.writeShort(entries.size());
			for(IMarketEntry entry : entries) {
				writeEntry(entry, buf);
			}
		}
	}

	public ArrayListMultimap<IMarketCategory, IMarketEntry> getEntryMap() {
		return entryMap;
	}

	private MarketEntry readEntry(ByteBuf buf, IMarketCategory category) {
		ItemStack outputItem = ByteBufUtils.readItemStack(buf);
		ItemStack costItem = ByteBufUtils.readItemStack(buf);
		return new MarketEntry(outputItem, costItem, category);
	}

	private void writeEntry(IMarketEntry entry, ByteBuf buf) {
		ByteBufUtils.writeItemStack(buf, entry.getOutputItem());
		ByteBufUtils.writeItemStack(buf, entry.getCostItem());
	}

}
