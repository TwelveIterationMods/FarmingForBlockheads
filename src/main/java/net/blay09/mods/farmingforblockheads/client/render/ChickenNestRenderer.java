package net.blay09.mods.farmingforblockheads.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.farmingforblockheads.block.ChickenNestBlock;
import net.blay09.mods.farmingforblockheads.tile.ChickenNestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;

public class ChickenNestRenderer extends TileEntityRenderer<ChickenNestTileEntity> {

    private final ItemStack EGG_STACK = new ItemStack(Items.EGG);
    private final float[] EGG_POSITIONS = new float[]{
            0.2f, 0, 0,
            -0.2f, 0, 0,
            0, 0, -0.1f,
            0, 0, 0.1f,
    };

    public ChickenNestRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    private static float getFacingAngle(Direction facing) {
        float angle;
        switch (facing) {
            case NORTH:
                angle = 0;
                break;
            case SOUTH:
                angle = 180;
                break;
            case WEST:
                angle = 90;
                break;
            case EAST:
            default:
                angle = -90;
                break;
        }
        return angle;
    }

    @Override
    public void render(ChickenNestTileEntity tileEntity, float p_225616_2_, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225616_5_, int p_225616_6_) {
        matrixStack.translate(0.5, 0, 0.5);

        BlockState state = tileEntity.getBlockState();
        float angle = 0f;
        if (state.hasProperty(ChickenNestBlock.FACING)) {
            angle = getFacingAngle(state.get(ChickenNestBlock.FACING));
        }

        matrixStack.rotate(new Quaternion(0f, angle, 0f, true));
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        for (int i = 0; i < Math.min(EGG_POSITIONS.length / 3, tileEntity.getEggCount()); i++) {
            matrixStack.push();
            matrixStack.translate(EGG_POSITIONS[i * 3], 0.1f + EGG_POSITIONS[i * 3 + 1], -0.1f + EGG_POSITIONS[i * 3 + 2]);
            matrixStack.rotate(new Quaternion(45f, 0, 0, true));
            itemRenderer.renderItem(EGG_STACK, ItemCameraTransforms.TransformType.GROUND, p_225616_5_, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);
            matrixStack.pop();
        }
    }
}
