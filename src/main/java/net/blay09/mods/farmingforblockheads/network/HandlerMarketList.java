package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.container.ContainerMarketClient;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class HandlerMarketList implements IMessageHandler<MessageMarketList, IMessage> {

	@Override
	@Nullable
	public IMessage onMessage(final MessageMarketList message, MessageContext ctx) {
		NetworkHandler.getThreadListener(ctx).addScheduledTask(new Runnable() {
			@Override
			public void run() {
				Container container = FMLClientHandler.instance().getClientPlayerEntity().openContainer;
				if(container instanceof ContainerMarketClient) {
					((ContainerMarketClient) container).setEntryList(message.getEntryList());
				}
			}
		});
		return null;
	}

}
