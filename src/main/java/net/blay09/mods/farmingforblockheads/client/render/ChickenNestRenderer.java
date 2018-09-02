package net.blay09.mods.farmingforblockheads.client.render;

import net.blay09.mods.farmingforblockheads.block.BlockChickenNest;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.tile.TileChickenNest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ChickenNestRenderer extends TileEntitySpecialRenderer<TileChickenNest> {

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
    public void render(TileChickenNest tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5f, y, z + 0.5f);

        IBlockState state = tileEntity.hasWorld() ? tileEntity.getWorld().getBlockState(tileEntity.getPos()) : null;
        float angle = 0f;
        if (state != null && state.getBlock() == ModBlocks.chickenNest) {
            angle = getFacingAngle(state.getValue(BlockChickenNest.FACING));
        }

        GlStateManager.rotate(angle, 0f, 1f, 0f);
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        for (int i = 0; i < Math.min(EGG_POSITIONS.length / 3, tileEntity.getEggCount()); i++) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(EGG_POSITIONS[i * 3], 0.1f + EGG_POSITIONS[i * 3 + 1], -0.1f + EGG_POSITIONS[i * 3 + 2]);
            GlStateManager.rotate(45f, 1, 0, 0);
            renderItem.renderItem(EGG_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
    }
}
