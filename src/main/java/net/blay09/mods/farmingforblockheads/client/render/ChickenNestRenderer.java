package net.blay09.mods.farmingforblockheads.client.render;

import net.blay09.mods.farmingforblockheads.tile.TileChickenNest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ChickenNestRenderer extends TileEntitySpecialRenderer<TileChickenNest> {

	private final ItemStack EGG_STACK = new ItemStack(Items.EGG);
	private final float[] EGG_POSITIONS = new float[] {
			0, 0, 0.1f,
			0.2f, 0, 0,
			0, 0, 0,
			0, 0, 0,
			0, 0, 0,
	};

	@Override
	public void render(TileChickenNest tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		float[] EGG_POSITIONS = new float[] {
				0.2f, 0, 0,
				-0.2f, 0, 0,
				0, 0, -0.1f,
				0, 0, 0.1f,
		};
		for(int i = 0; i < Math.min(4, tileEntity.getEggCount()); i++) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5f + EGG_POSITIONS[i * 3], y + 0.1f + EGG_POSITIONS[i * 3 + 1], z + 0.4f + EGG_POSITIONS[i * 3 + 2]);
			GlStateManager.rotate(45f, 1, 0, 0);
			renderItem.renderItem(EGG_STACK, ItemCameraTransforms.TransformType.GROUND);
			GlStateManager.popMatrix();
		}
	}
}
