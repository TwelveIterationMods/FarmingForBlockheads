package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.farmingforblockheads.CommonProxy;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.client.render.RenderMerchant;
import net.blay09.mods.farmingforblockheads.entity.EntityMerchant;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityMerchant.class, new IRenderFactory<EntityMerchant>() {
			@Override
			public Render<? super EntityMerchant> createRenderFor(RenderManager manager) {
				return new RenderMerchant(manager);
			}
		});

		//noinspection ConstantConditions
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.market), 0, new ModelResourceLocation(ModBlocks.market.getRegistryName(), "inventory"));
	}

}
