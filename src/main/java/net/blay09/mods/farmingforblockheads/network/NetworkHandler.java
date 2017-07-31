package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetworkHandler {
	public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(FarmingForBlockheads.MOD_ID);

	public static void init() {
		instance.registerMessage(HandlerMarketList.class, MessageMarketList.class, 0, Side.CLIENT);
		instance.registerMessage(HandlerMarketSelect.class, MessageMarketSelect.class, 1, Side.SERVER);
		instance.registerMessage(HandlerChickenNestEffect.class, MessageChickenNestEffect.class, 2, Side.CLIENT);
		instance.registerMessage(HandlerSyncMarketCategories.class, MessageSyncMarketCategories.class, 3, Side.CLIENT);
	}

	public static IThreadListener getThreadListener(MessageContext ctx) {
		return ctx.side == Side.SERVER ? (WorldServer) ctx.getServerHandler().player.world : getClientThreadListener();
	}

	@SideOnly(Side.CLIENT)
	public static IThreadListener getClientThreadListener() {
		return Minecraft.getMinecraft();
	}

}
