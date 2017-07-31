package net.blay09.mods.farmingforblockheads.network;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.registry.MarketCategory;
import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.Collection;

public class MessageMarketList implements IMessage {

	private Collection<IMarketEntry> entryList;

	public MessageMarketList() {}

	public MessageMarketList(Collection<IMarketEntry> entryList) {
		this.entryList = entryList;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int entryCount = buf.readInt();
		entryList = Lists.newArrayListWithCapacity(entryCount);
		for(int i = 0; i < entryCount; i++) {
			entryList.add(readEntry(buf));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		int count = entryList.size();
		buf.writeInt(count);
		for (IMarketEntry recipe : entryList) {
			writeEntry(recipe, buf);
		}
	}

	public Collection<IMarketEntry> getEntryList() {
		return entryList;
	}

	private MarketEntry readEntry(ByteBuf buf) {
		ItemStack outputItem = ByteBufUtils.readItemStack(buf);
		ItemStack costItem = ByteBufUtils.readItemStack(buf);
		IMarketCategory category = MarketRegistry.getCategories().get(buf.readByte());
		return new MarketEntry(outputItem, costItem, category);
	}

	private void writeEntry(IMarketEntry entry, ByteBuf buf) {
		ByteBufUtils.writeItemStack(buf, entry.getOutputItem());
		ByteBufUtils.writeItemStack(buf, entry.getCostItem());
		buf.writeByte(((MarketCategory) entry.getCategory()).getRuntimeId());
	}

}
