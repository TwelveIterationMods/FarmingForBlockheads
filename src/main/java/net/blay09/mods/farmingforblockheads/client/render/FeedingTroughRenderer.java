package net.blay09.mods.farmingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.farmingforblockheads.tile.FeedingTroughTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;

public class FeedingTroughRenderer extends TileEntityRenderer<FeedingTroughTileEntity> {

    private final float[] CONTENT_POSITIONS = new float[]{
            0.15f, 0.01f, 0,
            -0.2f, 0, 0,
            0, -0.01f, -0.2f,
            0, -0.02f, 0.15f,
    };

    public FeedingTroughRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(FeedingTroughTileEntity tileEntity, float p_225616_2_, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225616_5_, int p_225616_6_) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        float x = 0;
        float y = 0;
        float z = 0;
        ItemStack content = tileEntity.getContentStack();
        if (!content.isEmpty()) {
            for (int i = 0; i < Math.max(1, Math.min(CONTENT_POSITIONS.length / 3, content.getCount() / 12)); i++) {
                matrixStack.push();
                matrixStack.translate(x + 0.5f + CONTENT_POSITIONS[i * 3], y + 0.5f + CONTENT_POSITIONS[i * 3 + 1], z + 0.4f + CONTENT_POSITIONS[i * 3 + 2]);
                matrixStack.rotate(new Quaternion(90f, 0, 0, true));
                itemRenderer.renderItem(content, ItemCameraTransforms.TransformType.GROUND, p_225616_5_,  OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);
                matrixStack.pop();
            }
        }
    }
}
