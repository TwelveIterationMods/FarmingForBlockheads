package net.blay09.mods.farmingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.entity.MerchantEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
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
    public ResourceLocation getEntityTexture(MerchantEntity entity) {
        return MERCHANT_TEXTURE;
    }

    @Override
    public void render(MerchantEntity entity, float p_225623_2_, float p_225623_3_, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225623_6_) {
        int diggingAnimation = entity.getDiggingAnimation();
        if (diggingAnimation > 0) {
            matrixStack.translate(0.0, -diggingAnimation * 0.05, 0.0);
        }
        super.render(entity, p_225623_2_, p_225623_3_, matrixStack, renderTypeBuffer, p_225623_6_);
    }

    @Override
    protected boolean canRenderName(MerchantEntity entity) {
        return entity.getDiggingAnimation() <= 0 && super.canRenderName(entity);
    }
}
