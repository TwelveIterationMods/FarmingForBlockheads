package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.ModSounds;
import net.blay09.mods.farmingforblockheads.entity.EntityMerchant;
import net.blay09.mods.farmingforblockheads.network.GuiHandler;
import net.blay09.mods.farmingforblockheads.tile.TileMarket;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMarket extends BlockContainer {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockMarket() {
		super(Material.WOOD);
		setRegistryName(FarmingForBlockheads.MOD_ID, "market");
		setSoundType(SoundType.WOOD);
		setHardness(2f);
		setUnlocalizedName(getRegistryName().toString());
		setCreativeTab(FarmingForBlockheads.creativeTab);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);
		if (facing.getAxis() == EnumFacing.Axis.Y) {
			facing = EnumFacing.NORTH;
		}
		return getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileMarket();
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
	@SuppressWarnings("deprecation")
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
		EnumFacing facing = state.getValue(FACING);
		BlockPos entityPos = pos.offset(facing.getOpposite());
		EntityMerchant.SpawnAnimationType spawnAnimationType = EntityMerchant.SpawnAnimationType.MAGIC;
		if(world.canBlockSeeSky(entityPos)) {
			spawnAnimationType = EntityMerchant.SpawnAnimationType.FALLING;
		} else if(!world.isAirBlock(entityPos.down())) {
			spawnAnimationType = EntityMerchant.SpawnAnimationType.DIGGING;
		}
		if(!world.isRemote) {
			EntityMerchant merchant = new EntityMerchant(world);
			merchant.setMarket(pos, facing);
			merchant.setToFacingAngle();
			merchant.setSpawnAnimation(spawnAnimationType);

			if(world.canBlockSeeSky(entityPos)) {
				merchant.setPosition(entityPos.getX() + 0.5, entityPos.getY() + 172, entityPos.getZ() + 0.5);
			} else if(!world.isAirBlock(entityPos.down())) {
				merchant.setPosition(entityPos.getX() + 0.5, entityPos.getY(), entityPos.getZ() + 0.5);
			} else {
				merchant.setPosition(entityPos.getX() + 0.5, entityPos.getY(), entityPos.getZ() + 0.5);
			}

			world.spawnEntityInWorld(merchant);
			merchant.onInitialSpawn(world.getDifficultyForLocation(pos), null);
		}
		if(spawnAnimationType == EntityMerchant.SpawnAnimationType.FALLING) {
			world.playSound(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, ModSounds.falling, SoundCategory.NEUTRAL, 1f, 1f, false);
		} else if(spawnAnimationType == EntityMerchant.SpawnAnimationType.DIGGING) {
//			world.playSound(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, ModSounds.falling, SoundCategory.NEUTRAL, 1f, 1f, false);
		} else {
			world.playSound(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 1f, 1f, false);
			for (int i = 0; i < 50; i++) {
				world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, (Math.random() - 0.5) * 0.5f, (Math.random() - 0.5) * 0.5f, (Math.random() - 0.5) * 0.5f);
			}
			world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0, 0);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			player.openGui(FarmingForBlockheads.instance, GuiHandler.MARKET, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
}
