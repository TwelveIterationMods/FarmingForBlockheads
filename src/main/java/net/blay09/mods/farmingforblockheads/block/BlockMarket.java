package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.entity.EntityMerchant;
import net.blay09.mods.farmingforblockheads.tile.TileMarket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockMarket extends BlockContainer {

    public static final String name = "market";
    public static final ResourceLocation registryName = new ResourceLocation(FarmingForBlockheads.MOD_ID, name);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockMarket() {
        super(Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2f));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TileMarket();
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext useContext) {
        return getDefaultState().with(FACING, useContext.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, @Nullable EntityLivingBase placer, ItemStack itemStack) {
        EnumFacing facing = state.get(FACING);
        BlockPos entityPos = pos.offset(facing.getOpposite());

        EntityMerchant.SpawnAnimationType spawnAnimationType = EntityMerchant.SpawnAnimationType.MAGIC;
        if (world.canBlockSeeSky(entityPos)) {
            spawnAnimationType = EntityMerchant.SpawnAnimationType.FALLING;
        } else if (!world.isAirBlock(entityPos.down())) {
            spawnAnimationType = EntityMerchant.SpawnAnimationType.DIGGING;
        }

        if (!world.isRemote) {
            EntityMerchant merchant = new EntityMerchant(world);
            merchant.setMarket(pos, facing);
            merchant.setToFacingAngle();
            merchant.setSpawnAnimation(spawnAnimationType);

            if (world.canBlockSeeSky(entityPos)) {
                merchant.setPosition(entityPos.getX() + 0.5, entityPos.getY() + 172, entityPos.getZ() + 0.5);
            } else if (!world.isAirBlock(entityPos.down())) {
                merchant.setPosition(entityPos.getX() + 0.5, entityPos.getY(), entityPos.getZ() + 0.5);
            } else {
                merchant.setPosition(entityPos.getX() + 0.5, entityPos.getY(), entityPos.getZ() + 0.5);
            }

            world.spawnEntity(merchant);
            merchant.onInitialSpawn(world.getDifficultyForLocation(pos), null, null);
        }
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileMarket) {
                NetworkHooks.openGui((EntityPlayerMP) player, (TileMarket) tileEntity, pos);
            }
        }
        return true;
    }

    @Override
    public IBlockState rotate(IBlockState state, IWorld world, BlockPos pos, Rotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public IBlockState mirror(IBlockState state, Mirror mirror) {
        EnumFacing facing = state.get(FACING);
        return state.with(FACING, mirror.toRotation(facing).rotate(facing));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
