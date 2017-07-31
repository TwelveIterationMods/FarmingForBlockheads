package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.registry.MarketCategory;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;
import java.util.List;

public class HandlerSyncMarketCategories implements IMessageHandler<MessageSyncMarketCategories, IMessage> {
	@Override
	@Nullable
	public IMessage onMessage(MessageSyncMarketCategories message, MessageContext ctx) {
		NetworkHandler.getThreadListener(ctx).addScheduledTask(() -> {
			List<ResourceLocation> categories = message.getCategories();
			for(int i = 0; i < categories.size(); i++) {
				((MarketCategory) MarketRegistry.getCategory(categories.get(i))).setRuntimeId(i);
			}
		});
		return null;
	}
}
