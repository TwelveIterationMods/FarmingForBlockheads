package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.container.ContainerMarket;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class HandlerMarketSelect implements IMessageHandler<MessageMarketSelect, IMessage> {
    @Override
    @Nullable
    public IMessage onMessage(final MessageMarketSelect message, final MessageContext ctx) {
        NetworkHandler.getThreadListener(ctx).addScheduledTask(() -> {
            Container container = ctx.getServerHandler().player.openContainer;
            if (container instanceof ContainerMarket) {
                ((ContainerMarket) container).selectMarketEntry(message.getOutputItem());
            }
        });
        return null;
    }
}
