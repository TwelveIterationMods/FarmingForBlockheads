package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.entity.MerchantEntity;
import net.blay09.mods.farmingforblockheads.entity.ModEntities;
import net.blay09.mods.farmingforblockheads.tile.MarketTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class MarketBlock extends ContainerBlock {

    public static final String name = "market";
    public static final ResourceLocation registryName = new ResourceLocation(FarmingForBlockheads.MOD_ID, name);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape RENDER_SHAPE = Block.makeCuboidShape(0, 0.01, 0, 16, 16, 16);

    public MarketBlock() {
        super(Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2f));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new MarketTileEntity();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        return getDefaultState().with(FACING, useContext.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction facing = state.get(FACING);
        BlockPos entityPos = pos.offset(facing.getOpposite());

        MerchantEntity.SpawnAnimationType spawnAnimationType = MerchantEntity.SpawnAnimationType.MAGIC;
        if (world.canBlockSeeSky(entityPos)) {
            spawnAnimationType = MerchantEntity.SpawnAnimationType.FALLING;
        } else if (!world.isAirBlock(entityPos.down())) {
            spawnAnimationType = MerchantEntity.SpawnAnimationType.DIGGING;
        }

        if (!world.isRemote) {
            MerchantEntity merchant = new MerchantEntity(ModEntities.merchant, world);
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

            world.addEntity(merchant);
            merchant.onInitialSpawn(((ServerWorld) world), world.getDifficultyForLocation(pos), SpawnReason.STRUCTURE, null, null);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof MarketTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (MarketTileEntity) tileEntity, pos);
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        Direction facing = state.get(FACING);
        return state.with(FACING, mirror.toRotation(facing).rotate(facing));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return RENDER_SHAPE;
    }
}
