package net.blay09.mods.farmingforblockheads.client;

import net.blay09.mods.farmingforblockheads.client.render.ChickenNestRenderer;
import net.blay09.mods.farmingforblockheads.client.render.FeedingTroughRenderer;
import net.blay09.mods.farmingforblockheads.client.render.RenderMerchant;
import net.blay09.mods.farmingforblockheads.entity.MerchantEntity;
import net.blay09.mods.farmingforblockheads.tile.ChickenNestTileEntity;
import net.blay09.mods.farmingforblockheads.tile.FeedingTroughTileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ModRenderers {
    public static void register() {
        RenderingRegistry.registerEntityRenderingHandler(MerchantEntity.class, RenderMerchant::new);

        ClientRegistry.bindTileEntitySpecialRenderer(ChickenNestTileEntity.class, new ChickenNestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(FeedingTroughTileEntity.class, new FeedingTroughRenderer());
    }
}
