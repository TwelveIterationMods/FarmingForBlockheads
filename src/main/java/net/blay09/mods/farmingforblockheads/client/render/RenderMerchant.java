package net.blay09.mods.farmingforblockheads.client.render;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.entity.EntityMerchant;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.model.ModelVillager;
import net.minecraft.util.ResourceLocation;

public class RenderMerchant extends RenderLiving<EntityMerchant> {

	private static final ResourceLocation MERCHANT_TEXTURE = new ResourceLocation(FarmingForBlockheads.MOD_ID, "textures/entity/merchant.png");

	public RenderMerchant(RenderManager renderManager) {
		super(renderManager, new ModelVillager(0f), 0.5f);
		addLayer(new LayerCustomHead(this.getMainModel().func_205072_a())); // getVillagerHead
		shadowSize = 0.5f;
	}

	@Override
	public ModelVillager getMainModel() {
		return (ModelVillager) super.getMainModel();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMerchant entity) {
		return MERCHANT_TEXTURE;
	}

	@Override
	protected void preRenderCallback(EntityMerchant merchant, float partialTickTime) {
		float scale = 0.9375f;
		GlStateManager.scalef(scale, scale, scale);
		int diggingAnimation = merchant.getDiggingAnimation();
		if(diggingAnimation > 0) {
			GlStateManager.translated(0,diggingAnimation * 0.05, 0);
		}
	}

	@Override
	protected boolean canRenderName(EntityMerchant entity) {
		return entity.getDiggingAnimation() <= 0 && super.canRenderName(entity);
	}
}
