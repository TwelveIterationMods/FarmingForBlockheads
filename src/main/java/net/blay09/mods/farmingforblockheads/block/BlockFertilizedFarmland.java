package net.blay09.mods.farmingforblockheads.block;

import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.ModConfig;
import net.blay09.mods.farmingforblockheads.item.ItemFertilizer;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public abstract class BlockFertilizedFarmland extends BlockFarmland {
    protected final boolean isStable;

    public BlockFertilizedFarmland(boolean isStable) {
        this.isStable = isStable;
        setSoundType(SoundType.GROUND);
        setHardness(1f);
        setCreativeTab(FarmingForBlockheads.creativeTab);
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return true;
    }

    @Override
    public boolean isFertile(World world, BlockPos pos) {
        return world.getBlockState(pos).getValue(MOISTURE) > 0;
    }

    public float getDoubleGrowthChance() {
        return 0f;
    }

    public float getBonusCropChance() {
        return 0f;
    }

    public float getRegressionChance() {
        return ModConfig.general.fertilizerRegressionChance;
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        if (!isStable) {
            super.onFallenUpon(world, pos, entity, fallDistance);
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int moisture = state.getValue(MOISTURE);

        if (!this.hasWater(world, pos) && !world.isRainingAt(pos.up())) {
            if (moisture > 0) {
                world.setBlockState(pos, state.withProperty(MOISTURE, moisture - 1), 2);
            } else if (!this.hasCrops(world, pos) && !isStable) {
                turnToDirt(world, pos);
            }
        } else if (moisture < 7) {
            world.setBlockState(pos, state.withProperty(MOISTURE, 7), 2);
        }
    }

    public abstract IBlockState applyFertilizer(IBlockState state, ItemFertilizer.FertilizerType fertilizerType);
}
