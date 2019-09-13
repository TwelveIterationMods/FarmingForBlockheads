package net.blay09.mods.farmingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.entity.MerchantEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.util.ResourceLocation;

public class RenderMerchant extends LivingRenderer<MerchantEntity, VillagerModel<MerchantEntity>> {

    private static final ResourceLocation MERCHANT_TEXTURE = new ResourceLocation(FarmingForBlockheads.MOD_ID, "textures/entity/merchant.png");

    public RenderMerchant(EntityRendererManager rendererManager) {
        super(rendererManager, new VillagerModel<>(0f), 0.5f);
        addLayer(new HeadLayer<>(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(MerchantEntity entity) {
        return MERCHANT_TEXTURE;
    }

    @Override
    protected void preRenderCallback(MerchantEntity merchant, float partialTickTime) {
        float scale = 0.9375f;
        GlStateManager.scalef(scale, scale, scale);
        int diggingAnimation = merchant.getDiggingAnimation();
        if (diggingAnimation > 0) {
            GlStateManager.translated(0, diggingAnimation * 0.05, 0);
        }
    }

    @Override
    protected boolean canRenderName(MerchantEntity entity) {
        return entity.getDiggingAnimation() <= 0 && super.canRenderName(entity);
    }
}
