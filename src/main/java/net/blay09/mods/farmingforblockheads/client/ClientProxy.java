package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.farmingforblockheads.CommonProxy;
import net.blay09.mods.farmingforblockheads.client.render.ChickenNestRenderer;
import net.blay09.mods.farmingforblockheads.client.render.RenderMerchant;
import net.blay09.mods.farmingforblockheads.entity.EntityMerchant;
import net.blay09.mods.farmingforblockheads.tile.TileChickenNest;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerModels() {
		RenderingRegistry.registerEntityRenderingHandler(EntityMerchant.class, RenderMerchant::new);

		ClientRegistry.bindTileEntitySpecialRenderer(TileChickenNest.class, new ChickenNestRenderer());
	}

}
