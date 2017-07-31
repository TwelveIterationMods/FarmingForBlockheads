package net.blay09.mods.farmingforblockheads.network;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageSyncMarketCategories implements IMessage {

	private List<ResourceLocation> categories;

	public MessageSyncMarketCategories() {
	}

	public MessageSyncMarketCategories(List<IMarketCategory> categories) {
		this.categories = categories.stream().map(IMarketCategory::getRegistryName).collect(Collectors.toList());
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		categories = new ArrayList<>();
		int count = buf.readByte();
		for(int i = 0; i < count; i++) {
			categories.add(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(categories.size());
		for(ResourceLocation id : categories) {
			ByteBufUtils.writeUTF8String(buf, id.toString());
		}
	}

	public List<ResourceLocation> getCategories() {
		return categories;
	}
}
