package net.blay09.mods.farmingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.farmingforblockheads.tile.FeedingTroughTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

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
    public void func_225616_a_(FeedingTroughTileEntity tileEntity, float p_225616_2_, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225616_5_, int p_225616_6_) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        float x = 0;
        float y = 0;
        float z = 0;
        ItemStack content = tileEntity.getContentStack();
        if (!content.isEmpty()) {
            for (int i = 0; i < Math.max(1, Math.min(CONTENT_POSITIONS.length / 3, content.getCount() / 12)); i++) {
                matrixStack.func_227860_a_();
                matrixStack.func_227861_a_(x + 0.5f + CONTENT_POSITIONS[i * 3], y + 0.5f + CONTENT_POSITIONS[i * 3 + 1], z + 0.4f + CONTENT_POSITIONS[i * 3 + 2]);
                matrixStack.func_227863_a_(new Quaternion(90f, 0, 0, true));
                itemRenderer.func_229110_a_(content, ItemCameraTransforms.TransformType.GROUND, p_225616_5_,  OverlayTexture.field_229196_a_, matrixStack, renderTypeBuffer);
                matrixStack.func_227865_b_();
            }
        }
    }
}
