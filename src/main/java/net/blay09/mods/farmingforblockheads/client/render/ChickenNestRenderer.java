package net.blay09.mods.farmingforblockheads.client.render;

import net.blay09.mods.farmingforblockheads.block.BlockChickenNest;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.tile.TileChickenNest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class ChickenNestRenderer extends TileEntityRenderer<TileChickenNest> {

    private final ItemStack EGG_STACK = new ItemStack(Items.EGG);
    private final float[] EGG_POSITIONS = new float[]{
            0.2f, 0, 0,
            -0.2f, 0, 0,
            0, 0, -0.1f,
            0, 0, 0.1f,
    };

    private static float getFacingAngle(EnumFacing facing) {
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
    public void render(TileChickenNest tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x + 0.5, y, z + 0.5);

        IBlockState state = tileEntity.hasWorld() ? tileEntity.getWorld().getBlockState(tileEntity.getPos()) : null;
        float angle = 0f;
        if (state != null && state.getBlock() == ModBlocks.chickenNest) {
            angle = getFacingAngle(state.get(BlockChickenNest.FACING));
        }

        GlStateManager.rotatef(angle, 0f, 1f, 0f);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        for (int i = 0; i < Math.min(EGG_POSITIONS.length / 3, tileEntity.getEggCount()); i++) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(EGG_POSITIONS[i * 3], 0.1f + EGG_POSITIONS[i * 3 + 1], -0.1f + EGG_POSITIONS[i * 3 + 2]);
            GlStateManager.rotatef(45f, 1, 0, 0);
            itemRenderer.renderItem(EGG_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
    }
}
