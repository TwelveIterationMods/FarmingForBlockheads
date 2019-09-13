package net.blay09.mods.farmingforblockheads.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.farmingforblockheads.tile.FeedingTroughTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;

public class FeedingTroughRenderer extends TileEntityRenderer<FeedingTroughTileEntity> {

    private final float[] CONTENT_POSITIONS = new float[]{
            0.15f, 0.01f, 0,
            -0.2f, 0, 0,
            0, -0.01f, -0.2f,
            0, -0.02f, 0.15f,
    };

    @Override
    public void render(FeedingTroughTileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        ItemStack content = tileEntity.getContentStack();
        if (!content.isEmpty()) {
            for (int i = 0; i < Math.max(1, Math.min(CONTENT_POSITIONS.length / 3, content.getCount() / 12)); i++) {
                GlStateManager.pushMatrix();
                GlStateManager.translated(x + 0.5f + CONTENT_POSITIONS[i * 3], y + 0.5f + CONTENT_POSITIONS[i * 3 + 1], z + 0.4f + CONTENT_POSITIONS[i * 3 + 2]);
                GlStateManager.rotatef(90f, 1, 0, 0);
                itemRenderer.renderItem(content, ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.popMatrix();
            }
        }
    }
}
