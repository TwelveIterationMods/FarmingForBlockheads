package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.tile.TileChickenNest;
import net.blay09.mods.farmingforblockheads.tile.TileFeedingTrough;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
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
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFeedingTrough extends BlockContainer {
	public static final String name = "feeding_trough";
	public static final ResourceLocation registryName = new ResourceLocation(FarmingForBlockheads.MOD_ID, name);

	protected BlockFeedingTrough() {
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setHardness(2f);
		setUnlocalizedName(registryName.toString());
		setCreativeTab(FarmingForBlockheads.creativeTab);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileFeedingTrough();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			ItemStack heldItem = player.getHeldItem(hand);
			TileEntity tileEntity = world.getTileEntity(pos);
			if(tileEntity != null) {
				IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				if (itemHandler != null) {
					heldItem = ItemHandlerHelper.insertItem(itemHandler, heldItem, false);
				}
			}
			player.setHeldItem(hand, heldItem);
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if(!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity != null) {
				IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				if (itemHandler != null) {
					for (int i = 0; i < itemHandler.getSlots(); i++) {
						ItemStack itemStack = itemHandler.getStackInSlot(i);
						if (!itemStack.isEmpty()) {
							world.spawnEntity(new EntityItem(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, itemStack.copy()));
						}
					}
				}
			}
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format("tooltip.farmingforblockheads:feeding_trough"));
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
}
