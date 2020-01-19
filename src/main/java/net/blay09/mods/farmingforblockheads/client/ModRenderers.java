package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.farmingforblockheads.client.render.ChickenNestRenderer;
import net.blay09.mods.farmingforblockheads.client.render.FeedingTroughRenderer;
import net.blay09.mods.farmingforblockheads.client.render.RenderMerchant;
import net.blay09.mods.farmingforblockheads.entity.MerchantEntity;
import net.blay09.mods.farmingforblockheads.entity.ModEntities;
import net.blay09.mods.farmingforblockheads.tile.ModTileEntities;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ModRenderers {
    public static void register() {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.merchant, RenderMerchant::new);

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.chickenNest, ChickenNestRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.feedingTrough, FeedingTroughRenderer::new);
    }
}
