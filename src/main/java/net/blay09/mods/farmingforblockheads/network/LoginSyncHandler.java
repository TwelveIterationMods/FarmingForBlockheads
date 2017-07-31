package net.blay09.mods.farmingforblockheads.network;

import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class LoginSyncHandler {

	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		NetworkHandler.instance.sendTo(new MessageSyncMarketCategories(MarketRegistry.getCategories()), (EntityPlayerMP) event.player);
	}

}
