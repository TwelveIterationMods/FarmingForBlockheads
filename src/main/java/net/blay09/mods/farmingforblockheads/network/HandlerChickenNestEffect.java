package net.blay09.mods.farmingforblockheads.network;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class HandlerChickenNestEffect implements IMessageHandler<MessageChickenNestEffect, IMessage> {
	@Override
	@Nullable
	public IMessage onMessage(MessageChickenNestEffect message, MessageContext ctx) {
		NetworkHandler.getThreadListener(ctx).addScheduledTask(() -> Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, message.getPos().getX() + 0.5f, message.getPos().getY() + 0.5f, message.getPos().getZ() + 0.5f, 0, 0, 0));
		return null;
	}
}
