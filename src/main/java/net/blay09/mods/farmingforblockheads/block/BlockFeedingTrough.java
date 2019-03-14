package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.tile.TileFeedingTrough;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFeedingTrough extends BlockContainer {

    private static final VoxelShape BOUNDING_BOX = Block.makeCuboidShape(0, 0, 0, 1, 0.625, 1);

    public static final String name = "feeding_trough";
    public static final ResourceLocation registryName = new ResourceLocation(FarmingForBlockheads.MOD_ID, name);

    protected BlockFeedingTrough() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(2f).sound(SoundType.WOOD));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TileFeedingTrough();
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem(hand);
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null) {
                LazyOptional<IItemHandler> itemHandlerCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                itemHandlerCap.ifPresent(itemHandler -> {
                    if (player.isSneaking()) {
                        if (heldItem.isEmpty()) {
                            player.setHeldItem(hand, itemHandler.extractItem(0, 64, false));
                        } else {
                            ItemStack restStack = itemHandler.extractItem(0, 64, false);
                            if (!player.inventory.addItemStackToInventory(restStack)) {
                                ItemHandlerHelper.insertItem(itemHandler, restStack, false);
                            }
                        }
                    } else {
                        ItemStack restStack = ItemHandlerHelper.insertItem(itemHandler, heldItem, false);
                        player.setHeldItem(hand, restStack);
                    }
                });
            }
        }
        return true;
    }

    @Override
    public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean isMoving) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null) {
                LazyOptional<IItemHandler> itemHandlerCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                itemHandlerCap.ifPresent(itemHandler -> {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack itemStack = itemHandler.getStackInSlot(i);
                        if (!itemStack.isEmpty()) {
                            world.spawnEntity(new EntityItem(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, itemStack.copy()));
                        }
                    }
                });
            }
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new TextComponentTranslation("tooltip.farmingforblockheads:feeding_trough"));
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
        return BOUNDING_BOX;
    }

}
