package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.farmingforblockheads.CommonProxy;
import net.blay09.mods.farmingforblockheads.client.render.RenderMerchant;
import net.blay09.mods.farmingforblockheads.entity.EntityMerchant;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityMerchant.class, RenderMerchant::new);
	}

}
